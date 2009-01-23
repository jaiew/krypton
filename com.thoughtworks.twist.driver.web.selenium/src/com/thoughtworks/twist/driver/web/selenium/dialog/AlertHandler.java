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
package com.thoughtworks.twist.driver.web.selenium.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class AlertHandler implements ProgressListener, StatusTextListener, DialogHandler {
	private static final String JAVASCRIPT_ALERT = "javascript-alert: ";

	private final BrowserSession session;
	private List<String> alerts = new ArrayList<String>();
    Log log = LogFactory.getLog(getClass());

	public AlertHandler(BrowserSession session) {
		this.session = session;
		session.browser.addProgressListener(this);
		session.browser.addStatusTextListener(this);
		completed(null);
	}

	public void changed(ProgressEvent event) {
	}

	public void completed(ProgressEvent event) {
		session.browser.execute("window.alert = function(message) { window.status = '"
				+ JAVASCRIPT_ALERT + "' + message; window.status = ''; }");
		session.pumpEvents();
	}

	public void changed(StatusTextEvent event) {
		if (event.text.startsWith(JAVASCRIPT_ALERT)) {
			alerts.add(event.text.substring(JAVASCRIPT_ALERT.length()));
			log.info("alert: " + alerts.get(alerts.size() - 1));
		}
	}

	public String getMessage() {
		return alerts.remove(0);
	}

	public boolean isDialogPresent() {
		return !alerts.isEmpty();
	}
}
