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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestMultiSelect.html.
 */
public class TestMultiSelect extends SeleneseTestCase
{
   public void testMultiSelect() throws Throwable {
		try {
			

/* Test Multiple Select */
			// open|../tests/html/test_multiselect.html|
			selenium.open("/selenium-server/tests/html/test_multiselect.html");
			String[] tmp5 = {"Second Option"};
			// assertSelectedLabels|theSelect|Second Option
			assertEquals(tmp5, selenium.getSelectedLabels("theSelect"));
			// select|theSelect|index=4
			selenium.select("theSelect", "index=4");
			String[] tmp6 = {"Fifth Option"};
			// verifySelectedLabels|theSelect|Fifth Option
			verifyEquals(tmp6, selenium.getSelectedLabels("theSelect"));
			// addSelection|theSelect|Third Option
			selenium.addSelection("theSelect", "Third Option");
			// addSelection|theSelect|value=
			selenium.addSelection("theSelect", "value=");
			String[] tmp7 = {"Third Option", "Fifth Option", "Empty Value Option"};
			// verifySelectedLabels|theSelect|Third Option,Fifth Option,Empty Value Option
			verifyEquals(tmp7, selenium.getSelectedLabels("theSelect"));
			// removeSelection|theSelect|id=o7
			selenium.removeSelection("theSelect", "id=o7");
			String[] tmp8 = {"Third Option", "Fifth Option"};
			// verifySelectedLabels|theSelect|Third Option,Fifth Option
			verifyEquals(tmp8, selenium.getSelectedLabels("theSelect"));
			// removeSelection|theSelect|label=Fifth Option
			selenium.removeSelection("theSelect", "label=Fifth Option");

			boolean sawThrow13 = false;
			try {
				// originally verifySelected|theSelect|Third Option
						assertEquals("Third Option", selenium.getSelectedLabel("theSelect"));
			}
			catch (Throwable e) {
				sawThrow13 = true;
			}
			verifyFalse(sawThrow13);
			
			// addSelection|theSelect|
			selenium.addSelection("theSelect", "");
			String[] tmp9 = {"Third Option", ""};
			// verifySelectedLabels|theSelect|Third Option,
			verifyEquals(tmp9, selenium.getSelectedLabels("theSelect"));
			// removeSelection|theSelect|
			selenium.removeSelection("theSelect", "");
			// removeSelection|theSelect|Third Option
			selenium.removeSelection("theSelect", "Third Option");

			boolean sawThrow18 = false;
			try {
							// assertSelected|theSelect|
			fail("No option selected");
			}
			catch (Throwable e) {
				sawThrow18 = true;
			}
			verifyTrue(sawThrow18);
			

			boolean sawThrow20 = false;
			try {
							String[] tmp10 = {""};
			// assertSelectedLabels|theSelect|
			assertEquals(tmp10, selenium.getSelectedLabels("theSelect"));
			}
			catch (Throwable e) {
				sawThrow20 = true;
			}
			verifyTrue(sawThrow20);
			
			// verifyValue|theSelect|
			verifyEquals("", selenium.getValue("theSelect"));
			// verifyNotSomethingSelected|theSelect|
			try {selenium.getSelectedIndexes("theSelect");} catch(Throwable e) {}
			// addSelection|theSelect|Third Option
			selenium.addSelection("theSelect", "Third Option");
			// addSelection|theSelect|value=
			selenium.addSelection("theSelect", "value=");
			// removeAllSelections|theSelect|
			selenium.removeAllSelections("theSelect");
			// verifyNotSomethingSelected|theSelect|
			try {selenium.getSelectedIndexes("theSelect");} catch(Throwable e) {}

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
