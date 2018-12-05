function handleData(data)
{
    var d=null;
    try {
        d=JSON.parse(data);
    } catch(e) {
        if(run_cam) setTimeout(Screenshot(), 1000);
        return;
    }
    if(d===undefined||d===null||
        d[0]===undefined|| d[1]===undefined|| d[2]===undefined)
    {
        setTimeout(Screenshot(), 1000);
        return;
    }
    $("#comp-camera").hide();
    $("#comp-main").show();
    var cdkey = d[0];//获取CD-KEY
    var platxt = d[1];//获取产品信息
    var secert=d[2];//获取密文
    $("#dialog1").addClass("show");
    $("#cdkey").append(cdkey);
    $("#information").append(platxt);
    $("#back").click(function(){
        $("#dialog1").removeClass("show");
        location.reload(false);
    });
    $("#ver").click(function(){
        var data = {"cdk": d[0], "info": md5(d[1]),"cpt":d[2]};
        $.ajax({
            type: "POST",
            url: "search2",
            data: data,
            success: function (data) {
                var json = JSON.parse(data);
                if (json.state === "1") {
                    AuthSucceed(platxt);
                }
                else {
                    AuthFailed();
                }

            },
            error: function () {
               $("#dialog2").addClass("show");
               $("#result").text("Failed Transmission");
               $("#ok").click(function(){
                        location.reload(false);
                    });
            }

        });

    });

}