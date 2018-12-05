package iQRGenuine;

import iQRGenuine.util.DataConnection;
import iQRGenuine.util.RSATool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Search2", urlPatterns = {"search2"}, loadOnStartup = 1)
public class Search2 extends HttpServlet
{
    private DataConnection dataConn;
    private static String index_state = "state";
    private static String index_info = "info";
    private static String response_f = String.format(
            "{\"%s\":\"%%s\",\"%s\":\"%%s\"}",
            index_state,
            index_info);

    public void init() throws ServletException
    {
        try
        {
            dataConn = new DataConnection();
        }
        catch (Exception ex)
        {
            System.out.println("init() failed");
            System.out.println("SQLException: " + ex.getMessage());
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
        String cd_key = request.getParameter("cdk");
        String md5_info = request.getParameter("info");

        if (cd_key == null || md5_info == null)
        {
            response.getWriter().print(
                    String.format(response_f,
                            0,
                            "Please input cd-key and info."));
            return;
        }
        try
        {
            String public_key = dataConn.findInfo(cd_key, md5_info);
            String cipher_text = request.getParameter("cpt");
            if (cipher_text == null)
            {
                response.getWriter().print(
                        String.format(response_f,
                                0,
                                "Please input cipher_text."));
                return;
            }
            cipher_text = cipher_text.replaceAll(" ", "+");
            byte[] b_public_key = RSATool.base64Decode(public_key);
            byte[] b_cipher_text = RSATool.base64Decode(cipher_text);
            byte[] b_cd_key = RSATool.decrypt(b_public_key, b_cipher_text);
            String cd_key_to_comp = new String(b_cd_key, StandardCharsets.UTF_8);
            if (cd_key_to_comp.equals(cd_key))
            {
                dataConn.updateInfo(cd_key, md5_info);
                response.getWriter().print(
                        String.format(response_f,
                                1,
                                "Auth success",
                                public_key));
            }
            else
            {
                response.getWriter().print(
                        String.format(response_f,
                                2,
                                "Auth failed",
                                public_key));
            }
        }
        catch (Exception e)
        {
            response.getWriter().print(
                    String.format(response_f,
                            2,
                            "This cd-key is not exist or has been verified."));
            e.printStackTrace();
        }
    }
}
