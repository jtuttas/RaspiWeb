var webSocket;

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
    
    if (webSocket == undefined) {
        //webSocket = new WebSocket("ws://localhost:8080/Raspi/temppoint");
        
       webSocket = new WebSocket("ws://service.joerg-tuttas.de:8081/Raspi/temppoint");
        webSocket.onmessage = function(event){
                       var temp = event.data;

                       $('#h1temp').text("Temperatur "+temp+" C");
                       $('#tslider').val(temp).slider('refresh');
                   };
       }


});


$(document).on('pagebeforeshow', '#page3', function () {
    $('#slider1-range').change(function() {
        var slider_value = $("#slider1-range").val();
        $('#glowValue').val(slider_value);
        $('#glowValue').trigger("keydown");
        
    });
});

