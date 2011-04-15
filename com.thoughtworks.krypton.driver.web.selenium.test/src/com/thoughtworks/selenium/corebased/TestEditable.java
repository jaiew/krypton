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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestEditable.html.
 */
public class TestEditable extends SeleneseTestCase
{
   public void testEditable() throws Throwable {
		try {
			

/* Test verifyEditable */
			// open|../tests/html/test_editable.html|
			selenium.open("/selenium-server/tests/html/test_editable.html");

			boolean sawThrow4 = false;
			try {
				// originally verifyEditable|normal_text|
						assertTrue(selenium.isEditable("normal_text"));
			}
			catch (Throwable e) {
				sawThrow4 = true;
			}
			verifyFalse(sawThrow4);
			

			boolean sawThrow5 = false;
			try {
				// originally verifyEditable|normal_select|
						assertTrue(selenium.isEditable("normal_select"));
			}
			catch (Throwable e) {
				sawThrow5 = true;
			}
			verifyFalse(sawThrow5);
			

			boolean sawThrow6 = false;
			try {
				// originally verifyNotEditable|disabled_text|
						assertTrue(!selenium.isEditable("disabled_text"));
			}
			catch (Throwable e) {
				sawThrow6 = true;
			}
			verifyFalse(sawThrow6);
			

			boolean sawThrow7 = false;
			try {
				// originally verifyNotEditable|disabled_select|
						assertTrue(!selenium.isEditable("disabled_select"));
			}
			catch (Throwable e) {
				sawThrow7 = true;
			}
			verifyFalse(sawThrow7);
			

			boolean sawThrow8 = false;
			try {
				// originally verifyNotEditable|readonly_text|
						assertTrue(!selenium.isEditable("readonly_text"));
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			verifyFalse(sawThrow8);
			

			boolean sawThrow9 = false;
			try {
							assertTrue(!selenium.isEditable("normal_text"));
			}
			catch (Throwable e) {
				sawThrow9 = true;
			}
			verifyTrue(sawThrow9);
			

			boolean sawThrow11 = false;
			try {
							assertTrue(!selenium.isEditable("normal_select"));
			}
			catch (Throwable e) {
				sawThrow11 = true;
			}
			verifyTrue(sawThrow11);
			

			boolean sawThrow13 = false;
			try {
							assertTrue(selenium.isEditable("disabled_text"));
			}
			catch (Throwable e) {
				sawThrow13 = true;
			}
			verifyTrue(sawThrow13);
			

			boolean sawThrow15 = false;
			try {
							assertTrue(selenium.isEditable("disabled_select"));
			}
			catch (Throwable e) {
				sawThrow15 = true;
			}
			verifyTrue(sawThrow15);
			

			boolean sawThrow17 = false;
			try {
							assertTrue(selenium.isEditable("fake_input"));
			}
			catch (Throwable e) {
				sawThrow17 = true;
			}
			verifyTrue(sawThrow17);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
