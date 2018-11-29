var gCtx = null;
var gCanvas = null;
var imageData = null;

function initCanvas(w, h)//初始化拖动框
{
    gCanvas = document.getElementById("qr-canvas");
    gCanvas.addEventListener("dragenter", function (ev)
    {
        ev.stopPropagation();
        ev.preventDefault();
    }, false);
    gCanvas.addEventListener("dragover", function (ev)
    {
        ev.stopPropagation();
        ev.preventDefault();
    }, false);
    gCanvas.addEventListener("drop", function (ev)
    {
        ev.stopPropagation();
        ev.preventDefault();
        var dt = ev.dataTransfer;
        var files = dt.files;
        handleFiles(files);
    }, false);
    gCanvas.style.width = w + "px";
    gCanvas.style.height = h + "px";
    gCanvas.width = w;
    gCanvas.height = h;
    gCtx = gCanvas.getContext("2d");
    gCtx.clearRect(0, 0, w, h);
    imageData = gCtx.getImageData(0, 0, 320, 240);
}

function load()
{
    initCanvas(640, 480);
    qrcode.callback = handleData;
    //解码后的回调函数，handleData(d)在scan_dathandle.js里面
}

