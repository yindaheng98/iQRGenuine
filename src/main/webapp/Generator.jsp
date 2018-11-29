<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="iQRGenuine.util.DataConnection" %>
<%
    try
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null)
        {
            String resp_str = "<script>" +
                    "alert('Please input username and password.');" +
                    "</script>";
            response.getWriter().print(resp_str);
            response.sendRedirect("login.html");
            return;
        }
        Statement stmt = DataConnection.initConn();
        ResultSet rs = stmt.executeQuery(DataConnection.loginStatement(username, password));
        if (!rs.next())
        {
            String resp_str = "<script>" +
                    "alert('This username is not exist or the password is wrong.');" +
                    "</script>";
            response.getWriter().print(resp_str);
            response.sendRedirect("login.html");
            return;
        }
    }
    catch (Exception e)
    {
        String resp_str = "<script>" +
                "alert('Something went wrong.');" +
                "</script>";
        response.getWriter().print(resp_str);
        response.sendRedirect("login.html");
        e.printStackTrace();
        return;
    }
%>
<html>
<head>
    <title>QRCodeGenerator</title>
</head>
<body>

</body>
</html>
