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
package com.thoughtworks.krypton.driver.web.selenium.locator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;
import com.thoughtworks.krypton.driver.web.browser.locator.XPathLocatorStrategy;

public class IdentifierLocatorStrategy extends XPathLocatorStrategy {
    private String IDENTIFIER_PREFIX = "identifier=";

    public boolean canLocate(String locator) {
        return locator != null;
    }

    public Element locate(BrowserSession session, String locator) {
        if (locator.startsWith(IDENTIFIER_PREFIX)) {
            locator = locator.substring(IDENTIFIER_PREFIX.length());
        }
        Element element;
		try {
			element = new IdLocatorStrategy().locate(session, "id=" + locator);
			if (element == null) {
				element = new NameLocatorStrategy().locate(session, "name=" + locator);
			}
		} catch (RuntimeException e) {
			if (e.getCause() instanceof XPathExpressionException) {
				return null;
			}
			throw e;
		}
        return element;
    }
}
