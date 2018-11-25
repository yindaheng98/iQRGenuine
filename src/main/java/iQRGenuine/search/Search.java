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
    private static final String sql_f = String.format(
            "SELECT %s FROM public_keys WHERE id='%%s' AND cd_key='%%s' AND verified=0", key_col
    );

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
        String id = request.getParameter("id");
        String cd_key = request.getParameter("cdk");
        if (cd_key == null || id == null)
        {
            response.getWriter().print("Please input id and cd-key.");
            return;
        }
        try
        {
            ResultSet rs = stmt.executeQuery(String.format(sql_f, id, cd_key));
            if(!rs.next())
            {
                response.getWriter().print("This cd-key is not exist or has been verified.");
                return;
            }
            String public_key = rs.getString(key_col);
            response.getWriter().print(public_key);
        }
        catch (SQLException ex)
        {
            System.out.println(String.format(sql_f, id, cd_key));
            System.out.println("Query failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
