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
package com.thoughtworks.selenium.corebasedskip;
import com.thoughtworks.selenium.*;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestAddLocationStrategy.html.
 */
public class TestAddLocationStrategy extends SeleneseTestCase
{
   public void testAddLocationStrategy() throws Throwable {
		try {
			

/* TestAddLocationStrategy */
			// open|../tests/html/test_click_page1.html|
			selenium.open("/selenium-server/tests/html/test_click_page1.html");
			// addLocationStrategy|foo|return inDocument.getElementById(locator);
			selenium.addLocationStrategy("foo", "return inDocument.getElementById(locator);");
			assertTrue(selenium.isElementPresent("foo=link"));
			// refreshAndWait||
			selenium.refresh();
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.isElementPresent("foo=link"));

			boolean sawThrow8 = false;
			try {
							// addLocationStrategy|bar|[[[;
			selenium.addLocationStrategy("bar", "[[[;");
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			assertTrue(sawThrow8);
			
			// addLocationStrategy|bar|thisVariableDoesNotExist;
			selenium.addLocationStrategy("bar", "thisVariableDoesNotExist;");

			boolean sawThrow11 = false;
			try {
							assertTrue(selenium.isElementPresent("bar=link"));
			}
			catch (Throwable e) {
				sawThrow11 = true;
			}
			assertTrue(sawThrow11);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
