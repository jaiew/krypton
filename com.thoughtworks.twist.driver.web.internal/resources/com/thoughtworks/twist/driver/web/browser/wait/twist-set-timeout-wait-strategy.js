
if (!Twist) {
    var Twist = {};
}

if (!Twist.setTimeoutWaitStrategy) {
	Twist.setTimeoutMaxDelay = 0;
    
    Twist.setTimeoutWaitStrategy = function(){

        var realSetTimeout = window.setTimeout;
        var realClearTimeout = window.clearTimeout;
        var inSetTimeout = false;
        
        window.setTimeout = function(){
            var target = arguments[0];
            var delay = arguments[1];
            var wrapped = function(){
                try {
					removeActiveSetTimeout(wrapped.timeoutID);
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
            var timeoutID = Function.prototype.apply.call(realSetTimeout, this, arguments);
            if (wrapped === arguments[0]) {
	            wrapped.timeoutID = timeoutID;
				addActiveSetTimeout(timeoutID);
            }
            return timeoutID;
        };
        
        window.clearTimeout = function(timeoutID){
			removeActiveSetTimeout(timeoutID);
            realClearTimeout(timeoutID);
        };
    }();
}
