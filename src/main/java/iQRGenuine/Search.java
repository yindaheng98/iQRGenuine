package iQRGenuine;

import iQRGenuine.util.DataConnection;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Search", urlPatterns = {"search"}, loadOnStartup = 1)
public class Search extends HttpServlet
{
    private DataConnection dataConn;
    private static String index_state = "state";
    private static String index_info = "info";
    private static String index_pkey = "public_key";
    private static String response_f = String.format(
            "{\"%s\":\"%%s\",\"%s\":\"%%s\",\"%s\":\"%%s\"}",
            index_state,
            index_info,
            index_pkey);

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
                            "Please input cd-key and info.",
                            "null"));
            return;
        }
        try
        {
            String public_key = dataConn.verifyInfo(cd_key, md5_info);
            response.getWriter().print(
                    String.format(response_f,
                            1,
                            "Search success",
                            public_key));
        }
        catch (Exception e)
        {
            response.getWriter().print(
                    String.format(response_f,
                            0,
                            "This cd-key is not exist or has been verified.",
                            "null"));
            e.printStackTrace();
        }
    }
}
