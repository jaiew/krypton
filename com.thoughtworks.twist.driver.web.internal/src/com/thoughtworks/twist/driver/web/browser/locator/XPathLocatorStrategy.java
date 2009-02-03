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

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class XPathLocatorStrategy implements LocatorStrategy {
    private static final String XPATH_EQUALS_PREFIX = "xpath=";
    private static final String SLASH_SLASH_PREFIX = "//";

    public boolean canLocate(String locator) {
        return locator != null && (locator.startsWith(SLASH_SLASH_PREFIX) || locator.startsWith(XPATH_EQUALS_PREFIX));
    }

    public Element locate(BrowserSession session, String locator) {
        if (locator.startsWith(XPATH_EQUALS_PREFIX)) {
            locator = locator.substring(XPATH_EQUALS_PREFIX.length());
        }
        List<Node> locateAll = session.locateAll(locator);
        if (locateAll.isEmpty()) {
        	return null;
        }
		return (Element) locateAll.get(0);
    }
}
