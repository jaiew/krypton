
if (!Twist) {
    var Twist = {};
}

if (!Twist.dom) {
    Twist.isIE = window.ActiveXObject ? true : false;

    Twist.domFromInnerHTML = function(original){
		if (Twist.isIE) {
			var element = original;
		} else {
			var element = original.cloneNode(true);
		}

        Twist.walkDom(element, original);
		if (Twist.isIE) {
			return element.outerHTML;
		}
        var tagName = original.tagName;
        return "<" + tagName + ">" + element.innerHTML + "</" + tagName + ">";
    };
    
    Twist.walkDom = function(element, original){
        var index = 0;
        while (element != null) {
            Twist.updateAttributes(element, original, index);

            Twist.walkDom(element.firstChild, original.firstChild);
    
            if (element.nodeName.toLowerCase() === "textarea") {
				if (!Twist.isIE) {					
	                element.textContent = original.value;
				}
            }
            
            element = element.nextSibling;
            original = original.nextSibling;

			index++;
        }
    };
    
    Twist.propertiesOverAttributes = ["checked", "value", "selected", "multiple", "disabled", "readOnly"];

    Twist.updateAttributes = function(element, original, index){
        if (element.nodeType !== 1) {
            return;
        }
        element.setAttribute("twist.domindex", index);
		if (Twist.isIE && element === original) {
			return;
		}

        switch(element.tagName.toLowerCase()) {
            case "form":
            case "input":
            case "button":
            case "select":
            case "option":
            case "textarea":
            case "label":
                break;
            default:
                return;
        };

        for (var i = 0; i < Twist.propertiesOverAttributes.length; i++) {
            var property = Twist.propertiesOverAttributes[i];
            var value = original[property];

            var typeOf = typeof(value);
            if (typeOf === "undefined" || value === null) {
                continue;
            }
            
            if (value === "" && property === "value") {
                if (element.type === "reset") {
                    value = "Reset";
                }
                if (element.type === "submit") {
                    value = "Submit";
                }
                if (element.type === "checkbox" || element.type === "radio") {
                    value = "on";
                }
            }
			
			if (Twist.isIE) {
				element[property] = value;
			} else if (value !== "" || property === "value") {
                element.setAttribute(property, value.toString());
            }
        }
    };
}
