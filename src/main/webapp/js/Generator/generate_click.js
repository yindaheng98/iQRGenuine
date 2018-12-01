function create_qrcode(text, errorCorrectionLevel, mode, mb)
{
    qrcode.stringToBytes = qrcode.stringToBytesFuncs[mb];
    //"0"表示自动判断要生成的QRCode的大小
    var qr = qrcode("0", errorCorrectionLevel || 'M');
    qr.addData(text, mode);
    qr.make();
    return qr.createImgTag();
}

function update_qrcode(text)
{
    var form = document.forms['qrForm'];
    var e = form.elements['e'].value;
    var m = form.elements['m'].value;
    var mb = form.elements['mb'].value;
    document.getElementById('qr').innerHTML = create_qrcode(text, e, m, mb);
}

function generate_click()
{
    var info = $('#info').val().replace(/^[\s\u3000]+|[\s\u3000]+$/g, '');
    var md5_info = md5(info);
    $.post("generate", {md5_info: md5_info}, function (data)
    {
        data = JSON.parse(data);
        var qr_text = JSON.stringify([data.cdkey, info, data.cipher]);
        update_qrcode(qr_text);
    });
}
