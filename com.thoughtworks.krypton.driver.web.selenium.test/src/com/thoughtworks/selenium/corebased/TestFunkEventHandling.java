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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFunkEventHandling.html.
 */
public class TestFunkEventHandling extends SeleneseTestCase
{
   public void testFunkEventHandling() throws Throwable {
		try {
			

/* Test Funky Event Handling */
			// open|../tests/html/test_funky_event_handling.html|
			selenium.open("/selenium-server/tests/html/test_funky_event_handling.html");
			// click|clickMe|
			selenium.click("clickMe");
			// pause|1000|
			pause(1000);
			assertTrue(!selenium.isTextPresent("You shouldn't be here!"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
