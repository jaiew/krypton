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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.locator.XPathLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.GlobMatcher;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.RegexpMatcher;

public class LinkLocatorStrategy extends XPathLocatorStrategy {
	private static final String LINK_EQUALS_PREFIX = "link=";

	public boolean canLocate(String locator) {
		return locator != null && locator.startsWith(LINK_EQUALS_PREFIX);
	}

	public Element locate(BrowserSession session, String locator) {
		NodeList links = session.dom().getElementsByTagName("a");
		locator = locator.substring(LINK_EQUALS_PREFIX.length());
		for (int i = 0; i < links.getLength(); i++) {
			Element link = (Element) links.item(i);
			char nonBreakingSpace = (char) 160;
			String linkText = link.getTextContent().replace(nonBreakingSpace,
					' ').trim();

			RegexpMatcher regexpMatcher = new RegexpMatcher();
			if (regexpMatcher.canMatch(locator)
					&& regexpMatcher.matches(locator, linkText)) {
				return link;
			}

			if (new GlobMatcher().matchesExactly(locator, linkText)) {
				return link;
			}
		}
		return null;
	}
}
