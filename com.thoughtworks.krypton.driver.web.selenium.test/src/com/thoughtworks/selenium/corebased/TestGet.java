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
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestGet.html.
 */
public class TestGet extends SeleneseTestCase
{
   public void testGet() throws Throwable {
		try {
			
// This is one stupid crazy test.
		    
		    
/* TestGet */

			/* test API getters */
			// open|../tests/html/test_get.html|
			selenium.open("/selenium-server/tests/html/test_get.html");

			/* IE uppercases the property names of the style. Both Opera and Firefox         lowercase the property names of the style. Both IE and Opera omit the         trailing semi-colon. */
			// verifyAttribute|//img[@alt='banner']@style|regexp:(width|WIDTH): 644px; (height|HEIGHT): 41px(;?)
			verifyEquals("regexp:(width|WIDTH): 644px; (height|HEIGHT): 41px(;?)", selenium.getAttribute("//img[@alt='banner']@style"));

			/* This asserts on the current behavior of selArrayToString(). Commas and         backslashes are escaped in array values. Backslash-craziness!! */
//			String[] tmp2 = {"foo*"};
			// verifySelectOptions|selectWithFunkyValues|foo*
//			verifyEquals(tmp2, selenium.getSelectOptions("selectWithFunkyValues"));
//            String[] tmp3 = {"javascript{ [ 'foo'", " '\\\\,\\\\\\\\bar\\\\\\\\\\\\,'", " '*baz*' ].join('", "') }"};
			String[] tmp3 = {"foo", ",\\bar\\,", "\u00a0\u00a0baz\u00a0\u00a0"};
			// verifySelectOptions|selectWithFunkyValues|javascript{ [ 'foo', '\\\\,\\\\\\\\bar\\\\\\\\\\\\,', '*baz*' ].join(',') }
			verifyEquals(tmp3, selenium.getSelectOptions("selectWithFunkyValues"));
//			String[] tmp4 = {"javascript{ 'regexp:' + [ 'foo'", " '\\\\\\\\,\\\\\\\\\\\\\\\\bar\\\\\\\\\\\\\\\\\\\\\\\\,'", " '\\\\u00a0{2}baz\\\\u00a0{2}' ].join('", "') }"};
			// verifySelectOptions|selectWithFunkyValues|javascript{ 'regexp:' + [ 'foo', '\\\\\\\\,\\\\\\\\\\\\\\\\bar\\\\\\\\\\\\\\\\\\\\\\\\,', '\\\\u00a0{2}baz\\\\u00a0{2}' ].join(',') }
//			verifyEquals(tmp4, selenium.getSelectOptions("selectWithFunkyValues"));

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
