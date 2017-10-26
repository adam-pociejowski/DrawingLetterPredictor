var context;

$(function () {
    context = document.getElementById('canvas').getContext("2d");
});

$('#canvas').mousedown(function(e){
    var mouseX = e.pageX - this.offsetLeft;
    var mouseY = e.pageY - this.offsetTop;
    paint = true;
    addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
    redraw();
});

$('#canvas').mousemove(function(e){
    if(paint){
        addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
        redraw();
    }
});

$('#canvas').mouseup(function(e){
    paint = false;
});

$('#canvas').mouseleave(function(e){
    paint = false;
});

var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var paint;

function addClick(x, y, dragging) {
    clickX.push(x);
    clickY.push(y);
    clickDrag.push(dragging);
}

function redraw(){
    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    context.strokeStyle = "#000000";
    context.lineJoin = "round";
    context.lineWidth = 1;

    for(var i=0; i < clickX.length; i++) {
        context.beginPath();
        if(clickDrag[i] && i){
            context.moveTo(clickX[i-1], clickY[i-1]);
        }else{
            context.moveTo(clickX[i]-1, clickY[i]);
        }
        context.lineTo(clickX[i], clickY[i]);
        context.closePath();
        context.stroke();
    }
}

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
        success: function (data) {},
        error: function (error) {
            console.error('error', error);
        }
    });
});

$('#clearButton').click(function () {
    clearCanvas();
});

var clearCanvas = function() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    clickX = new Array();
    clickY = new Array();
    clickDrag = new Array();
    redraw();
};

var getFileFromCanvas = function () {
    var canvas = document.getElementById('canvas');
    var url = canvas.toDataURL();
    var blobBin = atob(url.split(',')[1]);
    var array = [];
    for(var i = 0; i < blobBin.length; i++) {
        array.push(blobBin.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], {type: 'image/png'});
};