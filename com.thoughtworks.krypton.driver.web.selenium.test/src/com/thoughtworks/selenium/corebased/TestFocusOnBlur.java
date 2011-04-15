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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFocusOnBlur.html.
 */
public class TestFocusOnBlur extends SeleneseTestCase
{
   public void testFocusOnBlur() throws Throwable {
		try {
			

/* Test Focus On Blur */
			// open|../tests/html/test_focus_on_blur.html|
			selenium.open("/selenium-server/tests/html/test_focus_on_blur.html");
			// type|testInput|test
			selenium.type("testInput", "test");
			// fireEvent|testInput|blur
			selenium.fireEvent("testInput", "blur");
			// verifyAlert|Bad value|
			verifyEquals("Bad value", selenium.getAlert());
			// type|testInput|somethingelse
			selenium.type("testInput", "somethingelse");

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
