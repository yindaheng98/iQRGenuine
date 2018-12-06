var video;
var canvas;
var run_cam = false;
var camera_ids = [];
var cam_num = 0;

$(document).ready(function ()
                  {
                      video = document.getElementById('video');
                      canvas = document.getElementById('canvas');
                      qrcode.callback = handleData;
                      //设置解码后的回调函数，handleData(d)在scan_dathandle.js里面
                      $("#verify").click(function ()
                                         {
                                             run_cam = false;
                                             document.getElementById("btn_file").click();
                                         });
                  });

function initCam()//打开相机
{
    if(!navigator.mediaDevices || !navigator.mediaDevices.enumerateDevices)
    {
        alert("enumerateDevices() not supported.");
        return;
    }
    run_cam = true;
    navigator.mediaDevices.enumerateDevices()
        .then(function (devices)
              {
                  devices.forEach(function (device)//获取全部的摄像设备
                                  {
                                      if(device.kind.indexOf("video") !== -1)
                                          camera_ids.push(device.deviceId);
                                  });
                  $("#comp-main").hide();
                  $("#comp-camera").show();
                  switch_cam();//开始录像
              })
        .catch(function (err)//不支持enumerateDevices()时
               {
                   navigator.mediaDevices.getUserMedia(
                       {
                           audio: false,
                           video: true
                       }).then(function (stream)
                               {
                                   stream_func(stream);
                                   $("#cam-icon").hide();//不支持enumerateDevices()时不能切摄像头
                               }).catch(function (err)//连摄像头都开不了时
                                        {
                                            alert("不支持摄像头");
                                            run_cam = false;
                                        });
               });
}

var l_sample = 0;//取样边长
var scale = 0;//缩放比
var w_draw = 0;
var h_draw = 0;//canvas绘图大小
var x_draw = 0;
var y_draw = 0;//canvas绘图位置
function stream_func(stream)
{
    video.src = window.URL.createObjectURL(stream);
    video.play();
    canvas.width = 400;
    canvas.height = 400;
    run_cam = false;//先把之前的Screenshot()停了
    setTimeout(function ()
               {
                   run_cam = true;
                   Screenshot()
               }, 300);//等之前的Screenshot()完全停了再开
}

function switch_cam()//切换摄像头
{
    cam_num = (cam_num + 1) % camera_ids.length;
    navigator.mediaDevices.getUserMedia({
                                            audio: false,
                                            video: {deviceId: {exact: camera_ids[cam_num]}}
                                        }).then(stream_func).catch(function (err)
                                                                   {
                                                                       alert("不支持此摄像头");
                                                                   });
}

function Screenshot()
{
    l_sample = Math.min(video.videoWidth, video.videoHeight) / 2;//计算取样边长
    scale = canvas.width / l_sample;//计算缩放比
    w_draw = video.videoWidth * scale;
    h_draw = video.videoHeight * scale;//计算canvas绘图大小
    x_draw = -(w_draw - canvas.width) / 2;
    y_draw = -(h_draw - canvas.height) / 2;//计算canvas绘图位置
    canvas.getContext('2d').drawImage(video, x_draw, y_draw, w_draw, h_draw);
    var imgData = canvas.toDataURL("image/png");
    qrcode.decode(imgData);
}


