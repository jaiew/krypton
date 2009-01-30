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
/*
 * Copyright 2006 ThoughtWorks, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

// This file has been automatically generated via XSL
package com.thoughtworks.selenium;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.selenium.internal.SeleniumOSGiFactory;
import com.thoughtworks.selenium.internal.SeleniumReflectionFactory;

/**
 * The default implementation of the Selenium interface; <i>end users will
 * primarily interact with this object.</i>
 */
@SuppressWarnings("serial")
public class DefaultSelenium implements Selenium {
	private static Map<String, String> SELENIUM_BROWSER_LAUNCHES_TO_SWT_BROWSERS = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				{
					put("*firefox", "mozilla");
					put("*pifirefox", "mozilla");
					put("*firefoxproxy", "mozilla");
					put("*chrome", "mozilla");

					put("*iexplore", "ie");
					put("*piiexplore", "ie");
					put("*iexploreproxy", "ie");
					put("*iehta", "ie");

					put("*safari", "safari");
				}
			});

	private Selenium selenium;

	public Selenium getUnderyingSelenium() {
		return selenium;
	}

	public DefaultSelenium(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		updateTwistBrowserPropertyIfNeeded(browserStartCommand);
		selenium = createSeleniumInstance(browserURL);
	}

	private void updateTwistBrowserPropertyIfNeeded(String browserStartCommand) {
		if (System.getProperty("twist.driver.web.browser") != null) {
			return;
		}
		if (browserStartCommand != null && browserStartCommand.length() != 0) {
			System.setProperty("twist.driver.web.browser", SELENIUM_BROWSER_LAUNCHES_TO_SWT_BROWSERS.get(browserStartCommand));
		}
	}

	private Selenium createSeleniumInstance(String browserURL) {
		try {
			return new SeleniumOSGiFactory().create(browserURL);
		} catch (RuntimeException e) {
			return new SeleniumReflectionFactory().create(browserURL);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Uses an arbitrary CommandProcessor */
	public DefaultSelenium(Object processor) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Allows javascript to be specified for the test on a per-browser session
	 * basis. The javascript will be in-play the next time a session is created;
	 * that is, typically the next time <code>start()</code> is invoked (and
	 * <code>getNewBrowserSession</code> is sent to the RC under the sheets).
	 * 
	 * @param extensionJs
	 *            a string representing the extra extension javascript to
	 *            include in the browser session. This is in addition to any
	 *            specified via the -userExtensions switch when starting the RC.
	 */
	public void setExtensionJs(String extensionJs) {
		selenium.setExtensionJs(extensionJs);
	}

	public void start() {
		selenium.start();
	}

	public void start(String optionsString) {
		selenium.start(optionsString);
	}

	public void start(Object optionsObject) {
		selenium.start(optionsObject);
	}

	public void stop() {
		selenium.stop();
	}

	public void showContextualBanner() {
		try {
			StackTraceElement[] e = Thread.currentThread().getStackTrace();

			String className = null;
			String methodName = null;

			for (int i = 0; i < e.length; i++) {
				if (e[i].getClassName().equals("java.lang.Thread") || e[i].getMethodName().equals("showContextualBanner")) {
					continue;
				}
				className = e[i].getClassName();
				methodName = e[i].getMethodName();
				break;
			}
			showContextualBanner(className, methodName);
		} catch (Exception e) {
			this.setContext("<unknown context>");
		}

	}

	public void showContextualBanner(String className, String methodName) {
		StringBuilder context = new StringBuilder().append(className).append(": ");

		boolean lastOneWasUpperCase = false;
		boolean nextOneIsUpperCase = false;
		int len = methodName.length();
		for (int i = 0; i < len; i++) {
			char ch = methodName.charAt(i);
			nextOneIsUpperCase = i < len - 1 ? Character.isUpperCase(methodName.charAt(i + 1)) : true;

			if ((Character.isUpperCase(ch) && (!lastOneWasUpperCase || !nextOneIsUpperCase))) {
				context.append(" ");
				lastOneWasUpperCase = true;
			}
			if (!Character.isUpperCase(ch)) {
				lastOneWasUpperCase = false;
			}
			context.append(ch);
		}
		this.setContext(context.toString());
	}

	// From here on, everything in this file has been auto-generated

	public void click(String locator) {
		selenium.click(locator);
	}

	public void doubleClick(String locator) {
		selenium.doubleClick(locator);
	}

	public void contextMenu(String locator) {
		selenium.contextMenu(locator);
	}

	public void clickAt(String locator, String coordString) {
		selenium.clickAt(locator, coordString);
	}

	public void doubleClickAt(String locator, String coordString) {
		selenium.doubleClickAt(locator, coordString);
	}

	public void contextMenuAt(String locator, String coordString) {
		selenium.contextMenuAt(locator, coordString);
	}

	public void fireEvent(String locator, String eventName) {
		selenium.fireEvent(locator, eventName);
	}

	public void focus(String locator) {
		selenium.focus(locator);
	}

	public void keyPress(String locator, String keySequence) {
		selenium.keyPress(locator, keySequence);
	}

	public void shiftKeyDown() {
		selenium.shiftKeyDown();
	}

	public void shiftKeyUp() {
		selenium.shiftKeyUp();
	}

	public void metaKeyDown() {
		selenium.metaKeyDown();
	}

	public void metaKeyUp() {
		selenium.metaKeyUp();
	}

	public void altKeyDown() {
		selenium.altKeyDown();
	}

	public void altKeyUp() {
		selenium.altKeyUp();
	}

	public void controlKeyDown() {
		selenium.controlKeyDown();
	}

	public void controlKeyUp() {
		selenium.controlKeyUp();
	}

	public void keyDown(String locator, String keySequence) {
		selenium.keyDown(locator, keySequence);
	}

	public void keyUp(String locator, String keySequence) {
		selenium.keyUp(locator, keySequence);
	}

	public void mouseOver(String locator) {
		selenium.mouseOver(locator);
	}

	public void mouseOut(String locator) {
		selenium.mouseOut(locator);
	}

	public void mouseDown(String locator) {
		selenium.mouseDown(locator);
	}

	public void mouseDownRight(String locator) {
		selenium.mouseDownRight(locator);
	}

	public void mouseDownAt(String locator, String coordString) {
		selenium.mouseDownAt(locator, coordString);
	}

	public void mouseDownRightAt(String locator, String coordString) {
		selenium.mouseDownRightAt(locator, coordString);
	}

	public void mouseUp(String locator) {
		selenium.mouseUp(locator);
	}

	public void mouseUpRight(String locator) {
		selenium.mouseUpRight(locator);
	}

	public void mouseUpAt(String locator, String coordString) {
		selenium.mouseUpAt(locator, coordString);
	}

	public void mouseUpRightAt(String locator, String coordString) {
		selenium.mouseUpRightAt(locator, coordString);
	}

	public void mouseMove(String locator) {
		selenium.mouseMove(locator);
	}

	public void mouseMoveAt(String locator, String coordString) {
		selenium.mouseMoveAt(locator, coordString);
	}

	public void type(String locator, String value) {
		selenium.type(locator, value);
	}

	public void typeKeys(String locator, String value) {
		selenium.typeKeys(locator, value);
	}

	public void setSpeed(String value) {
		selenium.setSpeed(value);
	}

	public String getSpeed() {
		return selenium.getSpeed();
	}

	public void check(String locator) {
		selenium.check(locator);
	}

	public void uncheck(String locator) {
		selenium.uncheck(locator);
	}

	public void select(String selectLocator, String optionLocator) {
		selenium.select(selectLocator, optionLocator);
	}

	public void addSelection(String locator, String optionLocator) {
		selenium.addSelection(locator, optionLocator);
	}

	public void removeSelection(String locator, String optionLocator) {
		selenium.removeSelection(locator, optionLocator);
	}

	public void removeAllSelections(String locator) {
		selenium.removeAllSelections(locator);
	}

	public void submit(String formLocator) {
		selenium.submit(formLocator);
	}

	public void open(String url) {
		selenium.open(url);
	}

	public void openWindow(String url, String windowID) {
		selenium.openWindow(url, windowID);
	}

	public void selectWindow(String windowID) {
		selenium.selectWindow(windowID);
	}

	public void selectFrame(String locator) {
		selenium.selectFrame(locator);
	}

	public boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target) {
		return selenium.getWhetherThisFrameMatchFrameExpression(currentFrameString, target);
	}

	public boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target) {
		return selenium.getWhetherThisWindowMatchWindowExpression(currentWindowString, target);
	}

	public void waitForPopUp(String windowID, String timeout) {
		selenium.waitForPopUp(windowID, timeout);
	}

	public void chooseCancelOnNextConfirmation() {
		selenium.chooseCancelOnNextConfirmation();
	}

	public void chooseOkOnNextConfirmation() {
		selenium.chooseOkOnNextConfirmation();
	}

	public void answerOnNextPrompt(String answer) {
		selenium.answerOnNextPrompt(answer);
	}

	public void goBack() {
		selenium.goBack();
	}

	public void refresh() {
		selenium.refresh();
	}

	public void close() {
		selenium.close();
	}

	public boolean isAlertPresent() {
		return selenium.isAlertPresent();
	}

	public boolean isPromptPresent() {
		return selenium.isPromptPresent();
	}

	public boolean isConfirmationPresent() {
		return selenium.isConfirmationPresent();
	}

	public String getAlert() {
		return selenium.getAlert();
	}

	public String getConfirmation() {
		return selenium.getConfirmation();
	}

	public String getPrompt() {
		return selenium.getPrompt();
	}

	public String getLocation() {
		return selenium.getLocation();
	}

	public String getTitle() {
		return selenium.getTitle();
	}

	public String getBodyText() {
		return selenium.getBodyText();
	}

	public String getValue(String locator) {
		return selenium.getValue(locator);
	}

	public String getText(String locator) {
		return selenium.getText(locator);
	}

	public void highlight(String locator) {
		selenium.highlight(locator);
	}

	public String getEval(String script) {
		return selenium.getEval(script);
	}

	public boolean isChecked(String locator) {
		return selenium.isChecked(locator);
	}

	public String getTable(String tableCellAddress) {
		return selenium.getTable(tableCellAddress);
	}

	public String[] getSelectedLabels(String selectLocator) {
		return selenium.getSelectedLabels(selectLocator);
	}

	public String getSelectedLabel(String selectLocator) {
		return selenium.getSelectedLabel(selectLocator);
	}

	public String[] getSelectedValues(String selectLocator) {
		return selenium.getSelectedValues(selectLocator);
	}

	public String getSelectedValue(String selectLocator) {
		return selenium.getSelectedValue(selectLocator);
	}

	public String[] getSelectedIndexes(String selectLocator) {
		return selenium.getSelectedIndexes(selectLocator);
	}

	public String getSelectedIndex(String selectLocator) {
		return selenium.getSelectedIndex(selectLocator);
	}

	public String[] getSelectedIds(String selectLocator) {
		return selenium.getSelectedIds(selectLocator);
	}

	public String getSelectedId(String selectLocator) {
		return selenium.getSelectedId(selectLocator);
	}

	public boolean isSomethingSelected(String selectLocator) {
		return selenium.isSomethingSelected(selectLocator);
	}

	public String[] getSelectOptions(String selectLocator) {
		return selenium.getSelectOptions(selectLocator);
	}

	public String getAttribute(String attributeLocator) {
		return selenium.getAttribute(attributeLocator);
	}

	public boolean isTextPresent(String pattern) {
		return selenium.isTextPresent(pattern);
	}

	public boolean isElementPresent(String locator) {
		return selenium.isElementPresent(locator);
	}

	public boolean isVisible(String locator) {
		return selenium.isVisible(locator);
	}

	public boolean isEditable(String locator) {
		return selenium.isEditable(locator);
	}

	public String[] getAllButtons() {
		return selenium.getAllButtons();
	}

	public String[] getAllLinks() {
		return selenium.getAllLinks();
	}

	public String[] getAllFields() {
		return selenium.getAllFields();
	}

	public String[] getAttributeFromAllWindows(String attributeName) {
		return selenium.getAttributeFromAllWindows(attributeName);
	}

	public void dragdrop(String locator, String movementsString) {
		selenium.dragdrop(locator, movementsString);
	}

	public void setMouseSpeed(String pixels) {
		selenium.setMouseSpeed(pixels);
	}

	public Number getMouseSpeed() {
		return selenium.getMouseSpeed();
	}

	public void dragAndDrop(String locator, String movementsString) {
		selenium.dragAndDrop(locator, movementsString);
	}

	public void dragAndDropToObject(String locatorOfObjectToBeDragged, String locatorOfDragDestinationObject) {
		selenium.dragAndDropToObject(locatorOfObjectToBeDragged, locatorOfDragDestinationObject);
	}

	public void windowFocus() {
		selenium.windowFocus();
	}

	public void windowMaximize() {
		selenium.windowMaximize();
	}

	public String[] getAllWindowIds() {
		return selenium.getAllWindowIds();
	}

	public String[] getAllWindowNames() {
		return selenium.getAllWindowNames();
	}

	public String[] getAllWindowTitles() {
		return selenium.getAllWindowTitles();
	}

	public String getHtmlSource() {
		return selenium.getHtmlSource();
	}

	public void setCursorPosition(String locator, String position) {
		selenium.setCursorPosition(locator, position);
	}

	public Number getElementIndex(String locator) {
		return selenium.getElementIndex(locator);
	}

	public boolean isOrdered(String locator1, String locator2) {
		return selenium.isOrdered(locator1, locator2);
	}

	public Number getElementPositionLeft(String locator) {
		return selenium.getElementPositionLeft(locator);
	}

	public Number getElementPositionTop(String locator) {
		return selenium.getElementPositionTop(locator);
	}

	public Number getElementWidth(String locator) {
		return selenium.getElementWidth(locator);
	}

	public Number getElementHeight(String locator) {
		return selenium.getElementHeight(locator);
	}

	public Number getCursorPosition(String locator) {
		return selenium.getCursorPosition(locator);
	}

	public String getExpression(String expression) {
		return selenium.getExpression(expression);
	}

	public Number getXpathCount(String xpath) {
		return selenium.getXpathCount(xpath);
	}

	public void assignId(String locator, String identifier) {
		selenium.assignId(locator, identifier);
	}

	public void allowNativeXpath(String allow) {
		selenium.allowNativeXpath(allow);
	}

	public void ignoreAttributesWithoutValue(String ignore) {
		selenium.ignoreAttributesWithoutValue(ignore);
	}

	public void waitForCondition(String script, String timeout) {
		selenium.waitForCondition(script, timeout);
	}

	public void setTimeout(String timeout) {
		selenium.setTimeout(timeout);
	}

	public void waitForPageToLoad(String timeout) {
		selenium.waitForPageToLoad(timeout);
	}

	public void waitForFrameToLoad(String frameAddress, String timeout) {
		selenium.waitForFrameToLoad(frameAddress, timeout);
	}

	public String getCookie() {
		return selenium.getCookie();
	}

	public String getCookieByName(String name) {
		return selenium.getCookieByName(name);
	}

	public boolean isCookiePresent(String name) {
		return selenium.isCookiePresent(name);
	}

	public void createCookie(String nameValuePair, String optionsString) {
		selenium.createCookie(nameValuePair, optionsString);
	}

	public void deleteCookie(String name, String optionsString) {
		selenium.deleteCookie(name, optionsString);
	}

	public void deleteAllVisibleCookies() {
		selenium.deleteAllVisibleCookies();
	}

	public void setBrowserLogLevel(String logLevel) {
		selenium.setBrowserLogLevel(logLevel);
	}

	public void runScript(String script) {
		selenium.runScript(script);
	}

	public void addLocationStrategy(String strategyName, String functionDefinition) {
		selenium.addLocationStrategy(strategyName, functionDefinition);
	}

	public void captureEntirePageScreenshot(String filename, String kwargs) {
		selenium.captureEntirePageScreenshot(filename, kwargs);
	}

	public void rollup(String rollupName, String kwargs) {
		selenium.rollup(rollupName, kwargs);
	}

	public void addScript(String scriptContent, String scriptTagId) {
		selenium.addScript(scriptContent, scriptTagId);
	}

	public void removeScript(String scriptTagId) {
		selenium.removeScript(scriptTagId);
	}

	public void useXpathLibrary(String libraryName) {
		selenium.useXpathLibrary(libraryName);
	}

	public void setContext(String context) {
		selenium.setContext(context);
	}

	public void attachFile(String fieldLocator, String fileLocator) {
		selenium.attachFile(fieldLocator, fileLocator);
	}

	public void captureScreenshot(String filename) {
		selenium.captureScreenshot(filename);
	}

	public String captureScreenshotToString() {
		return selenium.captureScreenshotToString();
	}

	public String captureEntirePageScreenshotToString(String kwargs) {
		return selenium.captureEntirePageScreenshotToString(kwargs);
	}

	public void shutDownSeleniumServer() {
		selenium.shutDownSeleniumServer();
	}

	public String retrieveLastRemoteControlLogs() {
		return selenium.retrieveLastRemoteControlLogs();
	}

	public void keyDownNative(String keycode) {
		selenium.keyDownNative(keycode);
	}

	public void keyUpNative(String keycode) {
		selenium.keyUpNative(keycode);
	}

	public void keyPressNative(String keycode) {
		selenium.keyPressNative(keycode);
	}
}
