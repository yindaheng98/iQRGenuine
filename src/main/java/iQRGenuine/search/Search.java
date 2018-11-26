package iQRGenuine.search;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

@WebServlet(name = "Search", urlPatterns = {"search"}, loadOnStartup = 1)
public class Search extends HttpServlet
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tests?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "tests";
    private static final String PASS = "tests";

    private Statement stmt;
    private static final String key_col = "public_key";
    private static final String sql_qf = String.format(
            "SELECT %s FROM public_keys WHERE cd_key='%%s' AND md5_info='%%s' AND verified=0", key_col
    );
    private static final String sql_df =
            "UPDATE public_keys SET verified=1 WHERE cd_key='%s' AND md5_info='%s'";

    public void init() throws ServletException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            stmt = DriverManager.getConnection(DB_URL, USER, PASS).createStatement();
        }
        catch (Exception ex)
        {
            // handle any errors
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
            response.getWriter().print("Please input cd-key and info.");
            return;
        }
        try
        {
            ResultSet rs = stmt.executeQuery(String.format(sql_qf, cd_key, md5_info));
            if (!rs.next())
            {
                response.getWriter().print("This cd-key is not exist or has been verified.");
                return;
            }
            String public_key = rs.getString(key_col);
            response.getWriter().print(public_key);
            stmt.execute(String.format(sql_df, cd_key, md5_info));
        }
        catch (SQLException ex)
        {
            System.out.println(String.format(sql_df, cd_key, md5_info));
            System.out.println("Query failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
