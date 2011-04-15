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
package com.thoughtworks.krypton.driver.web.selenium.dialog;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;

public class ConfirmationHandler implements LocationListener, DialogHandler {
	private final BrowserSession session;
	private List<String> confirmations = new ArrayList<String>();
	private boolean confirmationAnswer = true;
	private BrowserFunction confirm;

	Logger log = LoggerFactory.getLogger(getClass());

	public ConfirmationHandler(BrowserSession session) {
		this.session = session;
		session.getBrowser().addLocationListener(this);
	}

	public void changed(LocationEvent event) {
		if (event.top) {
			if (confirm == null) {
				confirm = new BrowserFunction(session.getBrowser(), "confirm") {
					public Object function(Object[] arguments) {
						String message = (String) arguments[0];
						confirmations.add(message);
						log.info("confirmation: {}: {}", message, confirmationAnswer);
						boolean result = confirmationAnswer;
						confirmationAnswer = true;
						return result;
					}
				};
			}
		}
	}

	public void changing(LocationEvent event) {
	}

	public String getMessage() {
		return confirmations.remove(0);
	}

	public boolean isDialogPresent() {
		return !confirmations.isEmpty();
	}

	public void cancel() {
		confirmationAnswer = false;
	}

	public void ok() {
		confirmationAnswer = true;
	}
}
