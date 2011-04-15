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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestElementOrder.html.
 */
public class TestElementOrder extends SeleneseTestCase
{
   public void testElementOrder() throws Throwable {
		try {
			

/* TestElementOrder */
			// open|../tests/html/test_element_order.html|
			selenium.open("/selenium-server/tests/html/test_element_order.html");
			// assertOrdered|s1.1|d1.1
			assertEquals(true, selenium.isOrdered("s1.1", "d1.1"));
			// assertNotOrdered|s1.1|s1.1
			assertNotEquals(true, selenium.isOrdered("s1.1", "s1.1"));
			// verifyOrdered|s1.1|d1.1
			verifyEquals(true, selenium.isOrdered("s1.1", "d1.1"));
			// assertNotOrdered|d1.1|s1.1
			assertNotEquals(true, selenium.isOrdered("d1.1", "s1.1"));
			// verifyNotOrdered|s1.1|d2
			verifyNotEquals(true, selenium.isOrdered("s1.1", "d2"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
