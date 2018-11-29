package iQRGenuine;

import iQRGenuine.util.DataConnection;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

@WebServlet(name = "Search", urlPatterns = {"search"}, loadOnStartup = 1)
public class Search extends HttpServlet
{
    private Statement stmt;

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
            ResultSet rs = stmt.executeQuery(DataConnection.selectStatement(cd_key, md5_info));
            if (!rs.next())
            {
                response.getWriter().print("This cd-key is not exist or has been verified.");
                return;
            }
            String public_key = rs.getString(DataConnection.colname_public_key);
            response.getWriter().print(public_key);
            stmt.execute(DataConnection.verifyStatement(cd_key, md5_info));
        }
        catch (SQLException ex)
        {
            System.out.println("Query failed");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
