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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestPause.html.
 */
public class TestPause extends SeleneseTestCase
{
   public void testPause() throws Throwable {
		try {
			

/* Test Select and Pause for Reload */
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");

			/* Make sure we can pause even when the page doesn't change */
			// pause|100|
			pause(100);
			// verifyTitle|Reload Page
			verifyEquals("*Reload Page", selenium.getTitle());

			boolean sawThrow8 = false;
			try {
				// originally verifyElementPresent|theSelect|
						assertTrue(selenium.isElementPresent("theSelect"));
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			verifyFalse(sawThrow8);
			
			// select|theSelect|Second Option
			selenium.select("theSelect", "Second Option");

			/* Make sure we can pause to wait for a page reload */

			/* Must pause longer than the slow-loading page takes (500ms) */
			// pause|5000|
			pause(5000);
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());

			boolean sawThrow16 = false;
			try {
				// originally verifyElementNotPresent|theSelect|
						assertTrue(!selenium.isElementPresent("theSelect"));
			}
			catch (Throwable e) {
				sawThrow16 = true;
			}
			verifyFalse(sawThrow16);
			

			boolean sawThrow17 = false;
			try {
				// originally verifyElementPresent|theSpan|
						assertTrue(selenium.isElementPresent("theSpan"));
			}
			catch (Throwable e) {
				sawThrow17 = true;
			}
			verifyFalse(sawThrow17);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
