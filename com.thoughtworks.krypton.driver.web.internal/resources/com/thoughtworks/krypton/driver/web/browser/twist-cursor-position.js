
if (!Twist) {
    var Twist = {};
}

Twist.getCursorPosition = function(element){
    if (document.selection) {
        var selectRange = document.selection.createRange().duplicate();
        var elementRange = element.createTextRange();
        selectRange.move("character", 0);
        elementRange.move("character", 0);
        var inRange1 = selectRange.inRange(elementRange);
        var inRange2 = elementRange.inRange(selectRange);
        elementRange.setEndPoint("EndToEnd", selectRange);
        return String(elementRange.text).replace(/\r/g, "").length;
    }
    else {
        return element.selectionStart;
    }
};

Twist.setCursorPosition = function(element, position){
    if (position === -1) {
        position = element.value.length;
    }
    
    if (element.setSelectionRange) {
        element.setSelectionRange(position, position);
    }
    else {
        var range = element.createTextRange();
        range.collapse(true);
        range.moveEnd('character', position);
        range.moveStart('character', position);
        range.select();
    }
};
