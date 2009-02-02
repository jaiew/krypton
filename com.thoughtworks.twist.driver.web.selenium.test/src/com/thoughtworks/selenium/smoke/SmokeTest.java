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
package com.thoughtworks.selenium.smoke;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.*;

import static junit.framework.Assert.*;

public class SmokeTest {
    private static Selenium browser;

    @BeforeClass
    public static void startBrowser() {
        browser = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.google.com");
        browser.start();
    }

    @Test
    public void testGoogle() throws InterruptedException {
        browser.open("http://www.google.com/webhp?hl=en");
        browser.type("q", "hello world\n");
        
        assertTrue(browser.isTextPresent("helloworld.com"));
        assertEquals("hello world - Google Search", browser.getTitle());
    }
    
    @Test
    public void testDigg() throws Exception {
        browser.open("http://www.digg.com");
        browser.click("//div[contains(@class, 'news-summary')][1]//a[@class = 'tool comments']");
        browser.click("//a[text() = 'digg it']");

        assertTrue(browser.isTextPresent("You've got to login or join to Digg that!"));
        assertTrue(browser.isElementPresent("//div[@class = 'login-digg']//input[@name = 'username']"));
	}

    @AfterClass
    public static void stopBrowser() {
        browser.stop();
    }
}