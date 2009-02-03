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
package com.thoughtworks.twist.driver.web.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thoughtworks.twist.driver.web.browser.locator.LocatorStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.WaitStrategy;

public interface BrowserSession {

	void openBrowser();

	void closeBrowser();

	String execute(String expression);

	void inject(String script);

	void inject(final String script, final Class<?> baseClass);

	String readResource(String resource, Class<?> aClass);

	BrowserFamily getBrowserFamily();

	Document dom();

	String domAsString();

	Rectangle boundingRectangle(Element element);

	Point center(Element element);

	Element locate(String locator);

	NodeList locateAll(String xpathExpression);

	void fireEvent(Element element, String eventName);

	void setCursorPosition(Element element, int position);

	int getCursorPosition(Element element);

	String outerHTML(Element element);

	void waitForIdle();

	void pumpEvents();

	boolean areWaitStrategiesIdle();

	void addLocatorStrategy(LocatorStrategy locatorStrategy);

	void addWaitStrategy(WaitStrategy waitStrategy);

	void removeWaitStrategy(WaitStrategy waitStrategy);

	String domExpression(Element element);

	void setEventLoopTimeout(int timeout);

	void setPatientLocatorTimeout(int timeout);

	void waitForActivity();

	String asXml(Node element);

	String getText(Node node);

	String getText(Node node, boolean preformatted);

	void scrollIntoView(Element element);

	boolean isVisible(Element element);

	void setWindowExpression(String domExpression);

	String getWindowExpression();

	String getDocumentExpression();

	Browser getBrowser();
}
