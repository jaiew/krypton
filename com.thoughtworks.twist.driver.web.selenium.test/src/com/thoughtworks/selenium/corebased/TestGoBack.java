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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestGoBack.html.
 */
public class TestGoBack extends SeleneseTestCase
{
   public void testGoBack() throws Throwable {
		try {
			

/* Test Back and Forward */
			// open|../tests/html/test_click_page1.html|
			selenium.open("/selenium-server/tests/html/test_click_page1.html");
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());

			/* Click a regular link */
			// clickAndWait|link|
			selenium.click("link");
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page Target|
			verifyEquals("*Click Page Target", selenium.getTitle());
			// goBackAndWait||
			selenium.goBack();
			selenium.waitForPageToLoad("5000");
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());

			/* history.forward() generates 'Permission Denied' in IE>>>>>goForward////////////<<<<<
			// verifyTitle|Click Page Target|
			verifyEquals("*Click Page Target", selenium.getTitle());
 */

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
