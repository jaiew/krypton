
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
        var text = element.textContent ? element.textContent : element.data;
        if (!text) {
            return "";
        }
        return escapeXml(text);
    }
    
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
        
        var attributes = {};
        
        var properties = ["checked", "value", "selected", "disabled", "readOnly", "type", "id", "name", "className", "title", "style", "action", "href", "multiple", "alt", "src", "maxLength", "length", "rows", "size", "cols", "htmlFor", "width", "height", "caption", "colSpan", "rowSpan"];
        for (var i = 0; i < properties.length; i++) {
            var property = properties[i];
            var value = element[property];
            if (property === "action") {
                value = element.getAttribute("action");
            }
            if (property === "href") {
                value = element.getAttribute("href");
            }
            if (property === "src") {
                value = element.getAttribute("src");
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
                    }
                    else {
                        value = "";
                    }
                }
                if (typeOf === "object") {
                    if (property === "name") {
                        value = element.attributes["name"].value;
                    }
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
                var childNodes = element.childNodes;
                var child = null;
                for (var i = 0; child = childNodes[i++];) {
                    xml += Twist.dom(child);
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
        var parent = element.parentNode;
        for (var i = 0; i < parent.childNodes.length; i++) {
            if (parent.childNodes[i] === element) {
                return i;
            }
        }
        return -1;
    };
}
