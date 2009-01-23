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
package com.thoughtworks.twist.driver.web.selenium.locator;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.Browsers;

public class SafariSeleniumLocatorStrategiesTest extends AbstractSeleniumLocatorStrategiesTest {
    protected BrowserSession createBrowserSession() {
        return Browsers.SAFARI.create();
    }

    protected String referenceErrorMessage(String reference) {
        return Browsers.SAFARI.referenceError(reference);
    }
}
