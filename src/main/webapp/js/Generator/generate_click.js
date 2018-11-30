function generate_click(info)
{
    var md5_info = md5(info);
    $.post("generate", {md5_info: md5_info}, function (data)
    {
        data = JSON.parse(data);
        var qr_data = JSON.stringify([data.cdkey, info, data.cipher]);
        $('#qrcode').html("").qrcode(qr_data);
    });
}