<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>QRCODE</title>
    <style type="text/css">
    </style>
    <script type="text/javascript" src="js/lib/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/lib/md5.min.js"></script>
    <script type="text/javascript" src="js/jsqrcode/grid.js"></script>
    <script type="text/javascript" src="js/jsqrcode/version.js"></script>
    <script type="text/javascript" src="js/jsqrcode/detector.js"></script>
    <script type="text/javascript" src="js/jsqrcode/formatinf.js"></script>
    <script type="text/javascript" src="js/jsqrcode/errorlevel.js"></script>
    <script type="text/javascript" src="js/jsqrcode/bitmat.js"></script>
    <script type="text/javascript" src="js/jsqrcode/datablock.js"></script>
    <script type="text/javascript" src="js/jsqrcode/bmparser.js"></script>
    <script type="text/javascript" src="js/jsqrcode/datamask.js"></script>
    <script type="text/javascript" src="js/jsqrcode/rsdecoder.js"></script>
    <script type="text/javascript" src="js/jsqrcode/gf256poly.js"></script>
    <script type="text/javascript" src="js/jsqrcode/gf256.js"></script>
    <script type="text/javascript" src="js/jsqrcode/decoder.js"></script>
    <script type="text/javascript" src="js/jsqrcode/qrcode.js"></script>
    <script type="text/javascript" src="js/jsqrcode/findpat.js"></script>
    <script type="text/javascript" src="js/jsqrcode/alignpat.js"></script>
    <script type="text/javascript" src="js/jsqrcode/databr.js"></script>
    <script type="text/javascript" src="js/scan_init.js"></script>
    <script type="text/javascript" src="js/scan_imghandle.js"></script>
    <script type="text/javascript" src="js/scan_dathandle.js"></script>
</head>

<body onload="load()">
<input type="file" onchange="handleFiles(this.files)">
<canvas id="qr-canvas" width="640" height="480"></canvas>
</body>

</html>