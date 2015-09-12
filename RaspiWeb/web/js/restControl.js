$(document).ready(function () {
    $("#dim0").click(function () {
        var dimObject = {"dim": 0};
        $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/led",
            data: JSON.stringify(dimObject),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                //alert("sucess:"+data);
            },
            failure: function (errMsg) {
                //alert("error:"+errMsg);
            }
        });
    });
    $("#dim33").click(function () {
        var dimObject = {"dim": 33};
        $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/led",
            data: JSON.stringify(dimObject),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                //alert("sucess:"+data);
            },
            failure: function (errMsg) {
                //alert("error:"+errMsg);
            }
        });

    });
    $("#dim66").click(function () {
        var dimObject = {"dim": 66};
        $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/led",
            data: JSON.stringify(dimObject),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                //alert("sucess:"+data);
            },
            failure: function (errMsg) {
                //alert("error:"+errMsg);
            }
        });

    });
    $("#dim100").click(function () {
        var dimObject = {"dim": 100};
        $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/led",
            data: JSON.stringify(dimObject),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                //alert("sucess:"+data);
            },
            failure: function (errMsg) {
                //alert("error:"+errMsg);
            }
        });

    });
});