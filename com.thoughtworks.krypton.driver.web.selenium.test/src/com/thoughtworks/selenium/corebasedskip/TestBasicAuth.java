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
 * Generated from C:\svn\selenium\rc\trunk\clients\java\target\selenium-server\tests/TestBasicAuth.html.
 */
public class TestBasicAuth extends SeleneseTestCase
{
	
   public void testBasicAuth() throws Throwable {
		try {
			

/* TestBasicAuth */
//			 open|http://alice:foo@localhost:4444/selenium-server/tests/html/basicAuth/index.html|
			selenium.open("http://alice:foo@localhost:" + getWebServerPort() + "/selenium-server/tests/html/basicAuth/index.html");
			// assertTitle|welcome|
			assertEquals("*Welcome", selenium.getTitle());

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
	public void setUp() throws Exception {
		super.setUp("http://localhost:4444");
	}
}
