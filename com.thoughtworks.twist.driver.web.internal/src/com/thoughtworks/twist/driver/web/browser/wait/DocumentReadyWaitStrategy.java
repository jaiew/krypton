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

import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

import static com.thoughtworks.twist.driver.web.browser.BrowserFamily.*;

public class DocumentReadyWaitStrategy implements WaitStrategy,
		LocationListener {
	private BrowserSession session;
	private boolean hasListener;

	public void init(final BrowserSession session) {
		this.session = session;
		session.getBrowser().addLocationListener(this);
	}

	public void changed(LocationEvent event) {
		if (MOZILLA == session.getBrowserFamily()) {
			session
					.execute("if (!Twist) { var Twist = {}; }"
							+ "if (!Twist.hasContentLoadListener) { document.addEventListener('DOMContentLoaded', function() { "
							+ "Twist.contentLoaded = true; }, false); Twist.hasContentLoadListener = true; }");
			hasListener = true;
		}
	}

	public void changing(LocationEvent event) {
		hasListener = false;
	}

	public boolean isBusy() {
		if (SAFARI == session.getBrowserFamily()) {
			String readyState = session.evaluate("document ? document.readyState : ''");
			return !("complete".equals(readyState) || "interactive".equals(readyState));
		} else if (IE == session.getBrowserFamily()) {
			String readyState = session.evaluate("document ? document.readyState : ''");
			return !"complete".equals(readyState);
		} else if (MOZILLA == session.getBrowserFamily()) {
			return Boolean
					.parseBoolean(session
							.evaluate("typeof document.getElementsByTagName == 'undefined' "
									+ (hasListener ? "|| typeof Twist.contentLoaded == 'undefined' "
											: "") + "|| document.body == null"));
		}
		return false;
	}
}
