function goBack()
{
    $("#comp-camera").hide();
    $("#comp-yes").hide();
    $("#comp-no").hide();
    $("#comp-main").show();
    location.reload(false);
}

function AuthFailed()
{
    $("#comp-camera").hide();
    $("#comp-yes").hide();
    $("#comp-no").show();
    $("#comp-main").hide();
}

function AuthSucceed(info)
{
    $("#comp-camera").hide();
    $("#comp-yes").show();
    $("#info").text(info);
    $("#comp-no").hide();
    $("#comp-main").hide();
}
