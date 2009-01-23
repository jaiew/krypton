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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestPrompt.html.
 */
public class TestPrompt extends SeleneseTestCase
{
   public void testPrompt() throws Throwable {
		try {
			

/* Test verify Prompting */
			// open|../tests/html/test_prompt.html|
			selenium.open("/selenium-server/tests/html/test_prompt.html");
			// verifyPromptNotPresent||
			assertTrue(!selenium.isPromptPresent());
			// assertPromptNotPresent||
			assertTrue(!selenium.isPromptPresent());
			// answerOnNextPrompt|no|
			selenium.answerOnNextPrompt("no");
			// click|promptAndLeave|
			selenium.click("promptAndLeave");
			// verifyPromptPresent||
			assertTrue(selenium.isPromptPresent());
			boolean sawCondition9 = false;
			for (int second = 0; second < 60; second++) {
				try {
					if ((selenium.isPromptPresent())) {
						sawCondition9 = true;
						break;
					}
				}
				catch (Exception ignore) {
				}
				pause(1000);
			}
			assertTrue(sawCondition9);
			
			// assertPromptPresent||
			assertTrue(selenium.isPromptPresent());
			// verifyPrompt|Type 'yes' and click OK|
			verifyEquals("Type 'yes' and click OK", selenium.getPrompt());
			// verifyTitle|Test Prompt|
			verifyEquals("*Test Prompt", selenium.getTitle());
			// answerOnNextPrompt|yes|
			selenium.answerOnNextPrompt("yes");
			// clickAndWait|promptAndLeave|
			selenium.click("promptAndLeave");
			selenium.waitForPageToLoad("5000");
			// verifyPrompt|*'yes'*|
			verifyEquals("*'yes'*", selenium.getPrompt());
			// verifyTitle|Dummy Page|
			verifyEquals("*Dummy Page", selenium.getTitle());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
