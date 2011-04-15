if (!Twist) {
	var Twist = {};
}

if (!Twist.cssQuery) {

	Twist.cssQuery = function() {

    var isMSIE = window.ActiveXObject ? true : false;
	
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
		if (loaded) cssQuery.globalEval("$script=" + String($script));
		modules[$name] = new $script();;
	};
	
	cssQuery.valueOf = function($code) {
		return $code ? eval($code) : this;
	};
    
    cssQuery.globalEval = function($code) {
		// Inspired by code by Andrea Giammarchi
		// http://webreflection.blogspot.com/2007/08/global-scope-evaluation-and-dom.html
		var head = document.getElementsByTagName("head")[0] || document.documentElement,
			script = document.createElement("script");

		script.type = "text/javascript";
		if ( !window.ActiveXObject )
			script.appendChild( document.createTextNode( $code ) );
		else
			script.text = $code;

		// Use insertBefore instead of appendChild  to circumvent an IE6 bug.
		// This arises when a base node is used (#2709).
		head.insertBefore( script, head.firstChild );
		head.removeChild( script );        
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
	
//	var isMSIE = true;
	
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
    
    // Standard
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

    // Level 2    
    selectors[">"] = function($results, $from, $tagName, $namespace) {
    	var $element, i, j;
    	for (i = 0; i < $from.length; i++) {
    		var $subset = childElements($from[i]);
    		for (j = 0; ($element = $subset[j]); j++)
    			if (compareTagName($element, $tagName, $namespace))
    				$results.push($element);
    	}
    };
    
    selectors["+"] = function($results, $from, $tagName, $namespace) {
    	for (var i = 0; i < $from.length; i++) {
    		var $element = nextElementSibling($from[i]);
    		if ($element && compareTagName($element, $tagName, $namespace))
    			$results.push($element);
    	}
    };
    
    selectors["@"] = function($results, $from, $attributeSelectorID) {
    	var $test = attributeSelectors[$attributeSelectorID].test;
    	var $element, i;
    	for (i = 0; ($element = $from[i]); i++)
    		if ($test($element)) $results.push($element);
    };
    
    pseudoClasses["first-child"] = function($element) {
    	return !previousElementSibling($element);
    };
    
    pseudoClasses["lang"] = function($element, $code) {
    	$code = new RegExp("^" + $code, "i");
    	while ($element && !$element.getAttribute("lang")) $element = $element.parentNode;
    	return $element && $code.test($element.getAttribute("lang"));
    };
    
    AttributeSelector.NS_IE = /\\:/g;
    AttributeSelector.PREFIX = "@";
    
    AttributeSelector.tests = {};
    
    AttributeSelector.replace = function($match, $attribute, $namespace, $compare, $value) {
    	var $key = this.PREFIX + $match;
    	if (!attributeSelectors[$key]) {
    		$attribute = this.create($attribute, $compare || "", $value || "");
    		attributeSelectors[$key] = $attribute;
    		attributeSelectors.push($attribute);
    	}
    	return attributeSelectors[$key].id;
    };
    AttributeSelector.parse = function($selector) {
    	$selector = $selector.replace(this.NS_IE, "|");
    	var $match;
    	while ($match = $selector.match(this.match)) {
    		var $replace = this.replace($match[0], $match[1], $match[2], $match[3], $match[4]);
    		$selector = $selector.replace(this.match, $replace);
    	}
    	return $selector;
    };
    AttributeSelector.create = function($propertyName, $test, $value) {
    	var $attributeSelector = {};
    	$attributeSelector.id = this.PREFIX + attributeSelectors.length;
    	$attributeSelector.name = $propertyName;
    	$test = this.tests[$test];
    	$test = $test ? $test(this.getAttribute($propertyName), getText($value)) : false;
    	$attributeSelector.test = new Function("e", "return " + $test);
    	return $attributeSelector;
    };
    AttributeSelector.getAttribute = function($name) {
    	switch ($name.toLowerCase()) {
    		case "id":
    			return "e.id";
    		case "class":
    			return "e.className";
    		case "for":
    			return "e.htmlFor";
    		case "href":
    			if (isMSIE) {
    				return "String((e.outerHTML.match(/href=\\x22?([^\\s\\x22]*)\\x22?/)||[])[1]||'')";
    			}
    	}
    	return "e.getAttribute('" + $name.replace($NAMESPACE, ":") + "')";
    };
    
    AttributeSelector.tests[""] = function($attribute) {
    	return $attribute;
    };
    
    AttributeSelector.tests["="] = function($attribute, $value) {
    	return $attribute + "==" + Quote.add($value);
    };
    
    AttributeSelector.tests["~="] = function($attribute, $value) {
    	return "/(^| )" + regEscape($value) + "( |$)/.test(" + $attribute + ")";
    };
    
    AttributeSelector.tests["|="] = function($attribute, $value) {
    	return "/^" + regEscape($value) + "(-|$)/.test(" + $attribute + ")";
    };
    
    var _parseSelector = parseSelector;
    parseSelector = function($selector) {
    	return _parseSelector(AttributeSelector.parse($selector));
    };

    // Level 3
    selectors["~"] = function($results, $from, $tagName, $namespace) {
    	var $element, i;
    	for (i = 0; ($element = $from[i]); i++) {
    		while ($element = nextElementSibling($element)) {
    			if (compareTagName($element, $tagName, $namespace))
    				$results.push($element);
    		}
    	}
    };
    
    pseudoClasses["contains"] = function($element, $text) {
    	$text = new RegExp(regEscape(getText($text)));
    	return $text.test(getTextContent($element));
    };
    
    pseudoClasses["root"] = function($element) {
    	return $element == getDocument($element).documentElement;
    };
    
    pseudoClasses["empty"] = function($element) {
    	var $node, i;
    	for (i = 0; ($node = $element.childNodes[i]); i++) {
    		if (thisElement($node) || $node.nodeType == 3) return false;
    	}
    	return true;
    };
    
    pseudoClasses["last-child"] = function($element) {
    	return !nextElementSibling($element);
    };
    
    pseudoClasses["only-child"] = function($element) {
    	$element = $element.parentNode;
    	return firstElementChild($element) == lastElementChild($element);
    };
    
    pseudoClasses["not"] = function($element, $selector) {
    	var $negated = cssQuery($selector, getDocument($element));
    	for (var i = 0; i < $negated.length; i++) {
    		if ($negated[i] == $element) return false;
    	}
    	return true;
    };
    
    pseudoClasses["nth-child"] = function($element, $arguments) {
    	return nthChild($element, $arguments, previousElementSibling);
    };
    
    pseudoClasses["nth-last-child"] = function($element, $arguments) {
    	return nthChild($element, $arguments, nextElementSibling);
    };
    
    pseudoClasses["target"] = function($element) {
    	return $element.id == location.hash.slice(1);
    };
    
    pseudoClasses["checked"] = function($element) {
    	return $element.checked;
    };
    
    pseudoClasses["enabled"] = function($element) {
    	return $element.disabled === false;
    };
    
    pseudoClasses["disabled"] = function($element) {
    	return $element.disabled;
    };
    
    pseudoClasses["indeterminate"] = function($element) {
    	return $element.indeterminate;
    };
    
    AttributeSelector.tests["^="] = function($attribute, $value) {
    	return "/^" + regEscape($value) + "/.test(" + $attribute + ")";
    };
    
    AttributeSelector.tests["$="] = function($attribute, $value) {
    	return "/" + regEscape($value) + "$/.test(" + $attribute + ")";
    };
    
    AttributeSelector.tests["*="] = function($attribute, $value) {
    	return "/" + regEscape($value) + "/.test(" + $attribute + ")";
    };
    
    function nthChild($element, $arguments, $traverse) {
    	switch ($arguments) {
    		case "n": return true;
    		case "even": $arguments = "2n"; break;
    		case "odd": $arguments = "2n+1";
    	}
    
    	var $$children = childElements($element.parentNode);
    	function _checkIndex($index) {
    		var $index = ($traverse == nextElementSibling) ? $$children.length - $index : $index - 1;
    		return $$children[$index] == $element;
    	};
    
    	if (!isNaN($arguments)) return _checkIndex($arguments);
    
    	$arguments = $arguments.split("n");
    	var $multiplier = parseInt($arguments[0]);
    	var $step = parseInt($arguments[1]);
    
    	if ((isNaN($multiplier) || $multiplier == 1) && $step == 0) return true;
    	if ($multiplier == 0 && !isNaN($step)) return _checkIndex($step);
    	if (isNaN($step)) $step = 0;
    
    	var $count = 1;
    	while ($element = $traverse($element)) $count++;
    
    	if (isNaN($multiplier) || $multiplier == 1)
    		return ($traverse == nextElementSibling) ? ($count <= $step) : ($step >= $count);
    
    	return ($count % $multiplier) == $step;
    };


	loaded = true;
	
	return cssQuery;
	
	}();
}