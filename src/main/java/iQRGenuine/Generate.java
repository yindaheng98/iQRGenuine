package iQRGenuine;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.sql.ResultSet;

import iQRGenuine.util.RSATool;

import iQRGenuine.util.DataConnection;

@WebServlet(name = "Generate", urlPatterns = {"generate"}, loadOnStartup = 1)
public class Generate extends HttpServlet
{
    private Statement stmt;
    private static String index_state = "state";
    private static String index_info = "info";
    private static String index_cipher = "cipher";
    private static String response_f = String.format(
            "{\"%s\":\"%%s\",\"%s\":\"%%s\",\"%s\":\"%%s\"}",
            index_state,
            index_info,
            index_cipher);

    public void init() throws ServletException
    {
        try
        {
            stmt = DataConnection.initConn();
        }
        catch (Exception ex)
        {
            // handle any errors
            System.out.println("init() failed");
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String md5_info = request.getParameter("md5_info");

        if (md5_info == null)
        {
            response.getWriter().print("Please input md5_info.");
            return;
        }//获取MD5产品信息摘要
        byte[][] RSAkeys = RSATool.getKeyPairBytes();
        byte[] private_key = RSAkeys[0];
        byte[] public_key = RSAkeys[1];
        String cd_key;//准备好加密要素
        try
        {
            String public_key_str = RSATool.base64Encode(public_key);
            stmt.execute(DataConnection.insertStatement(md5_info, public_key_str),
                    Statement.RETURN_GENERATED_KEYS);//转码为Base64然后插入到数据库
            //返回auto_increment的主键CD-Key
            ResultSet rs = stmt.getGeneratedKeys();
            if (!rs.next())//如果查不到主键就报错返回
            {
                response.getWriter().print(String.format(
                        response_f,
                        0,
                        "getGeneratedKeys() failed.",
                        "null"));
                return;
            }
            cd_key = "" + rs.getInt(1);//获取主键CD_Key值
            byte[] cipher_bytes = RSATool.encrypt(private_key, cd_key.getBytes(StandardCharsets.UTF_8));
            String cipher_text = RSATool.base64Encode(cipher_bytes);
            response.getWriter().print(String.format(
                    response_f,
                    1,
                    "Insert success.",
                    cipher_text));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
