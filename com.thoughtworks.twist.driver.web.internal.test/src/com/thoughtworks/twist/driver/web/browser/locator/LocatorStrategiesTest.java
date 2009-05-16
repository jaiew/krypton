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

import org.junit.Test;
import org.w3c.dom.Element;

import com.thoughtworks.twist.driver.web.browser.AbstractBaseBrowserSession;
import com.thoughtworks.twist.driver.web.browser.BrowserSession;

import static org.junit.Assert.*;

public class LocatorStrategiesTest extends AbstractBaseBrowserSession {
    @Test
    public void shouldThrowElementNotFoundExceptionWithoutStrategies() throws Exception {
        String locator = "locator";
        try {
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionWithoutMatchingStrategy() throws Exception {
        String locator = "locator";
        try {
            session.addLocatorStrategy(new CannotLocateDocumentElementLocatorStrategy());
            render("<html/>");
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldReturnDocumentElementForProvidedLocator() throws Exception {
        session.addLocatorStrategy(new DocumentElementLocatorStrategy());
        render("<html/>");

        assertEquals(xmlAsString(session.dom().getDocumentElement()), xmlAsString(session.locate("")));
    }

    @Test
    public void shouldNotBeAbleToLocateNonXPathUsingXPathStrategy() throws Exception {
        assertFalse(new XPathLocatorStrategy().canLocate(""));
    }

    @Test
    public void shouldBeAbleToLocateSlashSlashXPathUsingXPathStrategy() throws Exception {
        assertTrue(new XPathLocatorStrategy().canLocate("//html"));
    }

    @Test
    public void shouldBeAbleToLocateXPathEqualsUsingXPathStrategy() throws Exception {
        assertTrue(new XPathLocatorStrategy().canLocate("xpath=//html"));
    }

    @Test
    public void shouldReturnElementForXpathStartingWithSlashSlashUsingXPathLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new XPathLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedDiv = (Element) session.dom().getElementById("1");

        assertEquals("1", expectedDiv.getAttribute("id"));
        assertXmlEquals(expectedDiv, session.locate("//div"));
    }

    @Test
    public void shouldReturnElementForXpathStartingWithXPathEqualsUsingXPathLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new XPathLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedDiv = (Element) session.dom().getElementById("1");

        assertXmlEquals(expectedDiv, session.locate("xpath=//div"));
    }

    @Test
    public void shouldReturnElementForCssSelectorWithCssEqualsUsingCssLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new CssSelectorToXPathLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedInput = (Element) session.dom().getElementsByTagName("input").item(0);

        assertXmlEquals(expectedInput, session.locate("css=input[name=\"2\"]"));
    }

    @Test
    public void shouldReturnElementForDomExpressionWithDomEqualsUsingDomLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new DomLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedBody = (Element) session.dom().getElementsByTagName("body").item(0);

        assertXmlEquals(expectedBody, session.locate("dom=document.body"));
    }

    @Test
    public void shouldReturnElementForDomExpressionStartingWithDocumentUsingDomLocatorStrategy() throws Exception {
        session.addLocatorStrategy(new DomLocatorStrategy());
        render(readResource("test-locators.html"));
        Element expectedDiv = (Element) session.dom().getElementById("1");

        assertXmlEquals(expectedDiv, session.locate("document.getElementById('1')"));
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingCssSelector() throws Exception {
        String locator = "css=input[name=\"1\"]";
        try {
            session.addLocatorStrategy(new CssSelectorToXPathLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowElementNotFoundExceptionForNonExistingDomExpression() throws Exception {
        String locator = "document.getElementById('4')";
        try {
            session.addLocatorStrategy(new DomLocatorStrategy());
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

    @Test
    public void shouldThrowElementNotFoundExceptionIfNotPatientEnough() throws Exception {
        String locator = "document.getElementById('3')";
        try {
            session.addLocatorStrategy(new DomLocatorStrategy());
            render(readResource("test-locators.html"));
            session.locate(locator);
            fail();
        } catch (ElementNotFoundException expected) {
            assertEquals("No element found matching locator: " + locator, expected.getMessage());
        }
    }

    private static class DocumentElementLocatorStrategy implements LocatorStrategy {
        public Element locate(BrowserSession session, String locator) {
            return session.dom().getDocumentElement();
        }

        public boolean canLocate(String locator) {
            return true;
        }
    }

    private static class CannotLocateDocumentElementLocatorStrategy extends DocumentElementLocatorStrategy {
        public boolean canLocate(String locator) {
            return false;
        }
    }
}
