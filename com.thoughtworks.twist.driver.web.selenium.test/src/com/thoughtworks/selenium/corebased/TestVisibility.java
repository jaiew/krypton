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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestVisibility.html.
 */
public class TestVisibility extends SeleneseTestCase
{
   public void testVisibility() throws Throwable {
		try {
			

/* Test Visiblity */
			// open|../tests/html/test_visibility.html|
			selenium.open("/selenium-server/tests/html/test_visibility.html");

			boolean sawThrow4 = false;
			try {
				// originally verifyVisible|visibleParagraph|
						assertTrue(selenium.isVisible("visibleParagraph"));
			}
			catch (Throwable e) {
			    e.printStackTrace();
				sawThrow4 = true;
			}
			verifyFalse(sawThrow4);
			

			boolean sawThrow5 = false;
			try {
				// originally verifyNotVisible|hiddenParagraph|
						assertTrue(!selenium.isVisible("hiddenParagraph"));
			}
			catch (Throwable e) {
				sawThrow5 = true;
			}
			verifyFalse(sawThrow5);
			

			boolean sawThrow6 = false;
			try {
				// originally verifyNotVisible|suppressedParagraph|
						assertTrue(!selenium.isVisible("suppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow6 = true;
			}
			verifyFalse(sawThrow6);
			

			boolean sawThrow7 = false;
			try {
				// originally verifyNotVisible|classSuppressedParagraph|
						assertTrue(!selenium.isVisible("classSuppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow7 = true;
			}
			verifyFalse(sawThrow7);
			

			boolean sawThrow8 = false;
			try {
				// originally verifyNotVisible|jsClassSuppressedParagraph|
						assertTrue(!selenium.isVisible("jsClassSuppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow8 = true;
			}
			verifyFalse(sawThrow8);
			

			boolean sawThrow9 = false;
			try {
				// originally verifyNotVisible|hiddenSubElement|
						assertTrue(!selenium.isVisible("hiddenSubElement"));
			}
			catch (Throwable e) {
				sawThrow9 = true;
			}
			verifyFalse(sawThrow9);
			

			boolean sawThrow10 = false;
			try {
				// originally verifyVisible|visibleSubElement|
						assertTrue(selenium.isVisible("visibleSubElement"));
			}
			catch (Throwable e) {
				sawThrow10 = true;
			}
			verifyFalse(sawThrow10);
			

			boolean sawThrow11 = false;
			try {
				// originally verifyNotVisible|suppressedSubElement|
						assertTrue(!selenium.isVisible("suppressedSubElement"));
			}
			catch (Throwable e) {
				sawThrow11 = true;
			}
			verifyFalse(sawThrow11);
			

			boolean sawThrow12 = false;
			try {
				// originally verifyNotVisible|jsHiddenParagraph|
						assertTrue(!selenium.isVisible("jsHiddenParagraph"));
			}
			catch (Throwable e) {
				sawThrow12 = true;
			}
			verifyFalse(sawThrow12);
			

			boolean sawThrow13 = false;
			try {
							assertTrue(!selenium.isVisible("visibleParagraph"));
			}
			catch (Throwable e) {
				sawThrow13 = true;
			}
			verifyTrue(sawThrow13);
			

			boolean sawThrow15 = false;
			try {
							assertTrue(selenium.isVisible("hiddenParagraph"));
			}
			catch (Throwable e) {
				sawThrow15 = true;
			}
			verifyTrue(sawThrow15);
			

			boolean sawThrow17 = false;
			try {
							assertTrue(selenium.isVisible("suppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow17 = true;
			}
			verifyTrue(sawThrow17);
			

			boolean sawThrow19 = false;
			try {
							assertTrue(selenium.isVisible("classSuppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow19 = true;
			}
			verifyTrue(sawThrow19);
			

			boolean sawThrow21 = false;
			try {
							assertTrue(selenium.isVisible("jsClassSuppressedParagraph"));
			}
			catch (Throwable e) {
				sawThrow21 = true;
			}
			verifyTrue(sawThrow21);
			

			boolean sawThrow23 = false;
			try {
							assertTrue(selenium.isVisible("hiddenSubElement"));
			}
			catch (Throwable e) {
				sawThrow23 = true;
			}
			verifyTrue(sawThrow23);
			

			boolean sawThrow25 = false;
			try {
							assertTrue(selenium.isVisible("suppressedSubElement"));
			}
			catch (Throwable e) {
				sawThrow25 = true;
			}
			verifyTrue(sawThrow25);
			

			boolean sawThrow27 = false;
			try {
							assertTrue(selenium.isVisible("jsHiddenParagraph"));
			}
			catch (Throwable e) {
				sawThrow27 = true;
			}
			verifyTrue(sawThrow27);
			

			boolean sawThrow29 = false;
			try {
				// originally verifyNotVisible|hiddenInput|
						assertTrue(!selenium.isVisible("hiddenInput"));
			}
			catch (Throwable e) {
			    e.printStackTrace();
				sawThrow29 = true;
			}
			verifyFalse(sawThrow29);
			

			boolean sawThrow30 = false;
			try {
							assertTrue(selenium.isVisible("nonExistentElement"));
			}
			catch (Throwable e) {
				sawThrow30 = true;
			}
			verifyTrue(sawThrow30);
			

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
