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
package com.thoughtworks.selenium.corebasedskip;
import com.thoughtworks.selenium.*;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestJavaScriptAttributes.html.
 */
public class TestJavaScriptAttributes extends SeleneseTestCase
{
   public void testJavaScriptAttributes() throws Throwable {
		try {
			

/* TestJavaScriptAttributes */
			// open|../tests/html/test_javascript_attributes.html|
			selenium.open("/selenium-server/tests/html/test_javascript_attributes.html");
			// click|//a[@onclick="alert('foo')"]|
			selenium.click("//a[@onclick=\"alert('foo')\"]");
			// assertAlert|foo|
			assertEquals("foo", selenium.getAlert());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
