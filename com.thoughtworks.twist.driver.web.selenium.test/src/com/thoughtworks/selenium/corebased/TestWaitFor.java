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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestWaitFor.html.
 */
public class TestWaitFor extends SeleneseTestCase
{
   public void testWaitFor() throws Throwable {
		try {
			

/* Test WaitForValue */
			// open|../tests/html/test_async_event.html|
			selenium.open("/selenium-server/tests/html/test_async_event.html");
			// assertValue|theField|oldValue
			assertEquals("oldValue", selenium.getValue("theField"));
			// click|theButton|
			// assertValue|theField|oldValue
			assertEquals("oldValue", selenium.getValue("theField"));
			selenium.click("theButton");
			boolean sawCondition7 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (seleniumEquals("regexp:n[aeiou]wValue", selenium.getValue("theField"))) {
						sawCondition7 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
				waitForActivity();
			}
			assertTrue(sawCondition7);
			
			// verifyValue|theField|newValue
			verifyEquals("newValue", selenium.getValue("theField"));
			// assertText|theSpan|Some text
			assertEquals("Some text", selenium.getText("theSpan"));
			// click|theSpanButton|
			selenium.click("theSpanButton");
			// assertText|theSpan|Some text
//			assertEquals("Some text", selenium.getText("theSpan"));
			boolean sawCondition12 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (seleniumEquals("regexp:Some n[aeiou]w text", selenium.getText("theSpan"))) {
						sawCondition12 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
//                waitForActivity();
				pause(1000);
			}
			assertTrue(sawCondition12);
			
			// verifyText|theSpan|Some new text
			verifyEquals("Some new text", selenium.getText("theSpan"));
			// click|theAlertButton|
			selenium.click("theAlertButton");
			boolean sawCondition15 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (seleniumEquals("regexp:An [aeiou]lert", selenium.getAlert())) {
						sawCondition15 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
                waitForActivity();
				pause(1000);
			}
			assertTrue(sawCondition15);
			
			// open|../tests/html/test_reload_onchange_page.html|
			selenium.open("/selenium-server/tests/html/test_reload_onchange_page.html");
			// click|theLink|
			selenium.click("theLink");
			boolean sawCondition18 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (seleniumEquals("*Slow Loading Page", selenium.getTitle())) {
						sawCondition18 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
			}
			assertTrue(sawCondition18);
			
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());


			boolean sawThrow21 = false;
			try {
							boolean sawCondition22 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((selenium.isTextPresent("thisTextIsNotPresent"))) {
						sawCondition22 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
			}
			assertTrue(sawCondition22);
			
			}
			catch (Throwable e) {
				sawThrow21 = true;
			}
			assertTrue(sawThrow21);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
