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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestElementPresent.html.
 */
public class TestElementPresent extends SeleneseTestCase
{
   public void testElementPresent() throws Throwable {
		try {
			

/* TestElementPresent */
			// open|../tests/html/test_element_present.html|
			selenium.open("/selenium-server/tests/html/test_element_present.html");
			assertTrue(selenium.isElementPresent("aLink"));
			// click|removeLinkAfterAWhile|
			selenium.click("removeLinkAfterAWhile");
			boolean sawCondition6 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((!selenium.isElementPresent("aLink"))) {
						sawCondition6 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
//				pause(1000);
			}
			assertTrue(sawCondition6);
			
			assertTrue(!selenium.isElementPresent("aLink"));
			// click|addLinkAfterAWhile|
			selenium.click("addLinkAfterAWhile");
			boolean sawCondition9 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((selenium.isElementPresent("aLink"))) {
						sawCondition9 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
//				pause(1000);
			}
			assertTrue(sawCondition9);
			
			assertTrue(selenium.isElementPresent("aLink"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
