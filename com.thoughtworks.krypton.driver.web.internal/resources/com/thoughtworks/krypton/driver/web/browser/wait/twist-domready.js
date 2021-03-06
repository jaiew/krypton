
if (!Twist) {
    var Twist = {};
}

if (!Twist.DomReady) {
    (function(){

        var DomReady = window.Twist.DomReady = {};
        
        // Everything that has to do with properly supporting our document ready event. Brought over from the most awesome jQuery. 
        
        var userAgent = navigator.userAgent.toLowerCase();
        
        // Figure out what browser is being used
        var browser = {
            safari: /webkit/.test(userAgent),
            msie: (/msie/.test(userAgent)) && (!/opera/.test(userAgent)),
            mozilla: (/mozilla/.test(userAgent)) && (!/(compatible|webkit)/.test(userAgent))
        };
		
		// TODO: Figure out a better way to detect different kind of reloads in IE.
		if (browser.msie) {
			Twist.reload = window.location.reload;
			window.location.reload = function(force) {
				if (force) {				
					window.location = window.location;
				} else {
					Twist.reload(false);
				}
			};
		}
		
        var readyBound = false;
        var isReady = false;
        var readyList = [];
        var setTimeoutFunction = Twist.realSetTimeout ? Twist.realSetTimeout : window.setTimeout;
        
        // Handle when the DOM is ready
        function domReady(){
            // Make sure that the DOM is not already loaded
            if (!isReady) {
                // Remember that the DOM is ready
                isReady = true;
                
                if (readyList) {
                    for (var fn = 0; fn < readyList.length; fn++) {
                        readyList[fn].call(window, []);
                    }
                    
                    readyList = [];
                }
            }
        };
        
        // From Simon Willison. A safe way to fire onload w/o screwing up everyone else.
        function addLoadEvent(func){
            var oldonload = window.onload;
            if (typeof window.onload != 'function') {
                window.onload = func;
            }
            else {
                window.onload = function(){
                    if (oldonload) {
                        oldonload();
                    }
                    func();
                }
            }
        };
        
        // does the heavy work of working through the browsers idiosyncracies (let's call them that) to hook onload.
        function bindReady(){
            if (readyBound) {
                return;
            }
            
            readyBound = true;
            
            // Mozilla, Opera (see further below for it) and webkit nightlies currently support this event
            if (document.addEventListener && !browser.opera) {
                // Use the handy event callback
                document.addEventListener("DOMContentLoaded", domReady, false);
            }
            
            // If IE is used and is not in a frame
            // Continually check to see if the document is ready
            if (browser.msie && window == top) 
                (function(){
                    if (isReady) 
                        return;
                    try {
                        // If IE is used, use the trick by Diego Perini
                        // http://javascript.nwbox.com/IEContentLoaded/
                        document.documentElement.doScroll("left");
                    } 
                    catch (error) {
                        setTimeoutFunction(arguments.callee, 0);
                        return;
                    }
                    // and execute any waiting functions
                    domReady();
                })();
            
            if (browser.safari) {
                var numStyles;
                (function(){
                    if (isReady) 
                        return;
                    if (document.readyState != "loaded" && document.readyState != "complete") {
                        setTimeoutFunction(arguments.callee, 0);
                        return;
                    }
                    if (numStyles === undefined) {
                        var links = document.getElementsByTagName("link");
                        for (var i = 0; i < links.length; i++) {
                            if (links[i].getAttribute('rel') == 'stylesheet') {
                                numStyles++;
                            }
                        }
                        var styles = document.getElementsByTagName("style");
                        numStyles += styles.length;
                    }
                    if (document.styleSheets.length != numStyles) {
                        setTimeoutFunction(arguments.callee, 0);
                        return;
                    }
                    
                    // and execute any waiting functions
                    domReady();
                })();
            }
            
            // A fallback to window.onload, that will always work
            addLoadEvent(domReady);
        };
        
        // This is the public function that people can use to hook up ready.
        DomReady.ready = function(fn, args){
            // Attach the listeners
            bindReady();
            
            // If the DOM is already ready
            if (isReady) {
                // Execute the function immediately
                fn.call(window, []);
            }
            else {
                // Add the function to the wait list
                readyList.push(function(){
                    return fn.call(window, []);
                });
            }
        };
        
        bindReady();
        
    })();
}
