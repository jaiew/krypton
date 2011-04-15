if (!Twist) {
    var Twist = {};
}

if (!Twist.fireEvent) {
    Twist.fireEvent = function(element, type){
        if (element.fireEvent) {
            var event = element.ownerDocument.createEventObject();
            element.fireEvent("on" + type, event);
        }
        else {
            var event = document.createEvent('HTMLEvents');
            event.initEvent(type, true, true);
            element.dispatchEvent(event);
        }
    };
}
