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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFramesSpecialTargets.html.
 */
public class TestFramesSpecialTargets extends SeleneseTestCase
{
   public void testFramesSpecialTargets() throws Throwable {
		try {
			

/* TestFramesSpecialTargets */
			// openWindow|../tests/html/Frames.html|SpecialTargets
			selenium.openWindow("/selenium-server/tests/html/Frames.html", "SpecialTargets");
			// waitForPopUp|SpecialTargets|10000
			selenium.waitForPopUp("SpecialTargets", "10000");
			// selectWindow|SpecialTargets|
			selenium.selectWindow("SpecialTargets");
			// selectFrame|bottomFrame|
			selenium.selectFrame("bottomFrame");
			// clickAndWait|changeTop|
			selenium.click("changeTop");
			selenium.waitForPageToLoad("5000");
			// click|changeSpan|
			selenium.click("changeSpan");
			// open|../tests/html/Frames.html|
			selenium.open("/selenium-server/tests/html/Frames.html");
			// selectFrame|bottomFrame|
			selenium.selectFrame("bottomFrame");
			// clickAndWait|changeParent|
			selenium.click("changeParent");
			selenium.waitForPageToLoad("5000");
			// click|changeSpan|
			selenium.click("changeSpan");
			// open|../tests/html/Frames.html|
			selenium.open("/selenium-server/tests/html/Frames.html");
			// selectFrame|bottomFrame|
			selenium.selectFrame("bottomFrame");
			// clickAndWait|changeSelf|
			selenium.click("changeSelf");
			selenium.waitForPageToLoad("5000");
			// click|changeSpan|
			selenium.click("changeSpan");
			// close||
			selenium.close();

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
