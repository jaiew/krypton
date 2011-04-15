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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestComments.html.
 */
public class TestComments extends SeleneseTestCase
{
   public void testComments() throws Throwable {
		try {
			

/* Test Comments */
			// open|../tests/html/test_verifications.html?foo=bar|
			selenium.open("/selenium-server/tests/html/test_verifications.html?foo=bar");

/* Any row with fewer than 3 cells is ignored */
			// verifyLocation|*/tests/html/test_verifications.html*||anything after the 3rd cell is ignored
			verifyEquals("*/tests/html/test_verifications.html*", selenium.getLocation());
			// verifyValue|theText|the text value
			verifyEquals("the text value", selenium.getValue("theText"));
			// verifyValue|theHidden|the hidden value
			verifyEquals("the hidden value", selenium.getValue("theHidden"));
			verifyEquals("this is the span", selenium.getText("theSpan"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
