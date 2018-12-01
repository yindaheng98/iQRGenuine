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
<!doctype html>
<html>
<head>

    <title>QRCodeGenerator</title>
    <script src="js/lib/md5.min.js"></script>
    <script src="js/lib/jquery-3.3.1.min.js"></script>
    <script src="js/Generator/generate_click.js"></script>

    <meta http-equiv="Content-Type" content="text/html; charset=Utf-8">
    <script type="text/javascript" src="js/lib/qrcode.js"></script>
    <!-- SJIS Support (optional) -->
    <script type="text/javascript" src="js/lib/qrcode_SJIS.js"></script>
    <title>QR Code Generator for JavaScript</title>
</head>
<body>
<form name="qrForm">
    <span>ErrorCorrectionLevel:</span>
    <select name="e">
        <option value="L">L(7%)</option>
        <option value="M" selected="selected">M(15%)</option>
        <option value="Q">Q(25%)</option>
        <option value="H">H(30%)</option>
    </select>
    <span>Mode:</span>
    <select name="m">
        <option value="Numeric">Numeric</option>
        <option value="Alphanumeric">Alphanumeric</option>
        <option value="Byte" selected>Byte</option>
        <option value="Kanji">Kanji</option>
    </select>
    <span>Multibyte:</span>
    <select name="mb">
        <option value="default">None</option>
        <option value="SJIS">SJIS</option>
        <option value="UTF-8" selected>UTF-8</option>
    </select>
    <br/>
    <textarea name="msg" rows="10" cols="40" id="info">here comes qr!</textarea>
    <br/>
    <input type="button" value="update" onclick="generate_click()"/>
    <div id="qr"></div>
</form>
</body>
</html>