package iQRGenuine.util;

import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataConnection
{
    private Statement stmt;

    //private Jedis jedis;
    public DataConnection() throws Exception
    {
        stmt = DataTool.initConn();
        //jedis=new Jedis("localhost");
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
        return "" + rs.getInt(1);//获取主键CD_Key值
    }

    /**
     * 输入防伪验证信息获取公钥
     *
     * @param cd_key 产品序列号
     * @param md5_info 产品信息的MD5码
     * @return 公钥
     * @throws SQLException 查询失败
     */
    public String verifyInfo(String cd_key, String md5_info) throws SQLException
    {
        ResultSet rs = stmt.executeQuery(DataTool.selectStatement(cd_key, md5_info));
        if (!rs.next())throw new SQLException("查无此产品");
        String public_key = rs.getString(DataTool.colname_public_key);
        stmt.execute(DataTool.verifyStatement(cd_key, md5_info));
        return public_key;
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
