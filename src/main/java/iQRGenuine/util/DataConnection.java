package iQRGenuine.util;

import java.sql.DriverManager;
import java.sql.Statement;

public class DataConnection
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tests?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "tests";
    private static final String PASS = "tests";

    private static final String tablename = "public_keys";
    private static final String colname_cd_key = "cd_key";
    private static final String colname_md5_info = "md5_info";
    public static final String colname_public_key = "public_key";
    private static final String colname_verified = "verified";

    public static Statement initConn() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, USER, PASS).createStatement();
    }

    public static String insertStatement(String md5_info, String public_key)
    {
        return String.format(
                "INSERT INTO %s(%s,%s)VALUES('%s','%s')",
                tablename,
                colname_md5_info, colname_public_key,
                md5_info, public_key
        );
    }

    public static String selectStatement(String cd_key, String md5_info)
    {
        return String.format(
                "SELECT %s FROM %s WHERE %s='%s' AND %s='%s' AND %s=%d",
                colname_public_key,
                tablename,
                colname_cd_key, cd_key,
                colname_md5_info, md5_info,
                colname_verified, 0
        );
    }

    public static String verifyStatement(String cd_key, String md5_info)
    {
        return String.format(
                "UPDATE %s SET %s=%d WHERE %s='%s' AND %s='%s'",
                tablename,
                colname_verified, 1,
                colname_cd_key, cd_key,
                colname_md5_info, md5_info
        );
    }
}