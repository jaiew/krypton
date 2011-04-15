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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestConfirmations.html.
 */
public class TestConfirmations extends SeleneseTestCase
{
   public void testConfirmations() throws Throwable {
		try {
			

/* Test verify Confirmation */
			// open|../tests/html/test_confirm.html|
			selenium.open("/selenium-server/tests/html/test_confirm.html");
			// chooseCancelOnNextConfirmation||
			selenium.chooseCancelOnNextConfirmation();
			// click|confirmAndLeave|
			selenium.click("confirmAndLeave");
			// verifyConfirmationPresent||
			assertTrue(selenium.isConfirmationPresent());
			boolean sawCondition7 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((selenium.isConfirmationPresent())) {
						sawCondition7 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
			}
			assertTrue(sawCondition7);
			
			// assertConfirmationPresent||
			assertTrue(selenium.isConfirmationPresent());
			// verifyConfirmation|You are about to go to a dummy page.|

			verifyEquals("You are about to go to a dummy page.", selenium.getConfirmation());
			// verifyTitle|Test Confirm|
			verifyEquals("*Test Confirm", selenium.getTitle());
			// clickAndWait|confirmAndLeave|
			selenium.click("confirmAndLeave");
			selenium.waitForPageToLoad("5000");
			// verifyConfirmation|*dummy page*|
			verifyEquals("*dummy page*", selenium.getConfirmation());
			// verifyTitle|Dummy Page|
			verifyEquals("*Dummy Page", selenium.getTitle());
			// open|../tests/html/test_confirm.html|
			selenium.open("/selenium-server/tests/html/test_confirm.html");
			// verifyTitle|Test Confirm|
			verifyEquals("*Test Confirm", selenium.getTitle());
			// chooseCancelOnNextConfirmation||
			selenium.chooseCancelOnNextConfirmation();
			// chooseOkOnNextConfirmation||
			selenium.chooseOkOnNextConfirmation();
			// clickAndWait|confirmAndLeave|
			selenium.click("confirmAndLeave");
			selenium.waitForPageToLoad("5000");
			// verifyConfirmation|*dummy page*|
			verifyEquals("*dummy page*", selenium.getConfirmation());
			// verifyTitle|Dummy Page|
			verifyEquals("*Dummy Page", selenium.getTitle());
			// open|../tests/html/test_confirm.html|
			selenium.open("/selenium-server/tests/html/test_confirm.html");

			boolean sawThrow22 = false;
			try {
							// assertConfirmation|This should fail - there are no confirmations|
			assertEquals("This should fail - there are no confirmations", selenium.getConfirmation());
			}
			catch (Throwable e) {
				sawThrow22 = true;
			}
			verifyTrue(sawThrow22);
			
			// clickAndWait|confirmAndLeave|
			selenium.click("confirmAndLeave");
			selenium.waitForPageToLoad("5000");

			boolean sawThrow25 = false;
			try {
							// assertConfirmation|this should fail - wrong confirmation|
			assertEquals("this should fail - wrong confirmation", selenium.getConfirmation());
			}
			catch (Throwable e) {
				sawThrow25 = true;
			}
			verifyTrue(sawThrow25);
			
			// open|../tests/html/test_confirm.html|
			selenium.open("/selenium-server/tests/html/test_confirm.html");
			// clickAndWait|confirmAndLeave|
			selenium.click("confirmAndLeave");
			selenium.waitForPageToLoad("5000");

			boolean sawThrow29 = false;
			try {
							// open|../tests/html/test_confirm.html|
			selenium.open("/selenium-server/tests/html/test_confirm.html");
			}
			catch (Throwable e) {
				sawThrow29 = true;
			}
			assertTrue(sawThrow29);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
