function handleFiles(f)//处理二维码图片
{
    for (var i = 0; i < f.length; i++)
    {
        var reader = new FileReader();

        reader.onload = (function ()
        {
            return function (e)
            {
                qrcode.decode(e.target.result);
            };
        })(f[i]);
        reader.readAsDataURL(f[i]);
    }
}

