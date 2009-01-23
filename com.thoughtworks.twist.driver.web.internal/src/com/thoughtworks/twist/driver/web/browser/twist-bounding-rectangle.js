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
		    var scroll = window.scrollX ? window.scrollX : document.body.scrollLeft;
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
		    var scroll = window.scrollY ? window.scrollY : document.body.scrollTop;
		    return top - scroll;
		};

	    return xPosition(element) + "," + yPosition(element) + "," + element.offsetWidth + "," + element.offsetHeight;
	};
}