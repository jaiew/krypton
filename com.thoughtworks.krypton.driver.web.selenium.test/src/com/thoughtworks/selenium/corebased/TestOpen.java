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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestOpen.html.
 */
public class TestOpen extends SeleneseTestCase
{
   public void testOpen() throws Throwable {
		try {
			

/* Test Open */
			// open|../tests/html/test_open.html|
			selenium.open("/selenium-server/tests/html/test_open.html");
			// verifyLocation|*/tests/html/test_open.html|
			verifyEquals("*/tests/html/test_open.html", selenium.getLocation());

			/* Should really split these verifications into their own test file. */
			// verifyLocation|regexp:.*/tests/html/[Tt]est_open.html|
			verifyEquals("regexp:.*/tests/html/[Tt]est_open.html", selenium.getLocation());
			// verifyNotLocation|*/foo.html|
			verifyNotEquals("*/foo.html", selenium.getLocation());
			assertTrue(selenium.isTextPresent("glob:This is a test of the open command."));
			assertTrue(selenium.isTextPresent("This is a test of the open command."));
			assertTrue(selenium.isTextPresent("exact:This is a test of"));
			assertTrue(selenium.isTextPresent("regexp:This is a test of"));
			assertTrue(selenium.isTextPresent("regexp:T*his is a test of"));
			assertTrue(!selenium.isTextPresent("exact:XXXXThis is a test of"));
			assertTrue(!selenium.isTextPresent("regexp:ThXXXXXXXXXis is a test of"));
			// open|../tests/html/test_page.slow.html|
			selenium.open("/selenium-server/tests/html/test_page.slow.html");
			// verifyLocation|*/tests/html/test_page.slow.html|
			verifyEquals("*/tests/html/test_page.slow.html", selenium.getLocation());
			// verifyTitle|Slow Loading Page|
			verifyEquals("*Slow Loading Page", selenium.getTitle());

			// open|../tests/html/test_open.html|
			selenium.open("/selenium-server/tests/html/test_open.html");
			// open|../tests/html/test_open.html|
			selenium.open("/selenium-server/tests/html/test_open.html");
			// open|../tests/html/test_open.html|
			selenium.open("/selenium-server/tests/html/test_open.html");

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
