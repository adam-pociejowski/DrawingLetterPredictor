var canvas;

$(function () {
    canvas = window._canvas = new fabric.Canvas('canvas');
    initCanvas();

    $('#saveButton').click(function () {
        var letterInput = document.getElementById('letterInput');
        var letter = letterInput.value;
        var formData = new FormData();
        formData.append("file", getFileFromCanvas());
        formData.append("letter", letter);
        clearCanvas();

        $.ajax({
            type: 'POST',
            url: 'letter/save',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                console.log('success', data);
            },
            error: function (error) {
                console.log('error', error);
            }
        });
    });

    $('#clearButton').click(function () {
        clearCanvas();
    });
});

var clearCanvas = function() {
    canvas.clear();
    initCanvas();
};

var initCanvas = function () {
    canvas.backgroundColor = '#efefef';
    canvas.isDrawingMode= 1;
    canvas.freeDrawingBrush.color = "black";
    canvas.freeDrawingBrush.width = 1;
    canvas.setHeight(20);
    canvas.setWidth(20);
    canvas.renderAll();
};

var getFileFromCanvas = function () {
    var canvas = document.getElementById('canvas');
    console.log(canvas.width);
    var url = canvas.toDataURL();
    var blobBin = atob(url.split(',')[1]);
    var array = [];
    for(var i = 0; i < blobBin.length; i++) {
        array.push(blobBin.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], {type: 'image/png'});
};