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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.twist.driver.web.browser.BrowserFamily;
import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.JavascriptException;

public class DocumentReadyWaitStrategy implements WaitStrategy, LocationListener {
	Logger log = LoggerFactory.getLogger(getClass());

	private BrowserSession session;
	private boolean isDomReady = true;
	private boolean unloaded;

	private BrowserFunction documentIsReady;

	Map<String, Pattern> exclusionPatterns = new HashMap<String, Pattern>();

	public void init(BrowserSession session) {
		this.session = session;
		session.getBrowser().addLocationListener(this);

		addURLExclusionPattern("javascript:.*");
		addURLExclusionPattern("about:blank");
		addURLExclusionPattern("about:config");

		// This obviously needs to be configurable somehow.
		addURLExclusionPattern(".*analytics.live.com.*");
		addURLExclusionPattern(".*www.google.com/ig/ifpc_relay.*");
		addURLExclusionPattern(".*doubleclick.net.*");
		addURLExclusionPattern(".*adbrite.*");
	}

	public void changed(final LocationEvent event) {
		if (isExcluded(event.location)) {
			return;
		}
		if (event.top && BrowserFamily.IE != session.getBrowserFamily()) {
			isDomReady = false;

			if (documentIsReady != null) {
				documentIsReady.dispose();
			}
			documentIsReady = new BrowserFunction(session.getBrowser(), "documentIsReady") {
				public Object function(Object[] arguments) {
					isDomReady = true;
					log.debug("DOM is ready: {}", event.location);
					return null;
				}
			};
			session.inject("twist-domready.js", getClass());
			session.execute("Twist.DomReady.ready(documentIsReady)");
		} else {
			// TODO: We only need the window.location.reload override here
			// really.
			session.inject("twist-domready.js", getClass());
			isDomReady = true;
		}
	}

	public void changing(LocationEvent event) {
		String location = event.location;
		if (isExcluded(location)) {
			log.trace("skipping excluded {}", location);
			return;
		}
		isDomReady = false;
		unloaded = false;
	}

	public boolean isBusy() {
		if (BrowserFamily.IE == session.getBrowserFamily()) {
			if (isDomReady) {
				return false;
			}
			if (!canScrollDocument()) {
				unloaded = true;
			}
			isDomReady = unloaded && canScrollDocument();
		}
		return !isDomReady;
	}

	private boolean canScrollDocument() {
		try {
			session.execute("document.documentElement.doScroll('left');");
			return true;
		} catch (JavascriptException documentIsNotReady) {
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
}
