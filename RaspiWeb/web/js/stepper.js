var stepperSocket;

$(document).ready(function () {
    console.log("Connect to Stepper Websocket!");
    stepperSocket = new WebSocket("ws://" + serveradress + "/stepper");
    
    stepperSocket.onmessage = function (event) {
        var sensordata = JSON.parse(event.data)
        console.log("Stepper Websocket receive data:" + event.data);
        
        var data = JSON.parse(event.data);
        console.log("data="+JSON.stringify(data));
        if (data.moving != undefined) {
            if (data.moving) {
                $("#left90").attr('disabled', true);
                $("#left45").attr('disabled', true);
                $("#reset").attr('disabled', true);
                $("#right45").attr('disabled', true);
                $("#right90").attr('disabled', true);
            } else {
                $("#left90").removeAttr('disabled');
                $("#left45").removeAttr('disabled');
                $("#reset").removeAttr('disabled');
                $("#right45").removeAttr('disabled');
                $("#right90").removeAttr('disabled');
            }
        }
        if (data.position != undefined) {
            $("#stepperValue").text(data.position);
        }
        
    };
});

