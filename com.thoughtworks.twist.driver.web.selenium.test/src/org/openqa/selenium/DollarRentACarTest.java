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

public class DollarRentACarTest extends SeleneseTestCase {
    public static String TIMEOUT = "120000";

    public void testSearchAndRent() throws InterruptedException {
        selenium.setTimeout("15000");
        System.out.println(selenium.getEval("navigator.userAgent"));
        System.out.println(selenium.getEval("navigator.appVersion"));
        System.out.println(selenium.getEval("navigator.appName"));
        System.out.println(selenium.getEval("navigator.platform"));
        System.out.println(selenium.getEval("navigator.appCodeName"));
        selenium.open("http://www.dollar.com/default.aspx?AspxAutoDetectCookieSupport=1");
        Thread.sleep(10000);
        System.out.println(selenium.getEval("document.cookie"));
        selenium.type("ctl07_ctl01_ResStartColumnLayout_LocationTime_PickupLocationTextBox", "PDX");
        selenium.select("ctl07_ctl01_ResStartColumnLayout_LocationTime_CarTypeDropDownList", "label=Compact");
        selenium.click("ctl07_ctl01_ResStartColumnLayout_LocationTime_GetRatesButton");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.click("ctl06_ctl03_VehicleInformationColumnLayout_GoButton");
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.select("ctl06_ctl03_PersonalInformationColumnLayout_OptionsColumnLayout_LoyaltyProgramList", "label=Virgin Atlantic Flying Club");
        selenium.click("ctl06_ctl03_PersonalInformationColumnLayout_ReserveNowSitecoreImageButton");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isTextPresent("Please enter your first name")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        assertTrue(selenium.isTextPresent("Please enter your first name"));
        assertTrue(selenium.isTextPresent("Please enter a valid email address"));
    }
}
