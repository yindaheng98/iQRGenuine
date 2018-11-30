<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iQRGenuine.util.DataConnection" %>
<%
    String error_resp = "<script>" +
            "alert('%s');" +
            "window.location.href='login.html';" +
            "</script>";
    try//先登录验证
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null)
        {
            String resp_str = String.format(error_resp,
                    "Please input username and password.");
            response.getWriter().print(resp_str);
            return;
        }
        DataConnection dataConn=new DataConnection();
        if (!dataConn.loginPass(username,password))
        {
            String resp_str = String.format(error_resp,
                    "This username is not exist or the password is wrong.");
            response.getWriter().print(resp_str);
            return;
        }
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("username", username);
        httpSession.setAttribute("password", password);
    }
    catch (Exception e)
    {
        String resp_str = String.format(error_resp,
                "Something went wrong.");
        response.getWriter().print(resp_str);
        e.printStackTrace();
        return;
    }
%>
<html>
<head>
    <title>QRCodeGenerator</title>
    <script src="js/lib/md5.min.js"></script>
    <script src="js/lib/jquery-3.3.1.min.js"></script>
    <script src="js/lib/jquery.qrcode.min.js"></script>
    <script src="js/Generator/generate_click.js"></script>
</head>
<body>
<form action="#">
    <label for="info">Input a product info</label>
    <input type="text" id="info">
    <input type="button" onclick="generate_click($('#info').val())">
    <div id="qrcode"></div>
</form>
</body>
</html>
