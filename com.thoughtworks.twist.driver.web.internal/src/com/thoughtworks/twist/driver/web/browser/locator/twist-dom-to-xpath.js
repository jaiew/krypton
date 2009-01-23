if (!Twist) {
	var Twist = {};
}

if (!Twist.domToXPath) {
	Twist.domToXPath = function(element) {
		if (element.frameElement) {
			element = element.frameElement;
		}
		var xpath = "";
		var parent = null;

		while (parent = element.parentNode) {
			var index = 0;
			for (var i = 0; i < parent.childNodes.length;  i++) {
				if (parent.childNodes[i].nodeType === 1) {
					index++;
				}
				if (parent.childNodes[i] === element) {
					xpath = "/*[" + index + "]" + xpath;
					break;
				}
			}
			element = parent;
		}
		return xpath;
	};
}
