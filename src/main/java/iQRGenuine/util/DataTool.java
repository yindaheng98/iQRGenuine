package iQRGenuine.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataTool
{
    private static final String DB_URL = "jdbc:mysql://mysql:3306/iqrgenuine?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static final String USER = "iqrgenuine";
    private static final String PASS = "iqrgenuine";

    private static final String key_tablename = "public_keys";
    static final String colname_cd_key = "cd_key";
    static final String colname_md5_info = "md5_info";
    static final String colname_public_key = "public_key";
    static final String colname_verified = "verified";

    private static final String user_tablename = "user_infos";
    private static final String colname_username = "username";
    private static final String colname_md5_password = "md5_password";

    private static Connection connection = null;

    static Statement initConn() throws Exception
    {
        if(connection!=null)connection.close();
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        return connection.createStatement();
    }

    public static String initStatement(int n)
    {
        return String.format(
                "SELECT * FROM %s ORDER BY %s DESC LIMIT %d",
                key_tablename,
                colname_cd_key,
                n
        );
    }

    public static String insertStatement(String md5_info, String public_key)
    {
        return String.format(
                "INSERT INTO %s(%s,%s)VALUES('%s','%s')",
                key_tablename,
                colname_md5_info, colname_public_key,
                md5_info, public_key
        );
    }

    public static String selectStatement(String cd_key, String md5_info)
    {
        return String.format(
                "SELECT %s FROM %s WHERE %s='%s' AND %s='%s' AND %s=%d",
                colname_public_key,
                key_tablename,
                colname_cd_key, cd_key,
                colname_md5_info, md5_info,
                colname_verified, 0
        );
    }

    public static String verifyStatement(String cd_key, String md5_info)
    {
        return String.format(
                "UPDATE %s SET %s=%d WHERE %s='%s' AND %s='%s'",
                key_tablename,
                colname_verified, 1,
                colname_cd_key, cd_key,
                colname_md5_info, md5_info
        );
    }

    public static String loginStatement(String username, String password)
    {
        return String.format(
                "SELECT * FROM %s WHERE %s='%s' AND %s=md5('%s')",
                user_tablename,
                colname_username, username,
                colname_md5_password, password
        );
    }
}
