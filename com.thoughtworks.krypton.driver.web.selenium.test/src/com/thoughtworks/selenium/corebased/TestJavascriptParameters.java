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
 * @author Nelson Sproul (nelsons@plumtree.com)
 * based on, but different from, selenium/tests/TestJavascriptParameters.html.
 */
public class TestJavascriptParameters extends SeleneseTestCase
{
	public void test() throws Throwable {
        	try {

/* Test javascript evaluation of parameters       */
		// open|./tests/html/test_store_value.html
		selenium.open("/selenium-server/tests/html/test_store_value.html");
		// type|theText|javascript{[1,2,3,4,5].join(':')}
		selenium.type("theText", selenium.getEval("[1,2,3,4,5].join(':')"));
		// verifyValue|theText|1:2:3:4:5
		verifyEquals("1:2:3:4:5", selenium.getValue("theText"));
		// type|javascript{'the' + 'Text'}|javascript{10 * 5}
		selenium.type(selenium.getEval("'the' + 'Text'"), selenium.getEval("10 * 5"));
		// verifyValue|theText|50
		verifyEquals("50", selenium.getValue("theText"));
		// verifyValue|theText|javascript{10 + 10 + 10 + 10 + 10}
		verifyEquals(selenium.getEval("10 + 10 + 10 + 10 + 10"), selenium.getValue("theText"));

		/* Demonstrate interation between variable substitution and javascript */
		// store|the value|var1
		String var1 = "the value";
		// type|theText|javascript{'${var1}'.toUpperCase()}
		selenium.type("theText", selenium.getEval("'${var1}'.toUpperCase()"));
		// verifyValue|theText|${VAR1}
		verifyEquals("${VAR1}", selenium.getValue("theText"));    // hand translated
		// type|theText|javascript{storedVars['var1'].toUpperCase()}
        selenium.type("theText", selenium.getEval("'" + var1 + "'.toUpperCase()"));
        // verifyValue|theText|THE VALUE
		verifyEquals("THE VALUE", selenium.getValue("theText"));
		
        // verifyExpression|javascript{selenium.getValue('theText')}|regexp:TH[Ee] VALUE
//		verifyEquals(selenium.getEval("selenium.getValue('theText')"), "regexp:TH[Ee] VALUE");

		checkForVerificationErrors();
            }
            finally {
            	clearVerificationErrors();
            }
	}
}
