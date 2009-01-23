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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestClickJavascriptHref.html.
 */
public class TestClickJavascriptHref extends SeleneseTestCase
{
   public void testClickJavascriptHref() throws Throwable {
		try {
			

/* TestClickJavaScriptHref */
			// open|../tests/html/test_click_javascript_page.html|
			selenium.open("/selenium-server/tests/html/test_click_javascript_page.html");
			// click|link|
			selenium.click("link");
			// verifyAlert|link clicked: foo|
			verifyEquals("link clicked: foo", selenium.getAlert());
			// click|linkWithMultipleJavascriptStatements|
			selenium.click("linkWithMultipleJavascriptStatements");
			// verifyAlert|alert1|
			verifyEquals("alert1", selenium.getAlert());
			// verifyAlert|alert2|
			verifyEquals("alert2", selenium.getAlert());
			// verifyAlert|alert3|
			verifyEquals("alert3", selenium.getAlert());
			// click|linkWithJavascriptVoidHref|
			selenium.click("linkWithJavascriptVoidHref");
			// verifyAlert|onclick
			verifyEquals("onclick", selenium.getAlert());
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());
			// click|linkWithOnclickReturnsFalse|
			selenium.click("linkWithOnclickReturnsFalse");
			// verifyTitle|Click Page 1|
			verifyEquals("*Click Page 1", selenium.getTitle());
			// click|enclosedImage|
			selenium.click("enclosedImage");
			// verifyAlert|enclosedImage clicked|
			verifyEquals("enclosedImage clicked", selenium.getAlert());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
