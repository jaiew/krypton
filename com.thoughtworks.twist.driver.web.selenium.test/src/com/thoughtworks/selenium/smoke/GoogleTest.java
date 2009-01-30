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

import com.thoughtworks.selenium.*;
import junit.framework.*;

public class GoogleTest extends TestCase {
    private Selenium browser;

    public void setUp() {
        browser = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.google.com");
        browser.start();
    }

    public void testGoogle() {
        browser.open("http://www.google.com/webhp?hl=en");
        browser.type("q", "hello world\n");
        assertEquals("hello world - Google Search", browser.getTitle());
    }

    public void tearDown() {
        browser.stop();
    }
}
