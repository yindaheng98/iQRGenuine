function handleData(data)
{
    var d=null;
    try {
        d=JSON.parse(data);
    } catch(e) {
        if(run_cam) setTimeout(Screenshot(), 200);
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
        var data = {"cdk": d[0], "info": md5(d[1])};
        $.ajax({
            type: "POST",
            url: "search",
            data: data,
            success: function (data) {
                var json = JSON.parse(data);
                var private_key = json.public_key;//获取私钥
                 // Decrypt with the private key...
                var decrypt = new JSEncrypt();
                decrypt.setPrivateKey(private_key);
                var uncrypted = decrypt.decrypt(secert);
                // Now a simple check to see if the round-trip worked.
                if (uncrypted === cdkey) {
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