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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFramesClick.html.
 */
public class TestFramesClick extends SeleneseTestCase
{
   public void testFramesClick() throws Throwable {
		try {
			

/* TestFramesClick */
			// open|../tests/html/Frames.html|
			selenium.open("/selenium-server/tests/html/Frames.html");
			// selectFrame|mainFrame|
			selenium.selectFrame("mainFrame");
			// open|../tests/html/test_click_page1.html|
			selenium.open("/selenium-server/tests/html/test_click_page1.html");

			/* Click a regular link */
			// verifyText|link|Click here for next page
			verifyEquals("Click here for next page", selenium.getText("link"));
			// clickAndWait|link|
			selenium.click("link");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page Target|
			verifyEquals("*Click Page Target", selenium.getTitle());
			// clickAndWait|previousPage|
			selenium.click("previousPage");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());

			/* Click a link with an enclosed image */
			// clickAndWait|linkWithEnclosedImage|
			selenium.click("linkWithEnclosedImage");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page Target|
			verifyEquals("*Click Page Target", selenium.getTitle());
			// clickAndWait|previousPage|
			selenium.click("previousPage");
			selenium.waitForPageToLoad("5000");

			/* Click an image enclosed by a link */
			// clickAndWait|enclosedImage|
			selenium.click("enclosedImage");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page Target|
			verifyEquals("*Click Page Target", selenium.getTitle());
			// clickAndWait|previousPage|
			selenium.click("previousPage");
			selenium.waitForPageToLoad("5000");

			/* Click a link with an href anchor target within this page */
			// click|linkToAnchorOnThisPage|
			selenium.click("linkToAnchorOnThisPage");
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());

			/* Click a link where onclick returns false */
			// click|linkWithOnclickReturnsFalse|
			selenium.click("linkWithOnclickReturnsFalse");

			/* Need a pause to give the page a chance to reload (so this test can fail) */
			// pause|300
			pause(300);
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());

			// open|../tests/html/test_click_page1.html|
//			selenium.open("/selenium-server/tests/html/test_click_page1.html");

			/* TODO Click a link with a target attribute */

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
