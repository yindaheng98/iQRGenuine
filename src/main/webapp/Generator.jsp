<%@ page contentType="text/html;charset=UTF-8"%>
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
        DataConnection dataConn = new DataConnection();
        if (!dataConn.loginPass(username, password))
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
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, initial-scale=1.0, user-scalable=no">
    <!-- 引入 FrozenUI -->
    <link rel="stylesheet" href="https://i.gtimg.cn/vipstyle/frozenui/2.0.0/css/frozen.css"/>
    <title>iQRGenuine-Generator</title>
    <script src="js/lib/md5.min.js"></script>
    <script src="js/lib/jquery-3.3.1.min.js"></script>
    <script src="js/Generator/generate_click.js"></script>
    <script type="text/javascript" src="js/lib/qrcode.js"></script>
    <script type="text/javascript" src="js/lib/qrcode_SJIS.js"></script>
</head>
<body style="background-color: #fff">
<div class="ui-form ui-border-t" style="max-width: 575px;margin: 0 auto;">
    <form action="">
        <section class="ui-panel ui-panel-center ui-border-tb">
            <h2 class="ui-arrowlink"><span>产品信息</span></h2>
        </section>
        <div class="ui-form-item ui-form-item-pure ui-border-b" style="height: 150px;">
            <textarea name="info" id="info" placeholder="Input some information here."
                      style="height: 98%;width: 99%;">
                Input some information here.
            </textarea>
        </div>

        <section class="ui-panel ui-panel-center ui-border-tb">
            <h2 class="ui-arrowlink"><span>字符模式</span></h2>
        </section>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="mb-default">
                <input type="radio" name="mb" value="default" id="mb-default" checked="checked">
            </label>
            <label for="mb-default">默认</label>
        </div>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="mb-utf8">
                <input type="radio" name="mb" value="UTF-8" id="mb-utf8">
            </label>
            <label for="mb-utf8">UTF-8</label>
        </div>

        <section class="ui-panel ui-panel-center ui-border-tb">
            <h2 class="ui-arrowlink"><span>纠错级别</span></h2>
        </section>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="eL">
                <input type="radio" name="e" value="L" id="eL">
            </label>
            <label for="eL">L级(容错率7%)</label>
        </div>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="eM">
                <input type="radio" name="e" value="M" id="eM" checked="checked">
            </label>
            <label for="eM">M级(容错率15%)</label>
        </div>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="eQ">
                <input type="radio" name="e" value="Q" id="eQ">
            </label>
            <label for="eQ">Q级(容错率25%)</label>
        </div>
        <div class="ui-form-item ui-form-item-radio ui-border-b">
            <label class="ui-radio" for="eH">
                <input type="radio" name="e" value="H" id="eH">
            </label>
            <label for="eH">H级(容错率30%)</label>
        </div>
        <div class="ui-btn-wrap">
            <div class="ui-btn-lg ui-btn-primary" onclick="generate_click()">
                生成二维码
            </div>
        </div>
    </form>
    <div class="ui-feeds">
        <ul>
            <li id="qr" style="text-align: center">
            </li>
        </ul>
    </div>
</div>
</body>
</html>