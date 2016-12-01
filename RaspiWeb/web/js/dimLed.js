var dimSocket;

$(document).ready(function () {
    dimSocket = new WebSocket("ws://"+serveradress+"/ledpoint");
    
    dimSocket.onmessage = function (event) {
        var sensordata = JSON.parse(event.data)
        console.log("Dim Websocket receive data:"+event.data);
        $("#dimValue").text(event.data);
    };
});

