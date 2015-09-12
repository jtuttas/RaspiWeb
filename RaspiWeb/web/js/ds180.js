var webSocket;

$(document).ready(function () {
    webSocket = new WebSocket("ws://"+serveradress+"/ds180");
    
    webSocket.onmessage = function (event) {
        var sensordata = JSON.parse(event.data)
        console.log("Websocket receive data:"+event.data);
        $("#tempValue").text(sensordata.temperature+" C");
        if (sensordata.temperature<sensordata.level0) {
            $("#tempValue").attr("class","below0 margin-base-vertical text-center");
        }
        else if (sensordata.temperature<sensordata.level1) {
            $("#tempValue").attr("class","below1 margin-base-vertical text-center");
            
        }
        else {
            $("#tempValue").attr("class","above1 margin-base-vertical text-center");

        }
    };
});

