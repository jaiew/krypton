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
import com.thoughtworks.selenium.SeleneseTestCase;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestCursorPosition.html.
 */
public class TestCursorPosition extends SeleneseTestCase
{
   public void testCursorPosition() throws Throwable {
		try {
			

/* TestCursorPosition */
			// open|../tests/html/test_type_page1.html|
			selenium.open("/selenium-server/tests/html/test_type_page1.html");

			boolean sawThrow4 = false;
			try {
							// assertCursorPosition|username|8
			assertEquals("8", selenium.getCursorPosition("username"));
			}
			catch (Throwable e) {
				sawThrow4 = true;
			}
			verifyTrue(sawThrow4);
			
			// windowFocus||
			selenium.windowFocus();
			// verifyValue|username|
			verifyEquals("", selenium.getValue("username"));
			// type|username|TestUser
			selenium.type("username", "TestUser");
			// setCursorPosition|username|0
			selenium.setCursorPosition("username", "0");
			// verifyCursorPosition|username|0
			verifyEquals("0", selenium.getCursorPosition("username"));
			// setCursorPosition|username|-1
			selenium.setCursorPosition("username", "-1");
			// verifyCursorPosition|username|8
			verifyEquals("8", selenium.getCursorPosition("username"));
			// refreshAndWait||
			selenium.refresh();
			selenium.waitForPageToLoad("5000");

			// Firefox keeps value and caret position, Safari discards it
//			boolean sawThrow14 = false;
//			try {
//			    // assertCursorPosition|username|8
//			assertEquals("8", selenium.getCursorPosition("username"));
//			}
//			catch (Throwable e) {
//				sawThrow14 = true;
//			}
//			verifyTrue(sawThrow14);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
