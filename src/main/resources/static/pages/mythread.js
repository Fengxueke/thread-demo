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