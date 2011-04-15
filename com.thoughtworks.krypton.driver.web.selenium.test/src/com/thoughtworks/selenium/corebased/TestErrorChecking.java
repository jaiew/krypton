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
 * Generated from //socrates/unixhome/nelsons/work/selenium-rc/trunk/javascript-core/src/selenium/tests/TestErrorChecking.html.
 */
public class TestErrorChecking extends SeleneseTestCase
{
   public void test() throws Throwable {
   	try {
  
/* !!!Should Fail!!! Tests for expectError and expectFailure commands       */
		// open|./tests/html/test_click_page1.html|
		selenium.open("/selenium-server/tests/html/test_click_page1.html");
		// verifyText|link|Click here for next page
		verifyEquals("Click here for next page", selenium.getText("link"));

		/* These tests should all fail, as they are checking the error checking commands. */

		boolean sawThrow7 = false;
		try {
					// assertText|link|foo
		assertEquals("foo", selenium.getText("link"));
		}
		catch (Throwable e) {
			sawThrow7 = true;
		}
		verifyTrue(sawThrow7);
		

		boolean sawThrow9 = false;
		try {
					// assertText|notAnElement|foo
		assertEquals("foo", selenium.getText("notAnElement"));
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
