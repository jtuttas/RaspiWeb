var tempWebSocket;
var dimWebSocket;
// TODO Adresse anpassen
var serveradress = "localhost:8080/RaspiWeb";
//var serveradress = "service.joerg-tuttas.de:8081/Raspi";


/**
 * 
 * Aufbau der Websocket Verbindungen beim Laden der Seite
 */
$(document).ready( function() {
    if (tempWebSocket == undefined) {

        tempWebSocket = new WebSocket("ws://"+serveradress+"/temppoint");
        tempWebSocket.onmessage = function (event) {
           
            var jobj = jQuery.parseJSON(event.data );
            $('#h1temp').text("Temperatur " + jobj.temperature + " C");
            $('#tslider').val(jobj.temperature).slider('refresh');
            $('#h1pressure').text("Pressure " + jobj.pressure + " Pa");
            $('#pslider').val(jobj.pressure).slider('refresh');
            
            
        };
    }
    if (dimWebSocket == undefined) {

        dimWebSocket = new WebSocket("ws://" + serveradress + "/ledpoint");
        dimWebSocket.onmessage = function (event) {
            var dim = event.data;
            console.log("dimSocket receive ("+dim+")");

            $('#slider1').val(dim); 
            if ($('#slider1').is(":visible")) {
                $('#slider1').slider('refresh');
            }
            if (dim==0) {
                $('#toggle').val('0');
                if ($('#toggle').is(":visible")) {
                  $('#toggle').slider('refresh');
                }
            }
            else {
                $('#toggle').val('100');
                if ($('#toggle').is(":visible")) {
                    $('#toggle').slider('refresh');
                }
                
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

    
    
});

$(document).on('pagebeforeshow', '#page2', function () {
                $('#toggle').slider('refresh');
    });

$(document).on('pagebeforeshow', '#page3', function () {
    
    $('#slider1').slider('refresh');
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

    $('<input>').appendTo('#slider2').attr({'name': 'slider', 'id': 'tslider', 'data-highlight': 'true', 'min': '15', 'max': '35', 'value': '15', 'type': 'range'}).slider({
        create: function (event, ui) {
            $(this).parent().find('input').hide();
            $(this).parent().find('input').css('margin-left', '-9999px'); // Fix for some FF versions
            $(this).parent().find('.ui-slider-track').css('margin', '0 15px 0 15px');
            $(this).parent().find('.ui-slider-handle').hide();
        }
    }).slider("refresh");

    $('<input>').appendTo('#slider3').attr({'name': 'slider', 'id': 'pslider', 'data-highlight': 'true', 'min': '80000', 'max': '120000', 'value': '80000', 'type': 'range'}).slider({
        create: function (event, ui) {
            $(this).parent().find('input').hide();
            $(this).parent().find('input').css('margin-left', '-9999px'); // Fix for some FF versions
            $(this).parent().find('.ui-slider-track').css('margin', '0 15px 0 15px');
            $(this).parent().find('.ui-slider-handle').hide();
        }
    }).slider("refresh");


});

