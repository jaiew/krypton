if (!Twist) {
	var Twist = {};
}

if (!Twist.normalizeFrame) {
	Twist.normalizeFrame = function(frame) {
		var domExpression = "";
		var parent = null;

		while (window != frame) {
			parent = frame.parent;
			for (var i = 0; i < parent.frames.length;  i++) {
				if (parent.frames[i] === frame) {
					domExpression = ".frames[" + i + "]" + domExpression;
					break;
				}
			}
			frame = parent;
		}
		return "window" + domExpression;
	};
}
