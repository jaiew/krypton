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
package org.openqa.selenium;

import com.thoughtworks.selenium.SeleneseTestCase;

public class VerizonTest extends SeleneseTestCase {
    private static final String TIMEOUT = "120000";

    public void testSignUp() {
//        selenium.addLocationURLExclusionPattern(".*/vzTracker.aspx.*");
//        selenium.addLocationURLExclusionPattern(".*doubleclick.net/.*");
        selenium.setTimeout(TIMEOUT);
        selenium.open("http://www22.verizon.com/");
        selenium.click("link=Set Up New Phone Service");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.click("//a[@href=\"/ForYourHome/NewConnect/OrderWelcomeSignin.aspx\"]");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.type("txtSTREET_ADDRESS1", "49 Bonnie Lane");
        selenium.type("txtCITY", "Dedham");
        selenium.select("lstSTATE", "label=MA");
        selenium.type("txtZIP_CODE", "02026");
        selenium.click("imgNext");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.click("//img[@alt='next']");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.click("btnGo");
        selenium.waitForPageToLoad(TIMEOUT);
    }
}
