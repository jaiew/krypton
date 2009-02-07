if (!Twist) {
    var Twist = {};
}

if (!Twist.setTimeoutWaitStrategy) {

    Twist.numberOfActiveSetTimeouts = 0;
    
    Twist.setTimeoutWaitStrategy = function(){

        function increaseNumberOfActiveSetTimeouts(){
            Twist.numberOfActiveSetTimeouts++;
        }
        
        function decreaseNumberOfActiveSetTimeouts(){
            Twist.numberOfActiveSetTimeouts--;
        }

        var realSetTimeout = window.setTimeout;
        var realClearTimeout = window.clearTimeout;

        window.setTimeout = function() {
	        var target = arguments[0];
	        var delay = arguments[1];
	        if (delay > 0) {
		        increaseNumberOfActiveSetTimeouts();
		        arguments[0] = function() {
		        	if ("string" === typeof(target)) {
		        		eval(target);
		        	} else {
			        	target.apply(this, arguments);
		        	}
			        decreaseNumberOfActiveSetTimeouts();
		        };
	        }
	        if (realSetTimeout.apply) {
	        	return realSetTimeout.apply(this, arguments);
	        } else {
	        	return realSetTimeout(arguments[0], delay);
	        }
        };

        window.clearTimeout = function(timeoutID) {
	        realClearTimeout(timeoutID);
	        if (Twist.numberOfActiveSetTimeouts > 0) {
		        decreaseNumberOfActiveSetTimeouts();
	        }
        };
    }
();
}
