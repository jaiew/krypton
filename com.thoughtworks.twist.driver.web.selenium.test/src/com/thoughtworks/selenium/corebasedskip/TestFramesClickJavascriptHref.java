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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFramesClickJavascriptHref.html.
 */
public class TestFramesClickJavascriptHref extends SeleneseTestCase
{
   public void testFramesClickJavascriptHref() throws Throwable {
		try {
			

/* TestFramesClickJavaScriptHrefInWrongFrame */
			// open|../tests/html/Frames.html|
			selenium.open("/selenium-server/tests/html/Frames.html");
			// selectFrame|mainFrame|
			selenium.selectFrame("mainFrame");
			// open|../tests/html/test_click_javascript_page.html|
			selenium.open("/selenium-server/tests/html/test_click_javascript_page.html");
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// click|link|
			selenium.click("link");
			// verifyAlert|link clicked: foo|
			verifyEquals("link clicked: foo", selenium.getAlert());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
