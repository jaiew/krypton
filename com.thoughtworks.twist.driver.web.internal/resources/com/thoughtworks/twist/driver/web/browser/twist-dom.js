
if (!Twist) {
    var Twist = {};
}

if (!Twist.dom) {
    function escapeXml(value){
        return (value + "").replace(/&/g, '&amp;').replace(/&amp;(\\w+);/g, '&$1;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }
    
    function attributeString(name, value){
        return " " + name + "=\"" + escapeXml(unescape(value)).replace(/"/g, '&quot;') + "\"";
    };
    
    function isWhitespaceOnly(string){
        return string.replace(/\s*/g, "").length == 0;
    };
    
    function textContent(element){
        var text = element.textContent;
		if (!text) {
			text = element.data;
		}
        if (!text) {
            return "";
        }
        return escapeXml(text);
    }
	
	Twist.domProperties = ["checked", "value", "selected", "disabled", "readOnly", "type", "id", "name", "className", "title", "style", "action", "href", "multiple", "alt", "src", "maxLength", "length", "rows", "size", "cols", "htmlFor", "width", "height", "caption", "colSpan", "rowSpan"];
    
    Twist.dom = function(element){
        if (element.nodeType === 8) {
            return "<!--" + textContent(element) + "-->";
        }
        
        if (element.nodeType === 3) {
            return textContent(element);
        }
        
        var tagName = element.tagName.toLowerCase();
        if (tagName.search(/\//g) != -1 || tagName.length === 0) {
            return;
        }
        var xml = "<" + tagName;
        
        for (var i = 0; i < Twist.domProperties.length; i++) {
            var property = Twist.domProperties[i];
			
			if (/^(?:action|href|src)$/.test(property)) {
                value = element.getAttribute(property);
			} else {
	            var value = element[property];
			}

            var typeOf = typeof(value);
            if (typeOf !== "undefined" && value !== null) {
                if (property === "className") {
                    property = "class";
                }
                if (property === "htmlFor") {
                    property = "for";
                }
                if (property === "style") {
                    if (element.style.cssText) {
                        value = element.style.cssText;
                    } else {
                        value = "";
                    }
                }
                if (typeOf === "object" && property === "name") {
                    value = element.attributes["name"].value;
                }

                value = value.toString();
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
                if (value !== "" || property === "value") {
                    xml += attributeString(property.toLowerCase(), value);
                }
            }
        }		
        
        xml += attributeString("Twist.domIndex", Twist.domIndex(element));
    
        if (element.hasChildNodes() || tagName === "script" || tagName === "link") {
            xml += ">";
            var needsCData = tagName === "script" || tagName === "noscript" || tagName === "style";
            if (needsCData) {
                xml += "<![CDATA[";
            }
            if (tagName === "textarea") {
                xml += element.value;
            }
            else {
                var child = element.firstChild;
                while (child != null) {
                	try {
	                    xml += Twist.dom(child);
                	} catch (dontForgetThisSwallowedException) {
                	}                    
                    child = child.nextSibling;
                }
            }
            if (needsCData) {
                xml += "]]>";
            }
            return xml + "</" + tagName + ">"
        }
        else {
            return xml += "/>";
        }
    };
    
    Twist.domIndex = function(element){
        var index = -1;
        while (element != null) {
            index++;
            element = element.previousSibling;
        }
        return index;
    };
}
