function handleData(d)
{
	
    alert(d);
	var d = JSON.parse(d)
    var cdkey = d[0];//获取CD-KEY
    var secert=d[2];//获取密文
    var data = {"cdk": d[0], "info": md5(d[1])};
    $.ajax({
        type: "POST",
        url: "search",        
        data: data,      
        success: function (data, textStatus, jqXHR) {
        	var json = JSON.parse(data);
        	var private_key = json.public_key;//获取私钥
        	alert(private_key);
        	 
        	 // Decrypt with the private key...
            var decrypt1 = new JSEncrypt();
            decrypt1.setPrivateKey(private_key);
            var uncrypted = decrypt1.decrypt(secert);
            alert(uncrypted);
            // Now a simple check to see if the round-trip worked.
            if (uncrypted == cdkey) {
                alert("验证成功！");
            }
            else {
                alert("验证失败！");
            }
        
        },
        error: function () {
           alert("上传数据失败！");
        }
        
    });
}