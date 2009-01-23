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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.Element;

import com.thoughtworks.twist.driver.web.browser.AbstractBaseBrowserSession;
import com.thoughtworks.twist.driver.web.browser.locator.ElementNotFoundException;
import com.thoughtworks.twist.driver.web.browser.locator.XPathLocatorStrategy;

public abstract class AbstractSeleniumLocatorStrategiesTest extends AbstractBaseBrowserSession {
    @Test
    public void shouldReturnElementForIdStartingWithIdEqualsUsingIdLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new IdLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedDiv = (Element) session.dom().getElementsByTagName("div").item(0);

        assertXmlEquals(expectedDiv, session.locate("id=1"));
    }

    @Test
    public void shouldReturnElementForNameStartingWithNameEqualsUsingIdLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new NameLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedInput = (Element) session.dom().getElementsByTagName("input").item(0);

        assertXmlEquals(expectedInput, session.locate("name=2"));
    }

    @Test
    public void shouldReturnElementMatchingIdUsingIdentifierLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new IdentifierLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedDiv = (Element) session.dom().getElementsByTagName("div").item(0);

        assertXmlEquals(expectedDiv, session.locate("1"));
    }

    @Test
    public void shouldReturnElementMatchingNameUsingIdentifierLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new IdentifierLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedInput = (Element) session.dom().getElementsByTagName("input").item(0);

        assertXmlEquals(expectedInput, session.locate("2"));
    }

    @Test
    public void shouldReturnElementWithLinkTextStartingWithLinkEqualsUsingLinkLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new LinkLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedInput = (Element) session.dom().getElementsByTagName("a").item(0);

        assertXmlEquals(expectedInput, session.locate("link=My Link"));
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingName() throws Exception {
        String locator = "name=1";
        try {
            session.addLocatorStrategy(new NameLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingId() throws Exception {
        String locator = "id=2";
        try {
            session.addLocatorStrategy(new IdLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingLink() throws Exception {
        String locator = "link=To Nowhere";
        try {
            session.addLocatorStrategy(new LinkLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingIdentifer() throws Exception {
        String locator = "no-id";
        try {
            session.addLocatorStrategy(new IdentifierLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingXPath() throws Exception {
        String locator = "//img";
        try {
            session.addLocatorStrategy(new XPathLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }
}
