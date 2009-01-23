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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestAlerts.html.
 */
public class TestAlerts extends SeleneseTestCase
{
   public void testAlerts() throws Throwable {
		try {
			

/* Test Alert verifyment */
			// open|../tests/html/test_verify_alert.html|
			selenium.open("/selenium-server/tests/html/test_verify_alert.html");
			// verifyAlertNotPresent||
			assertTrue(!selenium.isAlertPresent());
			// assertAlertNotPresent||
			assertTrue(!selenium.isAlertPresent());
			// click|oneAlert|
			selenium.click("oneAlert");
			// verifyAlertPresent||
			assertTrue(selenium.isAlertPresent());
			boolean sawCondition8 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((selenium.isAlertPresent())) {
						sawCondition8 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
			}
			assertTrue(sawCondition8);
			
			// assertAlertPresent||
			assertTrue(selenium.isAlertPresent());
			// verifyAlert|Store Below 494 degrees K!|
			verifyEquals("Store Below 494 degrees K!", selenium.getAlert());
			// click|multipleLineAlert|
			selenium.click("multipleLineAlert");
			// verifyAlert|This alert spans multiple lines|
			verifyEquals("This alert spans\nmultiple lines", selenium.getAlert());
			// click|oneAlert|
			selenium.click("oneAlert");
			// storeAlert|myVar|
			String myVar = selenium.getAlert();
			// verifyExpression|${myVar}|Store Below 494 degrees K!
			verifyEquals(myVar, "Store Below 494 degrees K!");
			// click|twoAlerts|
			selenium.click("twoAlerts");
			// verifyAlert|* 220 degrees C!|
			verifyEquals("* 220 degrees C!", selenium.getAlert());
			// verifyAlert|regexp:^Store Below 429 degrees F!|
			verifyEquals("regexp:^Store Below 429 degrees F!", selenium.getAlert());
			// clickAndWait|alertAndLeave|
			selenium.click("alertAndLeave");
			selenium.waitForPageToLoad("30000");
			// verifyAlert|I'm Melting! I'm Melting!|
			verifyEquals("I'm Melting! I'm Melting!", selenium.getAlert());
			// open|../tests/html/test_verify_alert.html|
			selenium.open("/selenium-server/tests/html/test_verify_alert.html");

			boolean sawThrow22 = false;
			try {
							// assertAlert|noAlert|
			assertEquals("noAlert", selenium.getAlert());
			}
			catch (Throwable e) {
				sawThrow22 = true;
			}
			verifyTrue(sawThrow22);
			
			// click|oneAlert|
			selenium.click("oneAlert");

			boolean sawThrow25 = false;
			try {
							// assertAlert|wrongAlert|
			assertEquals("wrongAlert", selenium.getAlert());
			}
			catch (Throwable e) {
				sawThrow25 = true;
			}
			verifyTrue(sawThrow25);
			
			// click|twoAlerts|
			selenium.click("twoAlerts");

			boolean sawThrow28 = false;
			try {
							// assertAlert|Store Below 429 degrees F!|
			assertEquals("Store Below 429 degrees F!", selenium.getAlert());
			}
			catch (Throwable e) {
				sawThrow28 = true;
			}
			verifyTrue(sawThrow28);
			

			boolean sawThrow30 = false;
			try {
							// assertAlert|Store Below 220 degrees C!|
			assertEquals("Store Below 220 degrees C!", selenium.getAlert());
			}
			catch (Throwable e) {
				sawThrow30 = true;
			}
			verifyTrue(sawThrow30);
			
			// click|oneAlert|
			selenium.click("oneAlert");

			boolean sawThrow33 = false;
			try {
							// open|../tests/html/test_assert_alert.html|
			selenium.open("/selenium-server/tests/html/test_assert_alert.html");
			}
			catch (Throwable e) {
				sawThrow33 = true;
			}
			assertTrue(sawThrow33);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
