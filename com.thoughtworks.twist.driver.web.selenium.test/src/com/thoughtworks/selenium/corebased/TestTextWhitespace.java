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
 * Generated from C:\svn\selenium-rc\trunk\clients\java\target\selenium-server\tests/TestTextWhitespace.html.
 */
public class TestTextWhitespace extends SeleneseTestCase
{
   public void testTextWhitespace() throws Throwable {
		try {
			

/* Test Text Content */
			// open|../tests/html/test_text_content.html|
			selenium.open("/selenium-server/tests/html/test_text_content.html");
			// verifyText|nonTextMarkup|      There is non-visible and visible markup here that doesn't change the text content
			verifyEquals("There is non-visible and visible markup here that doesn't change the text content", selenium.getText("nonTextMarkup"));

			/* Match exactly the same space characters */
//			 verifyText|spaces|exact:1 space|2  space|3   space|1nbsp|2nbsp|3nbsp|2 space_nbsp|2 nbsp_space|3  space_nbsp_space|3 nbsp_space_nbsp
			
			verifyEquals("exact:1 space|2 space|3 space|1 nbsp|2  nbsp|3   nbsp|2  space_nbsp|2  nbsp_space|3   space_nbsp_space|3   nbsp_space_nbsp", selenium.getText("spaces"));
			// verifyText|tabcharacter|tab character between
			verifyEquals("tab character between", selenium.getText("tabcharacter"));
			// verifyText|nonVisibleNewlines|non visible newlines between
			verifyEquals("non visible newlines between", selenium.getText("nonVisibleNewlines"));
			// verifyText|visibleNewlines|regexp:visible\\s*newlines\\s*between
			verifyEquals("regexp:visible\\s*newlines\\s*between", selenium.getText("visibleNewlines"));
			// verifyNotText|visibleNewlines|visible newlines between
			verifyNotEquals("visible newlines between", selenium.getText("visibleNewlines"));
			// verifyText|paragraphs|First paragraph*Second paragraph
			verifyEquals("First paragraph*Second paragraph", selenium.getText("paragraphs"));
			// verifyNotText|paragraphs|First paragraph Second paragraph
			verifyNotEquals("First paragraph Second paragraph", selenium.getText("paragraphs"));
			// verifyText|preformatted|preformatted*newline
			verifyEquals("preformatted*newline", selenium.getText("preformatted"));
			// verifyNotText|preformatted|preformatted newline
			verifyNotEquals("preformatted newline", selenium.getText("preformatted"));
			// verifyText|mixedMarkup|visible*newlines and markup and non-visible newlines and markup*With*a paragraph*and*pre*formatted*text
			verifyEquals("visible*newlines and markup and non-visible newlines and markup*With*a paragraph*and*pre*formatted*text", selenium.getText("mixedMarkup"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
