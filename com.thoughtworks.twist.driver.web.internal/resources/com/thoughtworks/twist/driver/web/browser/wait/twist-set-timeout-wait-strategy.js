
if (!Twist) {
    var Twist = {};
}

if (!Twist.setTimeoutWaitStrategy) {
	Twist.activeTimeouts = {};
	Twist.setTimeoutMaxDelay = 0;
    
    Twist.getNumberOfActiveSetTimeouts = function () {
		var result = 0;
    	for (var p in Twist.activeTimeouts) {
			result++;
    	}
    	return result;
    };
    
    Twist.setTimeoutWaitStrategy = function(){

        var realSetTimeout = window.setTimeout;
        var realClearTimeout = window.clearTimeout;
        var inSetTimeout = false;
        
        window.setTimeout = function(){
            var target = arguments[0];
            var delay = arguments[1];
            var wrapped = function(){
                try {
		        	delete Twist.activeTimeouts[wrapped.timeoutID + ''];
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
	            Twist.activeTimeouts[timeoutID + ''] = arguments[0];
            }
            return timeoutID;
        };
        
        window.clearTimeout = function(timeoutID){
        	delete Twist.activeTimeouts[timeoutID + ''];
            realClearTimeout(timeoutID);
        };
    }();
}
