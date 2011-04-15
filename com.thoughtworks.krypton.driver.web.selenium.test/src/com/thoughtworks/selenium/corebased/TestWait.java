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
package com.thoughtworks.selenium.corebased;
import com.thoughtworks.selenium.*;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestWait.html.
 */
public class TestWait extends SeleneseTestCase
{
   public void testWait() throws Throwable {
		try {
			

/* Test AndWait commands for Reload */

			/* Link click */
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");
			// clickAndWait|theLink|
			selenium.click("theLink");
			selenium.waitForPageToLoad("5000");

			/* Page should reload */
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");
			// selectAndWait|theSelect|Second Option
			selenium.select("theSelect", "Second Option");
			selenium.waitForPageToLoad("5000");

			/* Page should reload */
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());

			/* Textbox with onblur */
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");
			// type|theTextbox|new value
			selenium.type("theTextbox", "new value");
			// fireEventAndWait|theTextbox|blur
			selenium.fireEvent("theTextbox", "blur");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());

			/* Submit button */
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");
			// clickAndWait|theSubmit|
			selenium.click("theSubmit");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());
			// clickAndWait|slowPage_reload|
			selenium.click("slowPage_reload");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
