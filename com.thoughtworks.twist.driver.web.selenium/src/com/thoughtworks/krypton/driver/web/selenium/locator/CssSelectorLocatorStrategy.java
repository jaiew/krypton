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
package com.thoughtworks.krypton.driver.web.selenium.locator;

import org.w3c.dom.Element;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;
import com.thoughtworks.krypton.driver.web.browser.JavascriptException;
import com.thoughtworks.krypton.driver.web.browser.locator.DomLocatorStrategy;
import com.thoughtworks.krypton.driver.web.browser.locator.XPathLocatorStrategy;

public class CssSelectorLocatorStrategy extends XPathLocatorStrategy {
    private static final String CSS_EQUALS_PREFIX = "css=";
//	private Context context;
//	private ScriptableObject scope;
    
    public boolean canLocate(String locator) {
        return locator != null && locator.startsWith(CSS_EQUALS_PREFIX);
    }

    public Element locate(BrowserSession session, String locator) {
//		if (context == null) {
//			context = new ContextFactory().enterContext();
//			scope = context.initStandardObjects();
//			context.evaluateString(scope, session.readResource("twist-cssQuery.js", getClass()), "", 0, null);
//			context.evaluateString(scope, session.readResource("twist-cssQuery-standard.js", getClass()), "", 0, null);
//			context.evaluateString(scope, session.readResource("twist-cssQuery-level2.js", getClass()), "", 0, null);
//			context.evaluateString(scope, session.readResource("twist-cssQuery-level3.js", getClass()), "", 0, null);
//			ScriptableObject.putProperty(scope, "out", Context.javaToJS(System.out, scope));
//		}
//		ScriptableObject.putProperty(scope, "document", Context.javaToJS(session.dom(), scope));
//		
//		Object result = context.evaluateString(scope, "Twist.cssQuery('" + locator.substring(CSS_EQUALS_PREFIX.length()) + "')[0]", "", 0, null);
//		if (result instanceof Undefined) {
//			return null;
//		} else {
//			return (Element) Context.jsToJava(result, Element.class);
//		}

    	session.inject("twist-dom-to-xpath.js", DomLocatorStrategy.class);
        session.inject("twist-cssQuery.js", getClass());
//        session.inject("twist-cssQuery-standard.js", getClass());
//        session.inject("twist-cssQuery-level2.js", getClass());
//        session.inject("twist-cssQuery-level3.js", getClass());
        try {
			String xpath = cssToXPath(session, locator);
			return super.locate(session, xpath);
		} catch (JavascriptException e) {
			return null;
		}
    }

    private String cssToXPath(BrowserSession session, String locator) {
        return session.evaluate("Twist.domToXPath(Twist.cssQuery('" + locator.substring(CSS_EQUALS_PREFIX.length()) + "')[0])");
    }
}
