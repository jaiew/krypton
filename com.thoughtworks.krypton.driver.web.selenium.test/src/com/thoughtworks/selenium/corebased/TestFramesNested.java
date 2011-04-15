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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestFramesNested.html.
 */
public class TestFramesNested extends SeleneseTestCase
{
   public void testFramesNested() throws Throwable {
		try {
			

/* TestFramesNested */
			// open|../tests/html/NestedFrames.html|
			selenium.open("/selenium-server/tests/html/NestedFrames.html");
			// verifyTitle|NestedFrames|
			verifyEquals("*NestedFrames", selenium.getTitle());
			assertTrue(!selenium.isTextPresent("This is a test"));
			// selectFrame|mainFrame|
			selenium.selectFrame("mainFrame");
			// verifyTitle|NestedFrames2|
			verifyEquals("*NestedFrames2", selenium.getTitle());
			// selectFrame|mainFrame|
			selenium.selectFrame("mainFrame");
			// verifyTitle|AUT|
			verifyEquals("*AUT", selenium.getTitle());
			// selectFrame|mainFrame|
			selenium.selectFrame("mainFrame");
			// verifyLocation|*/tests/html/test_open.html|
			verifyEquals("*/tests/html/test_open.html", selenium.getLocation());
			assertTrue(selenium.isTextPresent("This is a test"));
			// selectFrame|relative=up|
			selenium.selectFrame("relative=up");
			// verifyTitle|AUT|
			verifyEquals("*AUT", selenium.getTitle());
			assertTrue(!selenium.isTextPresent("This is a test"));
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// verifyTitle|NestedFrames|
			verifyEquals("*NestedFrames", selenium.getTitle());
			// selectFrame|dom=window.frames[1]|
			selenium.selectFrame("dom=window.frames[1]");
			// verifyTitle|NestedFrames2|
			verifyEquals("*NestedFrames2", selenium.getTitle());
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// verifyTitle|NestedFrames|
			verifyEquals("*NestedFrames", selenium.getTitle());
			// selectFrame|index=1|
			selenium.selectFrame("index=1");
			// verifyTitle|NestedFrames2|
			verifyEquals("*NestedFrames2", selenium.getTitle());
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// verifyTitle|NestedFrames|
			verifyEquals("*NestedFrames", selenium.getTitle());
			// selectFrame|foo|
			selenium.selectFrame("foo");
			// verifyTitle|NestedFrames2|
			verifyEquals("*NestedFrames2", selenium.getTitle());
			// selectFrame|relative=top|
			selenium.selectFrame("relative=top");
			// verifyTitle|NestedFrames|
			verifyEquals("*NestedFrames", selenium.getTitle());
			// selectFrame|dom=window.frames["mainFrame"].frames["mainFrame"]|
			selenium.selectFrame("dom=window.frames[\"mainFrame\"].frames[\"mainFrame\"]");
			// verifyTitle|AUT|
			verifyEquals("*AUT", selenium.getTitle());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
