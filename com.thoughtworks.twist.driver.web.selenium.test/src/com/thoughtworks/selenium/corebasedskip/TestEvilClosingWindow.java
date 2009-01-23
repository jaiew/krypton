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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestEvilClosingWindow.html.
 */
public class TestEvilClosingWindow extends SeleneseTestCase
{
   public void testEvilClosingWindow() throws Throwable {
		try {
			

/* Test selectWindow */
			// open|../tests/html/test_select_window.html|
			selenium.open("/selenium-server/tests/html/test_select_window.html");
			// click|popupPage|
			selenium.click("popupPage");
			// waitForPopUp|myPopupWindow|5000
			selenium.waitForPopUp("myPopupWindow", "5000");
			// selectWindow|myPopupWindow|
			selenium.selectWindow("myPopupWindow");
			// verifyLocation|*/tests/html/test_select_window_popup.html|
			verifyEquals("*/tests/html/test_select_window_popup.html", selenium.getLocation());
			// close||
			selenium.close();

			boolean sawThrow9 = false;
			try {
							// assertLocation|*/tests/html/test_select_window_popup.html|
			assertEquals("*/tests/html/test_select_window_popup.html", selenium.getLocation());
			}
			catch (Throwable e) {
				sawThrow9 = true;
			}
			verifyTrue(sawThrow9);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
