if (!Twist) {
	var Twist = {};
}

if (!Twist.boundingRectangle) {
	Twist.boundingRectangle = function(element) {
		function xPosition(element) {
		    var left = 0;
	        while (true) {
				if (element.offsetLeft) {
		            left += element.offsetLeft;
				}
				if (element.offsetParent) {
					element = element.offsetParent;
				} else if (element.ownerDocument.parentWindow && element.ownerDocument.parentWindow.frameElement) {
					element = element.ownerDocument.parentWindow.frameElement;					
				} else if (element.ownerDocument.defaultView && element.ownerDocument.defaultView.frameElement) {
					element = element.ownerDocument.defaultView.frameElement;

					if (element.offsetLeft == 0 && (element.parentNode.rows === "100%" || element.parentNode.rows === "*")) {
						var parentFrames = element.parentNode.ownerDocument.defaultView.frames;
						for (var i = 0; i < parentFrames.length; i++) {
							if (parentFrames[i].frameElement === element) {
								break;
							}
							left += parentFrames[i].frameElement.offsetWidth;
						}
					}
				} else if (element.x) {	
			        left += element.x;
					break;
				} else {
					break;
				}
	        }
		    var scroll = window.scrollX ? window.scrollX : document.documentElement.scrollLeft;
		    return left - scroll;
		};

		function yPosition(element) {
		    var top = 0;
	        while (true) {
				if (element.offsetTop) {
		            top += element.offsetTop;						
				}
				if (element.offsetParent) {
					element = element.offsetParent;
				} else if (element.ownerDocument.parentWindow && element.ownerDocument.parentWindow.frameElement) {
					element = element.ownerDocument.parentWindow.frameElement;					
				} else if (element.ownerDocument.defaultView && element.ownerDocument.defaultView.frameElement) {
					element = element.ownerDocument.defaultView.frameElement;

					if (element.offsetTop == 0 && (element.parentNode.cols === "100%" || element.parentNode.cols === "*")) {
						var parentFrames = element.parentNode.ownerDocument.defaultView.frames;
						for (var i = 0; i < parentFrames.length; i++) {							
							if (parentFrames[i].frameElement == element) {
								break;
							}
							top += parentFrames[i].frameElement.offsetHeight;
						}
					}
				} else if (element.y) {
			        top += element.y;
					break;
				} else {
					break;
				}
			}
		    var scroll = window.scrollY ? window.scrollY : document.documentElement.scrollTop;
		    return top - scroll;
		};

        if (window.ActiveXObject) {
            var x = 0, y = 0;
            
            var e = element;
            while (true) {
                var box = e.getBoundingClientRect();
                x += box.left;
                y += box.top;
                
                if (e.ownerDocument.parentWindow && e.ownerDocument.parentWindow.frameElement) {
                    e = e.ownerDocument.parentWindow.frameElement;
                } else if (e.ownerDocument.defaultView && e.ownerDocument.defaultView.frameElement) {
                    e = e.ownerDocument.defaultView.frameElement;
                } else {
                    break;
                }
            }
			x -= document.documentElement.scrollLeft;
			y -= document.documentElement.scrollTop;
	        
			return x + "," + y + "," + element.offsetWidth + "," + element.offsetHeight;
        } else {
		    return xPosition(element) + "," + yPosition(element) + "," + element.offsetWidth + "," + element.offsetHeight;
		}
	};
}
