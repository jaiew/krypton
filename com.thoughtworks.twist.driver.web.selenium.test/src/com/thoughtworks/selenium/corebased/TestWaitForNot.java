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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestWaitForNot.html.
 */
public class TestWaitForNot extends SeleneseTestCase
{
   public void testWaitForNot() throws Throwable {
		try {
			

/* Test WaitForValueNot */
			// open|../tests/html/test_async_event.html|
			selenium.open("/selenium-server/tests/html/test_async_event.html");
			// assertValue|theField|oldValue
			assertEquals("oldValue", selenium.getValue("theField"));
			// click|theButton|
			selenium.click("theButton");
			// assertValue|theField|oldValue
			assertEquals("oldValue", selenium.getValue("theField"));
			boolean sawCondition7 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (!seleniumEquals("regexp:oldValu[aei]", selenium.getValue("theField"))) {
						sawCondition7 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				waitForActivity();
				pause(1000);
			}
			assertTrue(sawCondition7);
			
			// verifyValue|theField|newValue
			verifyEquals("newValue", selenium.getValue("theField"));
			// assertText|theSpan|Some text
			assertEquals("Some text", selenium.getText("theSpan"));
			// click|theSpanButton|
			selenium.click("theSpanButton");
			// assertText|theSpan|Some text
			assertEquals("Some text", selenium.getText("theSpan"));
			boolean sawCondition12 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if (!seleniumEquals("regexp:Some te[xyz]t", selenium.getText("theSpan"))) {
						sawCondition12 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
                waitForActivity();
				pause(1000);
			}
			assertTrue(sawCondition12);
			
			// verifyText|theSpan|Some new text
			verifyEquals("Some new text", selenium.getText("theSpan"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
