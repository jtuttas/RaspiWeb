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
});

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