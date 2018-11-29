function handleData(d)
{
    d = JSON.parse(d);
    var data = {cdk: d[0], info: md5(d[1])};
    var secert=d[2];
    alert(JSON.stringify(data));
}