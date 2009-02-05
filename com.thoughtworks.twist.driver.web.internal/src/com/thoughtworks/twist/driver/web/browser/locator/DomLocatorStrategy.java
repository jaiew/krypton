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
import com.thoughtworks.twist.driver.web.browser.JavascriptException;

public class DomLocatorStrategy extends XPathLocatorStrategy {
    private static final String DOCUMENT = "document";
    private static final String WINDOW_DOT = "window.";
    private static final String DOM_EQUALS_PREFIX = "dom=";

    public boolean canLocate(String locator) {
        return locator != null && (locator.startsWith(DOM_EQUALS_PREFIX) || locator.startsWith(DOCUMENT));
    }

    public Element locate(BrowserSession session, String locator) {
        try {
            session.inject("twist-dom-to-xpath.js", getClass());
            if (locator.startsWith(DOM_EQUALS_PREFIX)) {
                locator = locator.substring(DOM_EQUALS_PREFIX.length());
            }
            if (locator.startsWith(WINDOW_DOT)) {
                locator = locator.substring(WINDOW_DOT.length());
            }
            
            locator = session.getWindowExpression() + "." + locator;
            String xpath = domToXPath(session, locator);
            return super.locate(session, xpath);
        } catch (JavascriptException e) {
            if (e.isTypeError() || e.isReferenceError()) {
                return null;
            }
            throw e;
        }
    }

    private String domToXPath(BrowserSession session, String locator) {
        return session.evaluate("Twist.domToXPath(" + locator + ")");
    }
}
