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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestSelectWindowTitle.html.
 */
public class TestSelectWindowTitle extends SeleneseTestCase
{
   public void testSelectWindowTitle() throws Throwable {
		try {
			

/* Test selectWindow by title */
			// open|../tests/html/test_select_window.html|
			selenium.open("/selenium-server/tests/html/test_select_window.html");
			// click|popupPage|
			selenium.click("popupPage");
			// waitForPopUp|myPopupWindow|5000
			selenium.waitForPopUp("myPopupWindow", "5000");
			// selectWindow|Select Window Popup|
			selenium.selectWindow("Select Window Popup");
			// verifyLocation|*/tests/html/test_select_window_popup.html|
			verifyEquals("*/tests/html/test_select_window_popup.html", selenium.getLocation());
			// verifyTitle|Select Window Popup|
			verifyEquals("*Select Window Popup", selenium.getTitle());
			// verifyAllWindowNames|*,*|
			verifyEquals("*,*", selenium.getAllWindowNames());
			// verifyAllWindowNames|regexp:myPopupWindow|
			verifyEquals("regexp:myPopupWindow", selenium.getAllWindowNames());
			// close||
			selenium.close();
			// selectWindow|null|
			selenium.selectWindow("null");
			// verifyLocation|*/tests/html/test_select_window.html|
			verifyEquals("*/tests/html/test_select_window.html", selenium.getLocation());
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
			// selectWindow|null|
			selenium.selectWindow("null");
			// click|popupAnonymous|
			selenium.click("popupAnonymous");
			// waitForPopUp|anonymouspopup|5000
			selenium.waitForPopUp("anonymouspopup", "5000");
			// selectWindow|anonymouspopup|
			selenium.selectWindow("anonymouspopup");
			// verifyLocation|*/tests/html/test_select_window_popup.html|
			verifyEquals("*/tests/html/test_select_window_popup.html", selenium.getLocation());
			// click|closePage|
			selenium.click("closePage");
			// selectWindow|null|
			selenium.selectWindow("null");
			// click|popupAnonymous|
			selenium.click("popupAnonymous");
			// waitForPopUp|anonymouspopup|5000
			selenium.waitForPopUp("anonymouspopup", "5000");
			// selectWindow|anonymouspopup|
			selenium.selectWindow("anonymouspopup");
			// verifyLocation|*/tests/html/test_select_window_popup.html|
			verifyEquals("*/tests/html/test_select_window_popup.html", selenium.getLocation());
			// click|closePage2|
			selenium.click("closePage2");

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
