if (!Twist) {
	var Twist = {};
}

if (!Twist.isVisible) {
	Twist.isVisible = function(element) {
		function findEffectiveStyleProperty(element, property) {
		    var effectiveStyle = findEffectiveStyle(element);
		    var propertyValue = effectiveStyle[property];
		    if (propertyValue == 'inherit' && element.parentNode.style) {
		        return findEffectiveStyleProperty(element.parentNode, property);
		    }
		    return propertyValue;
		}

		function isDisplayed(element) {
		    var display = findEffectiveStyleProperty(element, "display");
		    if (display == "none") return false;
		    if (element.parentNode.style) {
		        return isDisplayed(element.parentNode);
		    }
		    return true;
		}

		function findEffectiveStyle(element) {
		    if (element.style == undefined) {
		        return undefined;
		    }
		    if (window.getComputedStyle) {
		        return window.getComputedStyle(element, null);
		    } else {
				return element.currentStyle;
		    }
		}

		if (element.tagName.toLowerCase() === "input") {
			if (element.type && element.type.toLowerCase() === "hidden") {
				return false;
			}
		}

	    var visibility = findEffectiveStyleProperty(element, "visibility");
	    return (visibility != "hidden" && isDisplayed(element));
	}
}