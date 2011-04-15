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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestType.html.
 */
public class TestType extends SeleneseTestCase
{
   public void testType() throws Throwable {
		try {
			

/* Test Type */
			// open|../tests/html/test_type_page1.html|
			selenium.open("/selenium-server/tests/html/test_type_page1.html");
			// verifyValue|username|
			verifyEquals("", selenium.getValue("username"));
			// shiftKeyDown||
			selenium.shiftKeyDown();
			// type|username|x
			selenium.type("username", "x");
			// verifyValue|username|X
			verifyEquals("X", selenium.getValue("username"));
			// shiftKeyUp||
			selenium.shiftKeyUp();
			// type|username|TestUserWithLongName
			selenium.type("username", "TestUserWithLongName");
			// verifyValue|username|TestUserWi
			verifyEquals("TestUserWi", selenium.getValue("username"));
			// type|username|TestUser
			selenium.type("username", "TestUser");
			// verifyValue|username|TestUser
			verifyEquals("TestUser", selenium.getValue("username"));
			// verifyValue|password|
			verifyEquals("", selenium.getValue("password"));
			// type|password|testUserPasswordIsVeryLong
			selenium.type("password", "testUserPasswordIsVeryLong");
			// verifyValue|password|testUserPasswordIsVe
			verifyEquals("testUserPasswordIsVe", selenium.getValue("password"));
			// type|password|testUserPassword
			selenium.type("password", "testUserPassword");
			// verifyValue|password|testUserPassword
			verifyEquals("testUserPassword", selenium.getValue("password"));
			// clickAndWait|submitButton|
			selenium.click("submitButton");
			selenium.waitForPageToLoad("5000");
			assertTrue(selenium.isTextPresent("Welcome, TestUser!"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
