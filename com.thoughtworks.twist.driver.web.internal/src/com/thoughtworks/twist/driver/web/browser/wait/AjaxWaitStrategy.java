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
package com.thoughtworks.twist.driver.web.browser.wait;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.JavascriptException;

public class AjaxWaitStrategy implements LocationListener, WaitStrategy {
    Log log = LogFactory.getLog(getClass());

    BrowserSession session;
	List<String> exclusionPatterns = new ArrayList<String>();
	
	private int numberOfActiveAjaxRequests = 0;
	private BrowserFunction increaseNumberOfActiveAjaxRequests;
	private BrowserFunction decreaseNumberOfActiveAjaxRequests;

	public void init(BrowserSession session) {
		this.session = session;
		this.session.getBrowser().addLocationListener(this);
	}

	public void changed(LocationEvent event) {
		if (event.top) {
			numberOfActiveAjaxRequests = 0;
			if (increaseNumberOfActiveAjaxRequests != null) {
				increaseNumberOfActiveAjaxRequests.dispose();
			}
			increaseNumberOfActiveAjaxRequests = new BrowserFunction(session.getBrowser(), "increaseNumberOfActiveAjaxRequests") {
				public Object function(Object[] arguments) {
					if (!isUrlExcluded(arguments[0].toString())) {
						numberOfActiveAjaxRequests++;
						log.debug("new Ajax request, total: " + numberOfActiveAjaxRequests);
					}
					return null;
				}
			};
			if (decreaseNumberOfActiveAjaxRequests != null) {
				decreaseNumberOfActiveAjaxRequests.dispose();
			}
			decreaseNumberOfActiveAjaxRequests = new BrowserFunction(session.getBrowser(), "decreaseNumberOfActiveAjaxRequests") {
				public Object function(Object[] arguments) {
					if (!isUrlExcluded(arguments[0].toString())) {						
						numberOfActiveAjaxRequests--;
						log.debug("done Ajax request, total: " + numberOfActiveAjaxRequests);
					}
					return null;
				}
			};
			session.inject("twist-ajax-wait-strategy.js", getClass());
		}
	}

	protected boolean isUrlExcluded(String url) {
        for (String pattern : exclusionPatterns) {
            if (url.matches(pattern)) {
                return true;
            }
        }
		return false;
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

	public void addURLExclusionPattern(String pattern) {
		exclusionPatterns.add(pattern);
	}

	public void removeURLExclusionPattern(String pattern) {
		exclusionPatterns.remove(pattern);
	}

	public int getNumberOfActiveAjaxRequests() {
		return numberOfActiveAjaxRequests;
	}
}
