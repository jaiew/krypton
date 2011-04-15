/****************************************************************************
 * Copyright 2008-2011 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Initial Contributors:
 *   Håkan Råberg
 *   Manish Chakravarty
 *   Pavan K S
 ***************************************************************************/
package com.thoughtworks.krypton.driver.web.browser.wait;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;
import com.thoughtworks.krypton.driver.web.browser.JavascriptException;

public class AjaxWaitStrategy implements LocationListener, WaitStrategy {
	Logger log = LoggerFactory.getLogger(getClass());

	BrowserSession session;
    Map<String,Pattern> exclusionPatterns = new HashMap<String,Pattern>();

	private int numberOfActiveAjaxRequests = 0;
	private BrowserFunction increaseNumberOfActiveAjaxRequests;
	private BrowserFunction decreaseNumberOfActiveAjaxRequests;

	public void init(BrowserSession session) {
		this.session = session;
		this.session.getBrowser().addLocationListener(this);
	}

	public void increaseNumberOfActiveAjaxRequests(String url) {
		numberOfActiveAjaxRequests++;
		log.debug("new Ajax request: {} (total: {})", url, numberOfActiveAjaxRequests);
	}

	public void decreaseNumberOfActiveAjaxRequests(String url) {
		numberOfActiveAjaxRequests--;
		log.debug("done Ajax request: {} (total: {})", url, numberOfActiveAjaxRequests);
	}

	public void changed(LocationEvent event) {
		if (event.top) {
			numberOfActiveAjaxRequests = 0;
			if (increaseNumberOfActiveAjaxRequests == null) {
				increaseNumberOfActiveAjaxRequests = new BrowserFunction(session.getBrowser(), "increaseNumberOfActiveAjaxRequests") {
					public Object function(Object[] arguments) {
						String url = arguments[0].toString();
						if (!isExcluded(url)) {
							increaseNumberOfActiveAjaxRequests(url);
						}
						return null;
					}
				};
			}
			if (decreaseNumberOfActiveAjaxRequests == null) {
				decreaseNumberOfActiveAjaxRequests = new BrowserFunction(session.getBrowser(), "decreaseNumberOfActiveAjaxRequests") {
					public Object function(Object[] arguments) {
						String url = arguments[0].toString();
						if (!isExcluded(url)) {
							decreaseNumberOfActiveAjaxRequests(url);
						}
						return null;
					}
				};
			}
			session.inject("twist-ajax-wait-strategy.js", getClass());
		}
	}

	public void changing(LocationEvent event) {
	}

	public boolean isBusy() {
		try {
			if (session.getBrowser().isDisposed()) {
				return false;
			}
			return numberOfActiveAjaxRequests > 0;
		} catch (NumberFormatException e) {
			return false;
		} catch (JavascriptException e) {
			return false;
		}
	}

	private boolean isExcluded(String location) {
		for (Pattern pattern : exclusionPatterns.values()) {
            if (pattern.matcher(location).matches()) {
            	return true;
            }
        }
		return false;
	}
    
    public void addURLExclusionPattern(String pattern) {
        exclusionPatterns.put(pattern, Pattern.compile(pattern));
    }

    public void removeURLExclusionPattern(String pattern) {
        exclusionPatterns.remove(pattern);
    }

	public int getNumberOfActiveAjaxRequests() {
		return numberOfActiveAjaxRequests;
	}
}
