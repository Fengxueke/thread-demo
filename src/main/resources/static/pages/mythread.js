function openThread()
{
    $.ajax({
        url: "openThread",
        type: "GET",
        contentType: "application/json;charset=utf-8",
        success: function(data){
            console.log("--openThread-->" + data);
        }});


}

function getThreadInfo()
{
    var cmd_info = $("#cmd_info").val();
    $.ajax({
        url: "getThread",
        type: "GET",
        contentType: "application/json;charset=utf-8",
        success: function(data){
            cmd_info = cmd_info + data;
            $("#cmd_info").val(cmd_info);
            console.log("--getThread-->" + $("#cmd_info").val());
        }});

}

function getLog() {

    //websocket对象
    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/pages/thread/log");
    } else {
        console.error("不支持WebSocket");
    }

    //连接发生错误的回调方法
    websocket.onerror = function (e) {
        console.error("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        console.log("WebSocket连接成功")
    };

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        //追加
        if (event.data) {

            //日志内容
            var $loggingText = $("#deblock_udid");
            $loggingText.append(event.data + "\n");

            //是否开启自动底部
            if (window.loggingAutoBottom) {
                //滚动条自动到最底部
                $loggingText.scrollTop($loggingText[0].scrollHeight);
            }
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("WebSocket连接关闭")
    };

}

getLog()