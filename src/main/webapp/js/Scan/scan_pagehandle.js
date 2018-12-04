function goBack()
{
    $("#dialog1").hide();
    $("#dialog2").hide();
    $("#comp-yes").hide();
    $("#comp-no").hide();
    $("#comp-main").show();
}

function AuthFailed()
{
    $("#dialog1").hide();
    $("#dialog2").hide();
    $("#comp-yes").hide();
    $("#comp-no").show();
    $("#comp-main").hide();
}

function AuthSucceed(info)
{
    $("#dialog1").hide();
    $("#dialog2").hide();
    $("#comp-yes").show();
    $("#info").text(info);
    $("#comp-no").hide();
    $("#comp-main").hide();
}
