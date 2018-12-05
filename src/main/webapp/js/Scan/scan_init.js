var video;
var canvas;
var run_cam=false;

$(document).ready(function ()
                  {
                      video = document.getElementById('video');
                      canvas = document.getElementById('canvas');
                      //设置解码后的回调函数，handleData(d)在scan_dathandle.js里面
                      $("#verify").click(function ()
                                         {
                                             run_cam=false;
                                             qrcode.callback = handleData;
                                             document.getElementById("btn_file").click();
                                         });
                  });

function initCam()//打开相机
{
    run_cam=true;
    var getUserMedia =
        navigator.getUserMedia ||
        navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia ||
        navigator.msGetUserMedia;
    getUserMedia.call(navigator, {
        video: true,
        audio: false
    }, function (localMediaStream)
                      {
                          video.src = window.URL.createObjectURL(localMediaStream);
                          video.onloadedmetadata = function (e)
                          {
                              console.log('Error!', e);
                              console.log("Label: " + localMediaStream.label);
                              console.log("AudioTracks", localMediaStream.getAudioTracks());
                              console.log("VideoTracks", localMediaStream.getVideoTracks());
                          };
                      }, function (e)
                      {
                          console.log('Rejected!', e);
                      });
    qrcode.callback = cam_handleData;
    $("#comp-main").hide();
    $("#comp-camera").show();
    Screenshot();//开始扫描
}

function Screenshot()
{
    var scale=Math.min(canvas.style.width/video.videoWidth,canvas.style.height/video.videoHeight);
    canvas.getContext('2d').drawImage(video, 0, 0, video.videoWidth * scale, video.videoHeight * scale);
    var imgData = canvas.toDataURL("image/png");
    qrcode.decode(imgData);
}

function cam_handleData(data)
{
    if(data === "error decoding QR Code")
        setTimeout(Screenshot(), 1000);
    else handleData(data);
}


