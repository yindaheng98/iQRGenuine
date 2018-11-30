package iQRGenuine;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;

import iQRGenuine.util.DataConnection;
import iQRGenuine.util.RSATool;


@WebServlet(name = "Generate", urlPatterns = {"generate"}, loadOnStartup = 1)
public class Generate extends HttpServlet
{
    private DataConnection dataConn;
    private static String index_state = "state";
    private static String index_info = "info";
    private static String index_cdkey = "cdkey";
    private static String index_cipher = "cipher";
    private static String response_f = String.format(
            "{\"%s\":\"%%s\",\"%s\":\"%%s\",\"%s\":\"%%s\",\"%s\":\"%%s\"}",
            index_state,
            index_info,
            index_cdkey,
            index_cipher);

    public void init() throws ServletException
    {
        try
        {
            dataConn = new DataConnection();
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
        HttpSession httpSession = request.getSession();
        String username = (String) httpSession.getAttribute("username");
        String password = (String) httpSession.getAttribute("password");
        if (username == null || password == null)
        {
            response.getWriter().print(String.format(
                    response_f,
                    0,
                    "Please login.",
                    "null",
                    "null"));
            return;
        }//登录验证
        String md5_info = request.getParameter("md5_info");
        if (md5_info == null)
        {
            response.getWriter().print(String.format(
                    response_f,
                    0,
                    "Please input md5_info.",
                    "null",
                    "null"));
            return;
        }//数据验证
        try
        {
            if (!dataConn.loginPass(username, password))
            {
                response.getWriter().print("Please login");
                return;
            }//登录验证
            byte[][] RSAkeys = RSATool.getKeyPairBytes();
            byte[] private_key = RSAkeys[0];
            byte[] public_key = RSAkeys[1];
            String public_key_str = RSATool.base64Encode(public_key);
            String cd_key = dataConn.insertInfo(md5_info, public_key_str);//插入信息获取cdk
            byte[] cipher_bytes = RSATool.encrypt(private_key, cd_key.getBytes(StandardCharsets.UTF_8));
            String cipher_text = RSATool.base64Encode(cipher_bytes);
            response.getWriter().print(String.format(
                    response_f,
                    1,
                    "Insert success.",
                    cd_key,
                    cipher_text));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
