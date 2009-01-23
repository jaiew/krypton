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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestElementIndex.html.
 */
public class TestElementIndex extends SeleneseTestCase
{
   public void testElementIndex() throws Throwable {
		try {
			

/* TestElementIndex */
			// open|../tests/html/test_element_order.html|
			selenium.open("/selenium-server/tests/html/test_element_order.html");
			// assertElementIndex|d2|1
			assertEquals("1", selenium.getElementIndex("d2"));
			// assertElementIndex|d1.1.1|0
			assertEquals("0", selenium.getElementIndex("d1.1.1"));
			// verifyElementIndex|d2|1
			verifyEquals("1", selenium.getElementIndex("d2"));
			// verifyElementIndex|d1.2|5
			verifyEquals("5", selenium.getElementIndex("d1.2"));
			// assertNotElementIndex|d2|2
			assertNotEquals("2", selenium.getElementIndex("d2"));
			// verifyNotElementIndex|d2|2
			verifyNotEquals("2", selenium.getElementIndex("d2"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
