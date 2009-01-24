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
package com.thoughtworks.twist.driver.web.selenium;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class SeleniumActivator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		context.registerService(Selenium.class.getName(), new ServiceFactory() {
			public Object getService(Bundle bundle,
					ServiceRegistration registration) {
				return new TwistSelenium("", BrowserSession.create());
			}

			public void ungetService(Bundle bundle,
					ServiceRegistration registration, Object service) {
			}
		}, null);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Selenium stopped");
	}
}
