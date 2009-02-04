
if (!Twist) {
    var Twist = {};
}

if (!Twist.ajaxWaitStrategy) {
    Twist.numberOfActiveAjaxRequests = 0;
    Twist.ajaxExclusionPatterns = [];
    
    Twist.ajaxWaitStrategy = function(){
    
        function increaseNumberOfActiveAjaxRequests(url){
			if (!isURLExcluded(url)) {				
	            Twist.numberOfActiveAjaxRequests++;
			}
        }
        
        function decreaseNumberOfActiveAjaxRequests(url){
			if (!isURLExcluded(url)) {
				Twist.numberOfActiveAjaxRequests--;
			}
        }
        
        function isURLExcluded(url){
            for (var i = 0; i < Twist.ajaxExclusionPatterns.length; i++) {
                if (Twist.ajaxExclusionPatterns[i].test(url)) {
                    return true;
                }
            }
            return false;
        }
        
        function interceptActiveXObject(){
            if (window.RealActiveXObject) {
                return;
            }
            window.RealActiveXObject = window.ActiveXObject;
            window.ActiveXObject = function(aClass, serverName){
                var base;
                if (serverName) {
                    base = new window.RealActiveXObject(aClass, serverName);
                }
                else {
                    base = new window.RealActiveXObject(aClass);
                }
                if (aClass.toLowerCase().indexOf('xmlhttp') != -1) {
                    var self = this;
                    this.open = function(type, url, asynchronous, username, password){
						self.url = url;
                        increaseNumberOfActiveAjaxRequests(url);
                        base.onreadystatechange = function(){
                            self.readyState = base.readyState;
                            
                            if (base.readyState == 4) {
                                self.status = base.status;
                                self.statusText = base.statusText;
                                self.responseText = base.responseText;
                                self.responseXML = base.responseXML;
                                decreaseNumberOfActiveAjaxRequests(url);
                            }
                            if (self.onreadystatechange) {
                                self.onreadystatechange();
                            }
                        };
                        return base.open(type, url, asynchronous, username, password);
                    };
                    
                    this.send = function(data){
                        return base.send(data);
                    };
                    
                    this.setRequestHeader = function(header, value){
                        base.setRequestHeader(header, value);
                    };
                    
                    this.getResponseHeader = function(header){
                        return base.getResponseHeader(header);
                    };
                    
                    this.getAllResponseHeaders = function(){
                        return base.getAllResponseHeaders();
                    };
                    
                    this.abort = function(){
                        decreaseNumberOfActiveAjaxRequests(self.url);
                        return base.abort();
                    };
                    return this;
                }
                return base;
            };
        }
        
        function interceptXMLHttpRequest(){
            if (window.XMLHttpRequest.prototype.realOpen) {
                return;
            }
            window.XMLHttpRequest.prototype.realOpen = window.XMLHttpRequest.prototype.open;
            window.XMLHttpRequest.prototype.open = function(type, url, asynchronous, username, password){
                if (asynchronous) {
                    increaseNumberOfActiveAjaxRequests(url);
                    this.addEventListener('load', function(event){
                        decreaseNumberOfActiveAjaxRequests(url);
                    }, true);
                    this.addEventListener('error', function(event){
                        decreaseNumberOfActiveAjaxRequests(url);
                    }, true);
                }
                return this.realOpen(type, url, asynchronous, username, password);
            };
        }
        
        if (window.ActiveXObject) {
            interceptActiveXObject();
        }
        else {
            interceptXMLHttpRequest();
        }
    }
();
}
