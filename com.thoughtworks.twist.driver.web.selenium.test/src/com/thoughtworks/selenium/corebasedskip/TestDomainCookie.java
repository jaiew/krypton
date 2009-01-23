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
package com.thoughtworks.selenium.corebasedskip;
import com.thoughtworks.selenium.*;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from C:\svn\selenium-rc\trunk\clients\java\target\selenium-server\tests/TestDomainCookie.html.
 */
public class TestDomainCookie extends SeleneseTestCase
{
    public void setUp() throws Exception {
        super.setUp("http://dfab.fabulich.com");
    }
   public void testDomainCookie() throws Throwable {
        try {
            

/* Test Cookie */
            // storeEval|parseUrl(canonicalize(absolutify("html", selenium.browserbot.baseUrl))).host;|host
            String host = selenium.getEval("parseUrl(canonicalize(absolutify(\"html\", selenium.browserbot.baseUrl))).host;");
            String base = "/selenium-server/tests/html";
            // echo|${host}|            : op not meaningful from rc client
            // assertExpression|${host}|*.*.*
            assertEquals("*.*.*", host);
            // storeEval|var host = parseUrl(canonicalize(absolutify("html", selenium.browserbot.baseUrl))).host;  host.replace(/^[^\\.]*/, "");|domain
            String domain = host.replaceFirst("^[^\\.]*", "");
            // echo|${base}|            : op not meaningful from rc client
            // open|${base}/path1/cookie1.html|
            selenium.open(base + "/path1/cookie1.html");
            // deleteCookie| testCookieWithSameName|path=/
            selenium.deleteCookie(" testCookieWithSameName", "path=/");
            // deleteCookie| addedCookieForPath1|path=${base}/path1/
            selenium.deleteCookie(" addedCookieForPath1", "path=" + base + "/path1/");
            // deleteCookie|domainCookie|domain=${domain}; path=/
            selenium.deleteCookie("domainCookie", "domain=" + domain + "; path=/");
            // assertCookie||
            assertEquals("", selenium.getCookie());
            // open|${base}/path1/cookie1.html|
            selenium.open(base + "/path1/cookie1.html");
            // createCookie|domainCookie=domain value|domain=${domain}; path=/
            selenium.createCookie("domainCookie=domain value", "domain=" + domain + "; path=/");
            // assertCookieByName|domainCookie|domain value
            assertEquals("domain value", selenium.getCookieByName("domainCookie"));
            // deleteCookie|domainCookie|domain=${domain}; path=/
            selenium.deleteCookie("domainCookie", "domain=" + domain + "; path=/");
            // assertCookieNotPresent|domainCookie|
            assertTrue(!selenium.isCookiePresent("domainCookie"));
            // assertCookie||
            assertEquals("", selenium.getCookie());

            checkForVerificationErrors();
        }
        finally {
            clearVerificationErrors();
        }
    }
}
