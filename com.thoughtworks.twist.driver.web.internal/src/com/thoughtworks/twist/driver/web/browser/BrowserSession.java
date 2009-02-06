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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.eclipse.swt.browser.Browser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.thoughtworks.twist.driver.web.browser.locator.LocatorStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.WaitStrategy;

public interface BrowserSession {

	// Core Browser

	void openBrowser();

	void closeBrowser();

	BrowserFamily getBrowserFamily();

	Browser getBrowser();

	// JavaScript

	void execute(String statments);

	String evaluate(String expression);

	void inject(String script);

	void inject(String script, Class<?> baseClass);

	String readResource(String resource, Class<?> aClass);

	// DOM

	Document dom();

	Rectangle boundingRectangle(Element element);

	Point center(Element element);

	Element locate(String locator);

	List<Node> locateAll(String xpathExpression);

	void fireEvent(Element element, String eventName);

	void setCursorPosition(Element element, int position);

	int getCursorPosition(Element element);

	String domExpression(Element element);

	String getText(Node node);

	boolean isVisible(Element element);

	// Event Thread

	void waitForIdle();

	void pumpEvents();

	void setEventLoopTimeout(int timeout);

	void setPatientLocatorTimeout(int timeout);

	// Strategies

	void addLocatorStrategy(LocatorStrategy locatorStrategy);

	void addWaitStrategy(WaitStrategy waitStrategy);

	// Frames/Windows

	void setWindowExpression(String domExpression);

	String getWindowExpression();

	String getDocumentExpression();
}
