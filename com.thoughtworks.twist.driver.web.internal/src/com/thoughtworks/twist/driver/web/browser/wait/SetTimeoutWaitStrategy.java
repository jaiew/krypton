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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.JavascriptException;

public class SetTimeoutWaitStrategy implements LocationListener, WaitStrategy {
	private static final int DEFAULT_SET_TIMEOUT_MAX_DELAY = 2500;
	BrowserSession session;
    Log log = LogFactory.getLog(getClass());

    private int setTimeoutMaxDelay = DEFAULT_SET_TIMEOUT_MAX_DELAY;
	private BrowserFunction addActiveSetTimeout;
	private BrowserFunction removeActiveSetTimeout;
	private Set<Integer> activeSetTimeouts = new HashSet<Integer>();

	public void init(BrowserSession session) {
		this.session = session;
        session.getBrowser().addLocationListener(this);
    }

	public void changed(LocationEvent event) {
		if (event.top) {
			activeSetTimeouts.clear();
			if (addActiveSetTimeout != null) {
				addActiveSetTimeout.dispose();
			}
			addActiveSetTimeout = new BrowserFunction(session.getBrowser(), "addActiveSetTimeout") {
				public Object function(Object[] arguments) {
					activeSetTimeouts.add(((Number) arguments[0]).intValue());
					return null;
				}
			};
			if (removeActiveSetTimeout != null) {
				removeActiveSetTimeout.dispose();
			}
			removeActiveSetTimeout = new BrowserFunction(session.getBrowser(), "removeActiveSetTimeout") {
				public Object function(Object[] arguments) {
					activeSetTimeouts.remove(((Number) arguments[0]).intValue());
					return null;
				}
			};
			session.inject("twist-set-timeout-wait-strategy.js", getClass());
			session.execute("Twist.setTimeoutMaxDelay = " + setTimeoutMaxDelay);
		}
	}

	public void changing(LocationEvent event) {
	}

	public boolean isBusy() {
		try {
			if (session.getBrowser().isDisposed()) {
				return false;
			}
			return !activeSetTimeouts.isEmpty();
		} catch (JavascriptException e) {
			return false;
		}
	}

    public void setSetTimeoutMaxDelay(int maxDelay) {
		this.setTimeoutMaxDelay = maxDelay;
	}

	public int getNumberOfActiveSetTimeouts() {
		return activeSetTimeouts.size();
	}
}
