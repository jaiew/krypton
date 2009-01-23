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
package com.thoughtworks.twist.driver.web.browser.locator;

import org.w3c.dom.Element;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class CssSelectorToXPathLocatorStrategy extends XPathLocatorStrategy {
    private static final String CSS_EQUALS_PREFIX = "css=";

    public boolean canLocate(String locator) {
        return locator != null && locator.startsWith(CSS_EQUALS_PREFIX);
    }

    public Element locate(BrowserSession session, String locator) {
        session.inject("twist-css-to-xpath.js", getClass());
        String xpath = cssToXPath(session, locator);
        return super.locate(session, xpath);
    }

    private String cssToXPath(BrowserSession session, String locator) {
        return session.execute("Twist.cssToXPath('" + locator.substring(CSS_EQUALS_PREFIX.length()) + "')");
    }
}
