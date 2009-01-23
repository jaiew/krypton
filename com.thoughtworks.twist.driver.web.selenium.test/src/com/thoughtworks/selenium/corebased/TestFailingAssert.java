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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFailingAssert.html.
 */
public class TestFailingAssert extends SeleneseTestCase
{
   public void testFailingAssert() throws Throwable {
		try {
			

/* Test Failing Assert */
			// open|../tests/html/test_verifications.html|
			selenium.open("/selenium-server/tests/html/test_verifications.html");

			boolean sawThrow4 = false;
			try {
							// assertValue|theText|not the text value
			assertEquals("not the text value", selenium.getValue("theText"));
			}
			catch (Throwable e) {
				sawThrow4 = true;
			}
			assertTrue(sawThrow4);
			

			boolean sawThrow6 = false;
			try {
							// assertNotValue|theText|the text value
			assertNotEquals("the text value", selenium.getValue("theText"));
			}
			catch (Throwable e) {
				sawThrow6 = true;
			}
			assertTrue(sawThrow6);
			

			boolean sawThrow8 = false;
			try {
							// assertValue|theTable|x
			assertEquals("x", selenium.getValue("theTable"));
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			assertTrue(sawThrow8);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
