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
import com.thoughtworks.selenium.SeleneseTestCase;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestSelect.html.
 */
public class TestSelect extends SeleneseTestCase
{
   public void testSelect() throws Throwable {
		try {
			

/* Test Select */
			// open|../tests/html/test_select.html|
			selenium.open("/selenium-server/tests/html/test_select.html");
			// assertSomethingSelected|theSelect|
			
			assertTrue(selenium.getSelectedIndexes("theSelect").length != 0);
			assertEquals("Second Option", selenium.getSelectedLabel("theSelect"));
			// select|theSelect|index=4
			selenium.select("theSelect", "index=4");
			// verifySelectedLabel|theSelect|Fifth Option
			verifyEquals("Fifth Option", selenium.getSelectedLabel("theSelect"));

			boolean sawThrow8 = false;
			try {
				// originally verifySelected|theSelect|index=4
						assertEquals("4", selenium.getSelectedIndex("theSelect"));
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			verifyFalse(sawThrow8);
			
			// verifySelectedLabel|theSelect|Fifth Option
			verifyEquals("Fifth Option", selenium.getSelectedLabel("theSelect"));
			String[] tmp13 = {"Fifth Option"};
			// verifySelectedLabels|theSelect|Fifth Option
			verifyEquals(tmp13, selenium.getSelectedLabels("theSelect"));
			// select|theSelect|Third Option
			selenium.select("theSelect", "Third Option");

			boolean sawThrow12 = false;
			try {
				// originally verifySelected|theSelect|Third Option
						assertEquals("Third Option", selenium.getSelectedLabel("theSelect"));
			}
			catch (Throwable e) {
				sawThrow12 = true;
			}
			verifyFalse(sawThrow12);
			

			boolean sawThrow13 = false;
			try {
				// originally verifySelected|theSelect|label=Third Option
						assertEquals("Third Option", selenium.getSelectedLabel("theSelect"));
			}
			catch (Throwable e) {
				sawThrow13 = true;
			}
			verifyFalse(sawThrow13);
			
			// verifySelectedLabel|theSelect|Third Option
			verifyEquals("Third Option", selenium.getSelectedLabel("theSelect"));
			// select|theSelect|label=Fourth Option
			selenium.select("theSelect", "label=Fourth Option");
			// verifySelectedLabel|theSelect|Fourth Option
			verifyEquals("Fourth Option", selenium.getSelectedLabel("theSelect"));

			boolean sawThrow17 = false;
			try {
				// originally verifySelected|theSelect|Fourth Option
						assertEquals("Fourth Option", selenium.getSelectedLabel("theSelect"));
			}
			catch (Throwable e) {
				sawThrow17 = true;
			}
			verifyFalse(sawThrow17);
			
			// select|theSelect|value=option6
			selenium.select("theSelect", "value=option6");
			// verifySelectedLabel|theSelect|Sixth Option
			verifyEquals("Sixth Option", selenium.getSelectedLabel("theSelect"));
			// verifySelectedValue|theSelect|option6
			verifyEquals("option6", selenium.getSelectedValue("theSelect"));

			boolean sawThrow21 = false;
			try {
				// originally verifySelected|theSelect|value=option6
						assertEquals("option6", selenium.getSelectedValue("theSelect"));
			}
			catch (Throwable e) {
				sawThrow21 = true;
			}
			verifyFalse(sawThrow21);
			
			// select|theSelect|value=
			selenium.select("theSelect", "value=");
			// verifySelectedLabel|theSelect|Empty Value Option
			verifyEquals("Empty Value Option", selenium.getSelectedLabel("theSelect"));
			// select|theSelect|id=o4
			selenium.select("theSelect", "id=o4");
			// verifySelectedLabel|theSelect|Fourth Option
			verifyEquals("Fourth Option", selenium.getSelectedLabel("theSelect"));
			// verifySelectedId|theSelect|o4
			verifyEquals("o4", selenium.getSelectedId("theSelect"));
			// select|theSelect|
			selenium.select("theSelect", "");
			// verifySelectedLabel|theSelect|
			verifyEquals("", selenium.getSelectedLabel("theSelect"));
			String[] tmp14 = {""};
			// verifySelectedLabels|theSelect|
			verifyEquals(tmp14, selenium.getSelectedLabels("theSelect"));

			boolean sawThrow30 = false;
			try {
							// select|theSelect|Not an option
			selenium.select("theSelect", "Not an option");
			}
			catch (Throwable e) {
				sawThrow30 = true;
			}
			assertTrue(sawThrow30);
			

			boolean sawThrow32 = false;
			try {
							// addSelection|theSelect|Fourth Option
			selenium.addSelection("theSelect", "Fourth Option");
			}
			catch (Throwable e) {
				sawThrow32 = true;
			}
			assertTrue(sawThrow32);
			

			boolean sawThrow34 = false;
			try {
							// removeSelection|theSelect|Fourth Option
			selenium.removeSelection("theSelect", "Fourth Option");
			}
			catch (Throwable e) {
				sawThrow34 = true;
			}
			assertTrue(sawThrow34);
			
			String[] tmp15 = {"First Option", "Second Option", "Third Option", "Fourth Option", "Fifth Option", "Sixth Option", "Empty Value Option", ""};
			// verifySelectOptions|theSelect|First Option,Second Option,Third Option,Fourth Option,Fifth Option,Sixth Option,Empty Value Option,
			verifyEquals(tmp15, selenium.getSelectOptions("theSelect"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
