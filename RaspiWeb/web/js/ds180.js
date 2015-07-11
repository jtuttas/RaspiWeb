var webSocket;

$(document).ready(function () {
    // TODO Adresse anpassen
    //webSocket = new WebSocket("ws://localhost:8080/RaspiWeb/ds180");
    webSocket = new WebSocket("ws://service.joerg-tuttas.de:8081/Raspi/ds180");
    
    webSocket.onmessage = function (event) {
        console.log("Websocket receive data:"+event.data);
        $("#tempValue").text(event.data+" °C");
    };
});

