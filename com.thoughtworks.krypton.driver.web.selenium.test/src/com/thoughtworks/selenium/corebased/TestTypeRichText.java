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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestTypeRichText.html.
 */
public class TestTypeRichText extends SeleneseTestCase
{
   public void testTypeRichText() throws Throwable {
		try {
			

/* TestTypeRichText */
			// open|../tests/html/test_rich_text.html|
			selenium.open("/selenium-server/tests/html/test_rich_text.html");
			// selectFrame|richtext|
//			selenium.selectFrame("richtext");
          selenium.selectFrame("dom=document.getElementsByTagName('iframe')[0]");
			// verifyText|//body|
//			verifyEquals("", selenium.getText("//body"));
			// type|//body|hello world
			selenium.type("//body", "hello world");
			// verifyText|//body|hello world
			verifyEquals("hello world", selenium.getText("//body"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
