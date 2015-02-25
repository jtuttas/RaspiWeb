var tempWebSocket;
var dimWebSocket;
var serveradress = "localhost:8080";

/**
 * 
 * Aufbau der Websocket Verbindungen beim Laden der Seite
 */
$(document).ready( function() {
    if (tempWebSocket == undefined) {

        tempWebSocket = new WebSocket("ws://"+serveradress+"/RaspiWeb/temppoint");
        tempWebSocket.onmessage = function (event) {
            var temp = event.data;
            //console.log("tempSocket receive ("+temp+")");
            $('#h1temp').text("Temperatur " + temp + " C");
            $('#tslider').val(temp).slider('refresh');
        };
    }
    if (dimWebSocket == undefined) {

        dimWebSocket = new WebSocket("ws://" + serveradress + "/RaspiWeb/ledpoint");
        dimWebSocket.onmessage = function (event) {
            var dim = event.data;
            console.log("dimSocket receive ("+dim+")");

            $('#slider1').val(dim).slider('refresh');
            if (dim==0) {
                $('#toggle').val('0').slider('refresh');
            }
            else {
                $('#toggle').val('100').slider('refresh');
                
            }
        };
    }
    $('#toggle').change(function () {
       if (dimWebSocket != undefined) {
           dimWebSocket.send($('#toggle').val());
           console.log("send via Websocket "+$('#toggle').val());
       } 
       else {
           console.log("Keine Websocket Verbindung");
       }
    });
    $('#fieldslider1').change(function() {
       if (dimWebSocket != undefined) {
           dimWebSocket.send($('#slider1').val());
           console.log("send via Websocket "+$('#slider1').val());
       } 
       else {
           console.log("Keine Websocket Verbindung");
       }
        
        
    });

    
    
});

$(document).on('pagebeforeshow', '#page4', function () {
    $('#slider2').empty();

    $('<input>').appendTo('#slider2').attr({'name': 'slider', 'id': 'tslider', 'data-highlight': 'true', 'min': '0', 'max': '100', 'value': '0', 'type': 'range'}).slider({
        create: function (event, ui) {
            $(this).parent().find('input').hide();
            $(this).parent().find('input').css('margin-left', '-9999px'); // Fix for some FF versions
            $(this).parent().find('.ui-slider-track').css('margin', '0 15px 0 15px');
            $(this).parent().find('.ui-slider-handle').hide();
        }
    }).slider("refresh");



});

