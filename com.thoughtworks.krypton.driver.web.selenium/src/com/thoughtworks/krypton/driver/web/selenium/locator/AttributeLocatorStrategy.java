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

import org.w3c.dom.Element;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;
import com.thoughtworks.krypton.driver.web.browser.locator.XPathLocatorStrategy;

public class AttributeLocatorStrategy extends XPathLocatorStrategy {
    protected String attribute;
    protected String prefix;
    
    

    public AttributeLocatorStrategy(String attribute, String name) {
        this.attribute = attribute;
        this.prefix = name + "=";
    }

    public boolean canLocate(String locator) {
        return locator != null && locator.startsWith(prefix);
    }

    public Element locate(BrowserSession session, String locator) {
        return super.locate(session, ("//*[@" + attribute + "='" + locator.substring(prefix.length()) + "']"));
    }
}
