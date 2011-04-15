Twist.cssQuery.addModule("css-standard", function() {

isMSIE = eval("false;/*@cc_on@if(@\x5fwin32)isMSIE=true@end@*/");

if (!isMSIE) {
	getElementsByTagName = function($element, $tagName, $namespace) {
		return $namespace ? $element.getElementsByTagNameNS("*", $tagName) :
			$element.getElementsByTagName($tagName);
	};

	compareNamespace = function($element, $namespace) {
		return !$namespace || ($namespace == "*") || ($element.prefix == $namespace);
	};

	isXML = document.contentType ? function($element) {
		return /xml/i.test(getDocument($element).contentType);
	} : function($element) {
		return getDocument($element).documentElement.tagName != "HTML";
	};

	getTextContent = function($element) {
		return $element.textContent || $element.innerText || _getTextContent($element);
	};

	function _getTextContent($element) {
		var $textContent = "", $node, i;
		for (i = 0; ($node = $element.childNodes[i]); i++) {
			switch ($node.nodeType) {
				case 11:
				case 1: $textContent += _getTextContent($node); break;
				case 3: $textContent += $node.nodeValue; break;
			}
		}
		return $textContent;
	};
}
});
