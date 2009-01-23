if (!Twist) {
	var Twist = {};
}

if (!Twist.cssQuery) {

	Twist.cssQuery = function() {
	
	var $COMMA = /\s*,\s*/;
	var cssQuery = function($selector, $$from) {
	try {
		var $match = [];
		var $useCache = arguments.callee.caching && !$$from;
		var $base = ($$from) ? ($$from.constructor == Array) ? $$from : [$$from] : [document];

		var $$selectors = parseSelector($selector).split($COMMA), i;
		for (i = 0; i < $$selectors.length; i++) {

			$selector = _toStream($$selectors[i]);

			if (isMSIE && $selector.slice(0, 3).join("") == " *#") {
				$selector = $selector.slice(2);
				$$from = _msie_selectById([], $base, $selector[1]);
			} else $$from = $base;

			var j = 0, $token, $filter, $arguments, $cacheSelector = "";
			while (j < $selector.length) {
				$token = $selector[j++];
				$filter = $selector[j++];
				$cacheSelector += $token + $filter;

				$arguments = "";
				if ($selector[j] == "(") {
					while ($selector[j++] != ")" && j < $selector.length) {
						$arguments += $selector[j];
					}
					$arguments = $arguments.slice(0, -1);
					$cacheSelector += "(" + $arguments + ")";
				}

				$$from = ($useCache && cache[$cacheSelector]) ?
					cache[$cacheSelector] : select($$from, $token, $filter, $arguments);
				if ($useCache) cache[$cacheSelector] = $$from;
			}
			$match = $match.concat($$from);
		}
		delete cssQuery.error;
		return $match;
	} catch ($error) {
		cssQuery.error = $error;
		return [];
	}};
	
	
	cssQuery.toString = function() {
		return "function cssQuery() {\n  [version " + version + "]\n}";
	};
	
	var cache = {};
	cssQuery.caching = false;
	cssQuery.clearCache = function($selector) {
		if ($selector) {
			$selector = _toStream($selector).join("");
			delete cache[$selector];
		} else cache = {};
	};
	
	var modules = {};
	var loaded = false;
	cssQuery.addModule = function($name, $script) {
		if (loaded) eval("$script=" + String($script));
		modules[$name] = new $script();;
	};
	
	cssQuery.valueOf = function($code) {
		return $code ? eval($code) : this;
	};
	
	var selectors = {};
	var pseudoClasses = {};

	var AttributeSelector = {match: /\[([\w-]+(\|[\w-]+)?)\s*(\W?=)?\s*([^\]]*)\]/};
	var attributeSelectors = [];
	
	selectors[" "] = function($results, $from, $tagName, $namespace) {
		var $element, i, j;
		for (i = 0; i < $from.length; i++) {
			var $subset = getElementsByTagName($from[i], $tagName, $namespace);
			for (j = 0; ($element = $subset[j]); j++) {
				if (thisElement($element) && compareNamespace($element, $namespace))
					$results.push($element);
			}
		}
	};

	selectors["#"] = function($results, $from, $id) {

		var $element, j;
		for (j = 0; ($element = $from[j]); j++) if ($element.id == $id) $results.push($element);
	};
	
	selectors["."] = function($results, $from, $className) {
		$className = new RegExp("(^|\\s)" + $className + "(\\s|$)");

		var $element, i;
		for (i = 0; ($element = $from[i]); i++)
			if ($className.test($element.className)) $results.push($element);
	};
	
	selectors[":"] = function($results, $from, $pseudoClass, $arguments) {
		var $test = pseudoClasses[$pseudoClass], $element, i;
		if ($test) for (i = 0; ($element = $from[i]); i++)
			if ($test($element, $arguments)) $results.push($element);
	};
	
	pseudoClasses["link"] = function($element) {
		var $document = getDocument($element);
		if ($document.links) for (var i = 0; i < $document.links.length; i++) {
			if ($document.links[i] == $element) return true;
		}
	};
	
	pseudoClasses["visited"] = function($element) {
	};
	
	var thisElement = function($element) {
		return ($element && $element.nodeType == 1 && $element.tagName != "!") ? $element : null;
	};
	
	var previousElementSibling = function($element) {
		while ($element && ($element = $element.previousSibling) && !thisElement($element)) continue;
		return $element;
	};
	
	var nextElementSibling = function($element) {
		while ($element && ($element = $element.nextSibling) && !thisElement($element)) continue;
		return $element;
	};
	
	var firstElementChild = function($element) {
		return thisElement($element.firstChild) || nextElementSibling($element.firstChild);
	};
	
	var lastElementChild = function($element) {
		return thisElement($element.lastChild) || previousElementSibling($element.lastChild);
	};
	
	var childElements = function($element) {
		var $childElements = [];
		$element = firstElementChild($element);
		while ($element) {
			$childElements.push($element);
			$element = nextElementSibling($element);
		}
		return $childElements;
	};
	
	var isMSIE = true;
	
	var isXML = function($element) {
		var $document = getDocument($element);
		return (typeof $document.mimeType == "unknown") ?
			/\.xml$/i.test($document.URL) :
			Boolean($document.mimeType == "XML Document");
	};
	
	var getDocument = function($element) {
		return $element.ownerDocument || $element.document;
	};
	
	var getElementsByTagName = function($element, $tagName) {
		return ($tagName == "*" && $element.all) ? $element.all : $element.getElementsByTagName($tagName);
	};
	
	var compareTagName = function($element, $tagName, $namespace) {
		if ($tagName == "*") return thisElement($element);
		if (!compareNamespace($element, $namespace)) return false;
		if (!isXML($element)) $tagName = $tagName.toUpperCase();
		return $element.tagName == $tagName;
	};
	
	var compareNamespace = function($element, $namespace) {
		return !$namespace || ($namespace == "*") || ($element.scopeName == $namespace);
	};
	
	var getTextContent = function($element) {
		return $element.innerText;
	};
	
	function _msie_selectById($results, $from, id) {
		var $match, i, j;
		for (i = 0; i < $from.length; i++) {
			if ($match = $from[i].all.item(id)) {
				if ($match.id == id) $results.push($match);
				else if ($match.length != null) {
					for (j = 0; j < $match.length; j++) {
						if ($match[j].id == id) $results.push($match[j]);
					}
				}
			}
		}
		return $results;
	};
	
	if (![].push) Array.prototype.push = function() {
		for (var i = 0; i < arguments.length; i++) {
			this[this.length] = arguments[i];
		}
		return this.length;
	};
	
	var $NAMESPACE = /\|/;
	function select($$from, $token, $filter, $arguments) {
		if ($NAMESPACE.test($filter)) {
			$filter = $filter.split($NAMESPACE);
			$arguments = $filter[0];
			$filter = $filter[1];
		}
		var $results = [];
		if (selectors[$token]) {
			selectors[$token]($results, $$from, $filter, $arguments);
		}
		return $results;
	};
	
	var $STANDARD_SELECT = /^[^\s>+~]/;
	var $$STREAM = /[\s#.:>+~()@]|[^\s#.:>+~()@]+/g;
	function _toStream($selector) {
		if ($STANDARD_SELECT.test($selector)) $selector = " " + $selector;
		return $selector.match($$STREAM) || [];
	};
	
	var $WHITESPACE = /\s*([\s>+~(),]|^|$)\s*/g;
	var $IMPLIED_ALL = /([\s>+~,]|[^(]\+|^)([#.:@])/g;
	var parseSelector = function($selector) {
		return $selector
		.replace($WHITESPACE, "$1")
		.replace($IMPLIED_ALL, "$1*$2");
	};
	
	var Quote = {
		toString: function() {return "'"},
		match: /^('[^']*')|("[^"]*")$/,
		test: function($string) {
			return this.match.test($string);
		},
		add: function($string) {
			return this.test($string) ? $string : this + $string + this;
		},
		remove: function($string) {
			return this.test($string) ? $string.slice(1, -1) : $string;
		}
	};
	
	var getText = function($text) {
		return Quote.remove($text);
	};
	
	var $ESCAPE = /([\/()[\]?{}|*+-])/g;
	function regEscape($string) {
		return $string.replace($ESCAPE, "\\$1");
	};
	
	loaded = true;
	
	return cssQuery;
	
	}();
}