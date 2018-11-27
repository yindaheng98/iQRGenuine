package iQRGenuine.generate;

import java.io.IOException;
import javax.crypto.KeyGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import iQRGenuine.util.RSATool;

import iQRGenuine.util.DataConnection;

@WebServlet(name = "Generate", urlPatterns = {"generate"}, loadOnStartup = 1)
public class Generate extends HttpServlet
{
    private Statement stmt;

    public void init() throws ServletException
    {
        try
        {
            stmt= DataConnection.initConn();

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
        String md5_info = request.getParameter("info");
        String public_key = request.getParameter("pk");

        if (md5_info == null || public_key == null)
        {
            response.getWriter().print("Please input cd-key and info.");
            return;
        }
        try
        {
            stmt.execute(DataConnection.insertStatement(md5_info,public_key));
            response.getWriter().print("Insert success.");
            //此处应返回CD-Key
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
