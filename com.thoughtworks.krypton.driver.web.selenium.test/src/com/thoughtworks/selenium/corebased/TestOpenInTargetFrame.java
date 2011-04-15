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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestOpenInTargetFrame.html.
 */
public class TestOpenInTargetFrame extends SeleneseTestCase
{
   public void testOpenInTargetFrame() throws Throwable {
		try {
			

/* TestOpenInTargetFrame */
			// open|../tests/html/test_open_in_target_frame.html|
			selenium.open("/selenium-server/tests/html/test_open_in_target_frame.html");
			// selectFrame|rightFrame|
			selenium.selectFrame("rightFrame");
			// click|link=Show new frame in leftFrame|
			selenium.click("link=Show new frame in leftFrame");

			/* we are forced to do a pause instead of clickandwait here,                for currently we can not detect target frame loading in ie yet */
			// pause|1500|
			pause(1500);
			assertTrue(selenium.isTextPresent("Show new frame in leftFrame"));
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// selectFrame|leftFrame|
			selenium.selectFrame("leftFrame");
			assertTrue(selenium.isTextPresent("content loaded"));
			assertTrue(!selenium.isTextPresent("This is frame LEFT"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
