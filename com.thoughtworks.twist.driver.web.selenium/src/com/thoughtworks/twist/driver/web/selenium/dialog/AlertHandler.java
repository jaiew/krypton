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
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class AlertHandler implements LocationListener, DialogHandler {
	private final BrowserSession session;
	private List<String> alerts = new ArrayList<String>();
    Log log = LogFactory.getLog(getClass());

	private BrowserFunction alert;

	public AlertHandler(BrowserSession session) {
		this.session = session;
		session.getBrowser().addLocationListener(this);
	}


	public void changed(LocationEvent event) {
		if (event.top) {
			if (alert != null) {
				alert.dispose();
			}
			alert = new BrowserFunction(session.getBrowser(), "alert") {
				public Object function(Object[] arguments) {
					alerts.add((String) arguments[0]);
					return null;
				}
			};
		}
	}

	public void changing(LocationEvent event) {
	}

	public String getMessage() {
		return alerts.remove(0);
	}

	public boolean isDialogPresent() {
		return !alerts.isEmpty();
	}
}
