$(document).ready(function () {
    $("#dim0").click(function () {
        var dimObject = {"dim": 0};
        post(dimObject,$(this));
        $(this).attr('disabled', true);
    });
    $("#dim33").click(function () {
        var dimObject = {"dim": 33};
        post(dimObject,$(this));
        $(this).attr('disabled', true);

    });
    $("#dim66").click(function () {
        var dimObject = {"dim": 66};
        post(dimObject,$(this));
        $(this).attr('disabled', true);

    });
    $("#dim100").click(function () {
        var dimObject = {"dim": 100};
        post(dimObject,$(this));
        $(this).attr('disabled', true);

    });
    $("#left90").click(function () {
        var stepObject={"step":1024,"dir":"left"};
        postStepper(stepObject,$(this));
        disableStepper();        
    });
    $("#left45").click(function () {
        var stepObject={"step":512,"dir":"left"};
        postStepper(stepObject,$(this));
        disableStepper();        
    });
    $("#right45").click(function () {
        var stepObject={"step":512,"dir":"right"};
        postStepper(stepObject,$(this));
        disableStepper();        
    });
    $("#right90").click(function () {
        var stepObject={"step":1024,"dir":"right"};
        postStepper(stepObject,$(this));
        disableStepper();        
    });
    $("#reset").click(function () {
        delStepper();
        disableStepper();        
    });
    
});

function disableStepper() {
    $("#left90").attr('disabled', true);
    $("#left45").attr('disabled', true);
    $("#reset").attr('disabled', true);
    $("#right45").attr('disabled', true);
    $("#right90").attr('disabled', true);
}

function enableStepper() {
    $("#left90").removeAttr('disabled');
    $("#left45").removeAttr('disabled');
    $("#reset").removeAttr('disabled');
    $("#right45").removeAttr('disabled');
    $("#right90").removeAttr('disabled');
}

function post(obj,elem) {
            $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/led",
            data: JSON.stringify(obj),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                elem.removeAttr('disabled');
            },
            failure: function (errMsg) {
                elem.removeAttr('disabled');
            }
        });

}

function postStepper(obj,elem) {
            $.ajax({
            type: "POST",
            url: "http://"+serveradress+"/api/v1/stepper",
            data: JSON.stringify(obj),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                enableStepper();
            },
            failure: function (errMsg) {
                enableStepper();
            }
        });
}
function delStepper() {
            $.ajax({
            type: "DELETE",
            url: "http://"+serveradress+"/api/v1/stepper",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                enableStepper();
            },
            failure: function (errMsg) {
                enableStepper();
            }
        });
}