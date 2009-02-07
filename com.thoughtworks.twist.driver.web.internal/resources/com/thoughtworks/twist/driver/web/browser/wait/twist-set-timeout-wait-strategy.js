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
        var inSetTimeout = false;

        window.setTimeout = function() {
	        var target = arguments[0];
	        var delay = arguments[1];
	        if (delay > 0 && !inSetTimeout) {
		        increaseNumberOfActiveSetTimeouts();
		        arguments[0] = function() {
		        	try {
			        	inSetTimeout = true;
			        	if ("string" === typeof(target)) {
			        		eval(target);
			        	} else {
				        	target.apply(this, arguments);
			        	}
		        	} finally {
				        decreaseNumberOfActiveSetTimeouts();
				        inSetTimeout = false;
			        }
		        };
	        }
	        return Function.prototype.apply.call(realSetTimeout, this, arguments);
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
