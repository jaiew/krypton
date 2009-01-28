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
 * Generated from //socrates/unixhome/nelsons/shared/p4/Dev/selenium-rc_svn/trunk/core/javascript/tests/GoogleTestSearch.html.
 */
public class GoogleTestSearch extends SeleneseTestCase
{
   public void testGoogleTestSearch() throws Throwable {
//       for (int i = 0; i < 50; i++) {           
           runGoogleTest();
//           System.out.println(i);
//       }
	}
private void runGoogleTest() {
    try {

/* Google Test Search */
		// open|http://www.google.com|
		selenium.open("http://www.google.com/webhp");
		
		// verifyTitle|Google|
		verifyEquals("*Google", selenium.getTitle());
		
//		Browser browser = selenium.getBrowserSession().browser;
//		Display display = browser.getDisplay();
//        while (!browser.getShell().isDisposed()) {
//            if (!display.readAndDispatch()) {
//                display.sleep();
//            }
//        }
		
		// type|q|Selenium OpenQA
		selenium.type("q", "Selenium OpenQA");
		// verifyValue|q|Selenium OpenQA
		verifyEquals("Selenium OpenQA", selenium.getValue("q"));
		// clickAndWait|btnG|
		selenium.click("btnG");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("openqa.org"));
		// verifyTitle|Selenium OpenQA - Google Search|
		verifyEquals("*Selenium OpenQA - Google Search", selenium.getTitle());

		checkForVerificationErrors();
            }
            finally {
            	clearVerificationErrors();
            }
}
	public void setUp() throws Exception {
		super.setUp("http://www.google.com");
	}
}
