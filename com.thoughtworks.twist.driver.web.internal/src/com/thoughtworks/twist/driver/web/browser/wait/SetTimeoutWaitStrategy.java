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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.JavascriptException;

public class SetTimeoutWaitStrategy implements LocationListener, WaitStrategy {
	BrowserSession session;
    Log log = LogFactory.getLog(getClass());

    public void init(BrowserSession session) {
		this.session = session;
        session.getBrowser().addLocationListener(this);
    }

	public void changed(LocationEvent event) {
		session.inject("twist-set-timeout-wait-strategy.js", getClass());
	}

	public void changing(LocationEvent event) {
	}

	public boolean isBusy() {
		try {
			if (session.getBrowser().isDisposed()) {
				return false;
			}
			String timeouts = session.evaluate("Twist.numberOfActiveSetTimeouts");
			return Integer.parseInt(timeouts) > 0;
		} catch (NumberFormatException e) {
			return false;
		} catch (JavascriptException e) {
			return false;
		}
	}
}
