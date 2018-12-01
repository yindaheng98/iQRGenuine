package iQRGenuine.util;

import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataConnection
{
    private Statement stmt;
    private Jedis jedis;
    private boolean redis_avail = false;//此变量代表redis数据库的可用性

    public DataConnection() throws Exception
    {
        stmt = DataTool.initConn();
        try
        {
            jedis = new Jedis("localhost");
            if (jedis.ping().equals("PONG"))
                redis_avail = true;//如果成功连上了redis可用性就为true
            redis_init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void redis_init() throws SQLException
    {
        ResultSet rs = stmt.executeQuery(DataTool.initStatement(500));
        while (rs.next())
        {
            String cd_key = "" + rs.getInt(DataTool.colname_cd_key);
            int verified = rs.getInt(DataTool.colname_verified);
            if (verified == 0)//如果是未验证的产品
            {
                String md5_info = rs.getString(DataTool.colname_md5_info);
                String public_key=rs.getString(DataTool.colname_public_key);
                jedis.set(cd_key, md5_info + " " + public_key + " " + "0");
                //就写入全部信息
            }
            else
                jedis.set(cd_key, "1");//如果是已确认的信息就直接写1
        }
    }

    /**
     * 插入产品信息和公钥，获取cdkey
     *
     * @param md5_info   产品md5信息
     * @param public_key 产品公钥
     * @return 插入后auto_increment来的主键值
     * @throws SQLException SQL查询出错
     */
    public String insertInfo(String md5_info, String public_key) throws SQLException
    {
        stmt.execute(DataTool.insertStatement(md5_info, public_key),
                Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (!rs.next())//如果查不到主键就报错返回
            throw new SQLException("获取主键失败");
        String cd_key = "" + rs.getInt(1);
        redis_insertInfo(cd_key, md5_info, public_key);
        return cd_key;//获取主键CD_Key值
    }

    private void redis_insertInfo(String cd_key, String md5_info, String public_key)
    {
        if (!redis_avail) return;
        jedis.set(cd_key, md5_info + " " + public_key + " " + "0");
    }

    /**
     * 输入防伪验证信息获取公钥
     *
     * @param cd_key   产品序列号
     * @param md5_info 产品信息的MD5码
     * @return 公钥
     * @throws SQLException 查询失败
     */
    public String verifyInfo(String cd_key, String md5_info) throws Exception
    {
        String public_key = redis_verifyInfo(cd_key, md5_info);//先在redis里面找
        if (public_key != null)
        {
            stmt.execute(DataTool.verifyStatement(cd_key, md5_info));
            return public_key;//找到就写数据库然后返回
        }
        ResultSet rs = stmt.executeQuery(DataTool.selectStatement(cd_key, md5_info));
        if (!rs.next()) throw new Exception("查无此产品");
        public_key = rs.getString(DataTool.colname_public_key);
        stmt.execute(DataTool.verifyStatement(cd_key, md5_info));
        return public_key;
    }

    private String redis_verifyInfo(String cd_key, String md5_info) throws Exception
    {
        if (!jedis.exists(cd_key))
            return null;//序列号不存在，可能是没写进redis，返回null继续搜mysql数据库
        String[] infos = jedis.get(cd_key).split(" ");
        if (infos[0].equals("1") || !infos[0].equals(md5_info))
            throw new Exception("查无此产品");//序列号存在但是已经查过或者信息对不上直接报错退出
        jedis.set(cd_key, "1");//查过的序列号直接记成1
        return infos[1];
    }

    /**
     * login的用户名密码确认
     *
     * @param username 用户名
     * @param password 密码
     * @return 验证是否成功
     * @throws SQLException 查询失败
     */
    public boolean loginPass(String username, String password) throws SQLException
    {
        ResultSet rs = stmt.executeQuery(DataTool.loginStatement(username, password));
        return rs.next();
    }
}
