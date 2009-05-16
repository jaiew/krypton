
if (!Twist) {
    var Twist = {};
}

if (!Twist.setTimeoutWaitStrategy) {
	Twist.setTimeoutMaxDelay = 0;
    
    Twist.setTimeoutWaitStrategy = function(){

        var setTimeoutFunction = window.setTimeout;
        Twist.realSetTimeout = function(){
            return Function.prototype.apply.call(setTimeoutFunction, window, arguments);
        };
        var setIntervalFunction = window.setInterval;
        Twist.realSetInterval = function(){
            return Function.prototype.apply.call(setIntervalFunction, window, arguments);
        };
        var clearTimeoutFunction = window.clearTimeout;
        Twist.realClearTimeout = function(){
            return Function.prototype.apply.call(clearTimeoutFunction, window, arguments);
        };
        var clearIntervalFunction = window.clearInterval;
        Twist.realClearInterval = function(){
            return Function.prototype.apply.call(clearIntervalFunction, window, arguments);
        };

        var inSetTimeout = false;

        window.setTimeout = function(){
            var target = arguments[0];
            var delay = arguments[1];
            var wrapped = function(){
                try {
					removeActiveSetTimeout(wrapped.timeoutID);
                    inSetTimeout = true;
                    if ("string" === typeof(target)) {
            			// Inspired by code by Andrea Giammarchi
            			// http://webreflection.blogspot.com/2007/08/global-scope-evaluation-and-dom.html
            			var head = document.getElementsByTagName("head")[0] || document.documentElement,
            				script = document.createElement("script");
            
            			script.type = "text/javascript";
            			if ( !window.ActiveXObject )
            				script.appendChild( document.createTextNode( target ) );
            			else
            				script.text = target;
            
            			// Use insertBefore instead of appendChild  to circumvent an IE6 bug.
            			// This arises when a base node is used (#2709).
            			head.insertBefore( script, head.firstChild );
            			head.removeChild( script );
                    }
                    else {
                        target.apply(this, arguments);
                    }
                } finally {
                    inSetTimeout = false;
                }
            };
            if (delay > 0 && delay < Twist.setTimeoutMaxDelay && !inSetTimeout) {
                arguments[0] = wrapped;
            }
            var timeoutID = Function.prototype.apply.call(Twist.realSetTimeout, this, arguments);
            if (wrapped === arguments[0]) {
	            wrapped.timeoutID = timeoutID;
				addActiveSetTimeout(timeoutID);
            }
            return timeoutID;
        };
        
        window.setInterval = function(){
            var target = arguments[0];
            var delay = arguments[1];
            var wrapped = function(){
                try {
                    inSetTimeout = true;
                    if ("string" === typeof(target)) {
                        eval(target);
                    }
                    else {
                        target.apply(this, arguments);
                    }
                } finally {
                    inSetTimeout = false;
                }
            };
            if (delay > 0 && delay < Twist.setTimeoutMaxDelay && !inSetTimeout) {
                arguments[0] = wrapped;
            }
            
            var timeoutID = Function.prototype.apply.call(Twist.realSetInterval, this, arguments);
            if (wrapped === arguments[0]) {
	            wrapped.timeoutID = timeoutID;
				addActiveSetTimeout(timeoutID);
                Twist.realSetTimeout(function() {
        			removeActiveSetTimeout(timeoutID);
                }, Twist.setTimeoutMaxDelay);
            }
            return timeoutID;
        };
        
        window.clearTimeout = function(timeoutID){
			removeActiveSetTimeout(timeoutID);
            Twist.realClearTimeout(timeoutID);
        };
        
        window.clearInterval = function(timeoutID){
			removeActiveSetTimeout(timeoutID);
            Twist.realClearInterval(timeoutID);
        };
    }();
}
