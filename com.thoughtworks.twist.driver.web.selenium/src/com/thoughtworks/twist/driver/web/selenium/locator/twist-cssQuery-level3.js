Twist.cssQuery.addModule("css-level3", function() {

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

});
