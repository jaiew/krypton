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
package com.thoughtworks.twist.driver.web.selenium;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.twist.driver.web.browser.BrowserSession;
import com.thoughtworks.twist.driver.web.browser.BrowserSessionFactory;
import com.thoughtworks.twist.driver.web.browser.Decorators;
import com.thoughtworks.twist.driver.web.browser.locator.DomLocatorStrategy;
import com.thoughtworks.twist.driver.web.browser.locator.ElementNotFoundException;
import com.thoughtworks.twist.driver.web.browser.locator.XPathLocatorStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.AjaxWaitStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.DocumentReadyWaitStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.LocationChangedWaitStrategy;
import com.thoughtworks.twist.driver.web.selenium.dialog.AlertHandler;
import com.thoughtworks.twist.driver.web.selenium.dialog.ConfirmationHandler;
import com.thoughtworks.twist.driver.web.selenium.dialog.DialogHandler;
import com.thoughtworks.twist.driver.web.selenium.dialog.PromptHandler;
import com.thoughtworks.twist.driver.web.selenium.locator.AltLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.ClassLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.CssSelectorLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.IdLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.IdentifierLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.LinkLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.locator.NameLocatorStrategy;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.ExactMatcher;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.GlobMatcher;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.RegexpMatcher;
import com.thoughtworks.twist.driver.web.selenium.stringmatch.StringMatcher;
import com.thoughtworks.twist.driver.web.user.User;
import com.thoughtworks.twist.driver.web.user.UserFactory;

import static java.util.Arrays.*;

public class TwistSelenium implements Selenium {
	private BrowserSession session;
	private String title;
	private List<StringMatcher> stringMatchers = new ArrayList<StringMatcher>();

	private String browserUrl;

	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}

	private int speed;

	private AlertHandler alertHandler;
	private ConfirmationHandler confirmationHandler;
	private PromptHandler promptHandler;
	private AjaxWaitStrategy ajaxWaitStrategy;
	private LocationChangedWaitStrategy locationChangedWaitStrategy;
	private User user;

	public TwistSelenium(String browserURL) {
		this(browserURL, BrowserSessionFactory.create());
	}

	public TwistSelenium(String browserUrl, BrowserSession session) {
		this.browserUrl = browserUrl;
		this.session = session;
		User plainUser = new UserFactory().createUser(session.getBrowser().getShell());
		User logginUser = Decorators.wrapWithLogging(User.class, plainUser);
		this.user = Decorators.wrapWithSWTThreading(User.class, logginUser);

		addWaitStrategies(session);
		addLocatorStrategies(session);

		addStringMatchers();

		addDialogHandlers(session);

		addTitleListenerToTrackCurrentTitle(session);
		addLocationListenerToTrackCurrentUrl(session);
	}

	private void addDialogHandlers(BrowserSession session) {
		alertHandler = new AlertHandler(session);
		confirmationHandler = new ConfirmationHandler(session);
		promptHandler = new PromptHandler(session);
	}

	public void addLocationStrategy(String strategyName, String functionDefinition) {
		throw new UnsupportedOperationException();
	}

	public void addSelection(String locator, String optionLocator) {
		doSelection(locator, optionLocator, true, true);
	}

	public void allowNativeXpath(String allow) {
	}

	public void altKeyDown() {
		throw new UnsupportedOperationException();
	}

	public void altKeyUp() {
		throw new UnsupportedOperationException();
	}

	public void answerOnNextPrompt(String answer) {
		promptHandler.answerOnNextPrompt(answer);
	}

	public void assignId(String locator, String identifier) {
		session.execute(session.domExpression(locate(locator)) + ".setAttribute('id', '" + identifier + "')");
	}

	public void attachFile(String fieldLocator, String fileLocator) {
		throw new UnsupportedOperationException();
	}

	public void captureEntirePageScreenshot(String filename) {
		throw new UnsupportedOperationException();
	}

	public void captureScreenshot(String filename) {
		throw new UnsupportedOperationException();
	}

	public void check(String locator) {
		if (!isChecked(locator)) {
			click(locator);
		}
	}

	public void chooseCancelOnNextConfirmation() {
		confirmationHandler.chooseCancelOnNextConfirmation();
	}

	public void chooseOkOnNextConfirmation() {
		confirmationHandler.chooseOkOnNextConfirmation();
	}

	public void click(String locator) {
		click(locate(locator));
	}

	public void clickAt(String locator, String coordString) {
		Rectangle rectangle = session.boundingRectangle(locate(locator));
		String[] coordinates = coordString.split(",");
		user.click(rectangle.x + Integer.parseInt(coordinates[0]), rectangle.y + Integer.parseInt(coordinates[1]));
		waitForIdle();
	}

	public void close() {
		session.getBrowser().getShell().setVisible(false);
	}

	public void contextMenu(String locator) {
		throw new UnsupportedOperationException();
	}

	public void contextMenuAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void controlKeyDown() {
		throw new UnsupportedOperationException();
	}

	public void controlKeyUp() {
		throw new UnsupportedOperationException();
	}

	public void createCookie(String nameValuePair, String optionsString) {
		throw new UnsupportedOperationException();
	}

	public void deleteAllVisibleCookies() {
		throw new UnsupportedOperationException();
	}

	public void deleteCookie(String name, String optionsString) {
		throw new UnsupportedOperationException();
	}

	public void doubleClick(String locator) {
		Point center = session.center(locate(locator));
		user.doubleClick(center.x, center.y);
		waitForIdle();
	}

	public void doubleClickAt(String locator, String coordString) {
		Rectangle rectangle = session.boundingRectangle(locate(locator));
		String[] coordinates = coordString.split(",");
		user.doubleClick(rectangle.x + Integer.parseInt(coordinates[0]), rectangle.y + Integer.parseInt(coordinates[1]));
		waitForIdle();
	}

	public void dragAndDrop(String locator, String movementsString) {
		Point source = session.center(locate(locator));
		String[] movement = movementsString.split(",");
		user.dragAndDrop(source.x, source.y, source.x + Integer.parseInt(movement[0]), source.y + Integer.parseInt(movement[1]));
		waitForIdle();
	}

	public void dragAndDropToObject(String locatorOfObjectToBeDragged, String locatorOfDragDestinationObject) {
		Point source = session.center(locate(locatorOfObjectToBeDragged));
		Point destination = session.center(locate(locatorOfDragDestinationObject));
		user.dragAndDrop(source.x, source.y, destination.x, destination.y);
		waitForIdle();
	}

	public void dragdrop(String locator, String movementsString) {
		dragAndDrop(locator, movementsString);
	}

	public void fireEvent(String locator, String eventName) {
		fireEvent(locate(locator), eventName);
	}

	private void fireEvent(Element element, String eventName) {
		session.fireEvent(element, eventName);
		waitForIdle();
	}

	public void focus(String locator) {
		session.execute(session.domExpression(locate(locator)) + ".focus();");
	}

	public String getAlert() {
		waitForIdle();
		if (!alertHandler.isDialogPresent()) {
			throw new IllegalStateException("No alert present");
		}
		return alertHandler.getMessage();
	}

	public String[] getAllButtons() {
		throw new UnsupportedOperationException();
	}

	public String[] getAllFields() {
		throw new UnsupportedOperationException();
	}

	public String[] getAllLinks() {
		throw new UnsupportedOperationException();
	}

	public String[] getAllWindowIds() {
		throw new UnsupportedOperationException();
	}

	public String[] getAllWindowNames() {
		throw new UnsupportedOperationException();
	}

	public String[] getAllWindowTitles() {
		throw new UnsupportedOperationException();
	}

	public String getAttribute(String attributeLocator) {
		int indexOfAt = attributeLocator.lastIndexOf("@");
		String locator = attributeLocator.substring(0, indexOfAt);
		if (new XPathLocatorStrategy().canLocate(locator) && locator.endsWith("/")) {
			locator = locator.substring(0, locator.length() - 1);
		}
		String attribute = attributeLocator.substring(indexOfAt + 1);
		Element element = locate(locator);
		return element.getAttribute(attribute);
	}

	public String[] getAttributeFromAllWindows(String attributeName) {
		throw new UnsupportedOperationException();
	}

	public String getBodyText() {
		return getText("document.body");
	}

	public String getConfirmation() {
		waitForIdle();
		if (!confirmationHandler.isDialogPresent()) {
			throw new IllegalStateException("No confirmation present");
		}
		return confirmationHandler.getMessage();
	}

	public String getCookie() {
		throw new UnsupportedOperationException();
	}

	public String getCookieByName(String name) {
		throw new UnsupportedOperationException();
	}

	public Number getCursorPosition(String locator) {
		Element element = locate(locator);
		return session.getCursorPosition(element);
	}

	public Number getElementHeight(String locator) {
		return session.boundingRectangle(locate(locator)).height;
	}

	public Number getElementIndex(String locator) {
		Element element = locate(locator);
		NodeList childNodes = element.getParentNode().getChildNodes();
		int index = 0;
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() == Element.ELEMENT_NODE) {
				index++;
			}
			if (element == childNodes.item(i)) {
				return index - 1;
			}
		}
		throw new IllegalStateException("Not a child of its parent: " + element);
	}

	public Number getElementPositionLeft(String locator) {
		return session.boundingRectangle(locate(locator)).x;
	}

	public Number getElementPositionTop(String locator) {
		return session.boundingRectangle(locate(locator)).y;
	}

	public Number getElementWidth(String locator) {
		return session.boundingRectangle(locate(locator)).width;
	}

	public String getEval(String script) {
		return getExpression(script);
	}

	public String getExpression(String expression) {
		try {
			return session.execute(expression);
		} finally {
			waitForIdle();
		}
	}

	public String getHtmlSource() {
		try {
			StringWriter htmlAsString = new StringWriter();
			OutputFormat format = new OutputFormat();
			format.setOmitXMLDeclaration(true);
			new XMLSerializer(htmlAsString, format).serialize(session.dom());
			return htmlAsString.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getLocation() {
		return session.execute(session.getDocumentExpression() + ".location");
	}

	public Number getMouseSpeed() {
		throw new UnsupportedOperationException();
	}

	public String getPrompt() {
		waitForIdle();
		if (!promptHandler.isDialogPresent()) {
			throw new IllegalStateException("No prompt present");
		}
		return promptHandler.getMessage();
	}

	public String[] getSelectOptions(String selectLocator) {
		Element select = locate(selectLocator);
		List<String> selectOptions = new ArrayList<String>();
		NodeList options = select.getElementsByTagName("option");
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			selectOptions.add(option.getTextContent());
		}
		return selectOptions.toArray(new String[0]);
	}

	public String getSelectedId(String selectLocator) {
		return getSelectedIds(selectLocator)[0];
	}

	public String[] getSelectedIds(String selectLocator) {
		Element select = locate(selectLocator);
		List<String> selectOptions = new ArrayList<String>();
		NodeList options = select.getElementsByTagName("option");
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			if (Boolean.parseBoolean(option.getAttribute("selected"))) {
				selectOptions.add(option.getAttribute("id"));
			}
		}
		return selectOptions.toArray(new String[0]);
	}

	public String getSelectedIndex(String selectLocator) {
		return getSelectedIndexes(selectLocator)[0];
	}

	public String[] getSelectedIndexes(String selectLocator) {
		Element select = locate(selectLocator);
		List<String> selectOptions = new ArrayList<String>();
		NodeList options = select.getElementsByTagName("option");
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			if (Boolean.parseBoolean(option.getAttribute("selected"))) {
				selectOptions.add(i + "");
			}
		}
		return selectOptions.toArray(new String[0]);
	}

	public String getSelectedLabel(String selectLocator) {
		return getSelectedLabels(selectLocator)[0];
	}

	public String[] getSelectedLabels(String selectLocator) {
		Element select = locate(selectLocator);
		List<String> selectOptions = new ArrayList<String>();
		NodeList options = select.getElementsByTagName("option");
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			if (Boolean.parseBoolean(option.getAttribute("selected"))) {
				selectOptions.add(option.getTextContent());
			}
		}
		return selectOptions.toArray(new String[0]);
	}

	public String getSelectedValue(String selectLocator) {
		return getSelectedValues(selectLocator)[0];
	}

	public String[] getSelectedValues(String selectLocator) {
		Element select = locate(selectLocator);
		List<String> selectOptions = new ArrayList<String>();
		NodeList options = select.getElementsByTagName("option");
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			if (Boolean.parseBoolean(option.getAttribute("selected"))) {
				selectOptions.add(option.getAttribute("value"));
			}
		}
		return selectOptions.toArray(new String[0]);
	}

	public String getSpeed() {
		return speed + "";
	}

	public String getTable(String tableCellAddress) {
		int columnIndex = tableCellAddress.lastIndexOf(".");
		int column = Integer.parseInt(tableCellAddress.substring(columnIndex + 1));
		tableCellAddress = tableCellAddress.substring(0, columnIndex);

		int rowIndex = tableCellAddress.lastIndexOf(".");
		int row = Integer.parseInt(tableCellAddress.substring(rowIndex + 1));

		tableCellAddress = tableCellAddress.substring(0, rowIndex);

		Element table = locate(tableCellAddress);
		Element rowElement = (Element) table.getElementsByTagName("tr").item(row);
		NodeList cells = rowElement.getElementsByTagName("td");
		if (cells.getLength() == 0) {
			cells = rowElement.getElementsByTagName("th");
		}
		return session.getText((Element) cells.item(column));
	}

	public String getText(String locator) {
		return session.getText(locate(locator));
	}

	public String getTitle() {
		// waitForIdle();
		return session.execute(session.getDocumentExpression() + ".title").trim();
	}

	public String getValue(String locator) {
		return locate(locator).getAttribute("value");
	}

	public boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target) {
		throw new UnsupportedOperationException();
	}

	public boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target) {
		throw new UnsupportedOperationException();
	}

	public Number getXpathCount(String xpath) {
		return session.locateAll(xpath).getLength();
	}

	public void goBack() {
		session.getBrowser().back();
		waitForIdle();
	}

	public void highlight(String locator) {
		throw new UnsupportedOperationException();
	}

	public void ignoreAttributesWithoutValue(String ignore) {
	}

	public boolean isAlertPresent() {
		waitForIdle();
		return alertHandler.isDialogPresent();
	}

	public boolean isChecked(String locator) {
		return Boolean.parseBoolean(locate(locator).getAttribute("checked"));
	}

	public boolean isConfirmationPresent() {
		waitForIdle();
		return confirmationHandler.isDialogPresent();
	}

	public boolean isCookiePresent(String name) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String locator) {
		Element element = locate(locator);
		String tagName = element.getTagName().toLowerCase();
		boolean editable = !(Boolean.parseBoolean(element.getAttribute("disabled")) || Boolean.parseBoolean(element
				.getAttribute("readonly")));
		editable = editable && asList("input", "select", "textarea").contains(tagName);
		return editable;
	}

	public boolean isElementPresent(String locator) {
		try {
			locate(locator);
			return true;
		} catch (ElementNotFoundException e) {
			return false;
		}
	}

	public boolean isOrdered(String locator1, String locator2) {
		Element firstElement = locate(locator1);
		Element secondElement = locate(locator2);

		if (!firstElement.isEqualNode(secondElement) && firstElement.getParentNode().isEqualNode(secondElement.getParentNode())) {
			return indexOfNode(firstElement) < indexOfNode(secondElement);
		}
		return false;
	}

	private int indexOfNode(Element element) {
		int index = 0;
		NodeList siblings = element.getParentNode().getChildNodes();
		for (; index < siblings.getLength(); index++) {
			if (siblings.item(index) == element) {
				break;
			}
		}
		return index;
	}

	public boolean isPromptPresent() {
		waitForIdle();
		return promptHandler.isDialogPresent();
	}

	public boolean isSomethingSelected(String selectLocator) {
		return getSelectedIndexes(selectLocator).length > 0;
	}

	public boolean isTextPresent(String pattern) {
		String text = getBodyText();
		for (StringMatcher matcher : stringMatchers) {
			if (matcher.canMatch(pattern)) {
				return matcher.matches(pattern, text);
			}
		}
		return false;
	}

	public boolean isVisible(String locator) {
		return session.isVisible(locate(locator));
	}

	public void keyDown(String locator, String keySequence) {
		throw new UnsupportedOperationException();
	}

	public void keyDownNative(String keycode) {
		throw new UnsupportedOperationException();
	}

	public void keyPress(String locator, String keySequence) {
		focus(locator);
		if (keySequence.startsWith("\\")) {
			keySequence = keySequence.substring("\\".length());
			int keyCode = (char) Integer.parseInt(keySequence);

			switch (keyCode) {
			case KeyEvent.VK_DOWN:
				keyCode = SWT.ARROW_DOWN;
				break;
			case KeyEvent.VK_UP:
				keyCode = SWT.ARROW_UP;
				break;
			case KeyEvent.VK_LEFT:
				keyCode = SWT.ARROW_LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				keyCode = SWT.ARROW_RIGHT;
				break;
			case KeyEvent.VK_PAGE_UP:
				keyCode = SWT.PAGE_UP;
				break;
			case KeyEvent.VK_PAGE_DOWN:
				keyCode = SWT.PAGE_DOWN;
				break;
			case KeyEvent.VK_HOME:
				keyCode = SWT.HOME;
				break;
			case KeyEvent.VK_END:
				keyCode = SWT.END;
				break;
			}
			user.key(keyCode);
		} else {
			user.key(keySequence.charAt(0));
		}
		waitForIdle();
	}

	public void keyPressNative(String keycode) {
		throw new UnsupportedOperationException();
	}

	public void keyUp(String locator, String keySequence) {
		throw new UnsupportedOperationException();
	}

	public void keyUpNative(String keycode) {
		throw new UnsupportedOperationException();
	}

	public void metaKeyDown() {
		throw new UnsupportedOperationException();
	}

	public void metaKeyUp() {
		throw new UnsupportedOperationException();
	}

	public void mouseDown(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseDownAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void mouseMove(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseMoveAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void mouseOut(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseOver(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseUp(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseUpAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void open(String url) {
		try {
			checkModalDialogs();
			start();
			if (url.startsWith("/")) {
				URL realUrl = new URL(browserUrl);
				url = new URL(realUrl.getProtocol(), realUrl.getHost(), realUrl.getPort(), url).toExternalForm();
			}
			boolean isSameURL = url.equals(getLocation());
			if (isSameURL) {
				addLocationURLExclusionPattern(url);
			}
			session.execute(session.getDocumentExpression() + ".location = '" + url + "'");
			waitForIdle();
			if (isSameURL) {
				removeLocationURLExclusionPattern(url);
			}
			if (title != null && title.contains("404")) {
				throw new IllegalArgumentException("Page not found: " + url);
			}

		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private void checkModalDialogs() {
		if (isAlertPresent() || isConfirmationPresent() || isPromptPresent()) {
			throw new IllegalStateException("Modal dialog present");
		}
	}

	public void openWindow(String url, String windowID) {
		throw new UnsupportedOperationException();
	}

	public void refresh() {
		session.getBrowser().refresh();
		waitForIdle();
	}

	public void removeAllSelections(String locator) {
		doSelection(locator, "", false, false);
	}

	public void removeSelection(String locator, String optionLocator) {
		doSelection(locator, optionLocator, true, false);
	}

	public void runScript(String script) {
		throw new UnsupportedOperationException();
	}

	public void select(String selectLocator, String optionLocator) {
		doSelection(selectLocator, optionLocator, false, true);
	}

	private void doSelection(String selectLocator, String optionLocator, boolean multiselect, boolean shouldSelect) {
		Element select = locate(selectLocator);

		String indexPrefix = "index=";
		String labelPrefix = "label=";
		String valuePrefix = "value=";
		String idPrefix = "id=";

		if (!Boolean.parseBoolean(select.getAttribute("multiple")) && multiselect) {
			throw new IllegalArgumentException("Not a multi select: " + selectLocator);
		}

		fireEvent(select, "focus");

		int currentIndex = 0;
		boolean wasSelected = false;
		NodeList childNodes = select.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() != Element.ELEMENT_NODE) {
				continue;
			}
			Element option = (Element) childNodes.item(i);
			if (option.getNodeType() == Element.ELEMENT_NODE) {
				currentIndex++;
			}
			if (!multiselect) {
				setSelected(option, false);
			}
			if (optionLocator.startsWith(indexPrefix)) {
				int index = Integer.parseInt(optionLocator.substring(indexPrefix.length()));
				if (currentIndex - 1 == index) {
					wasSelected = true;
					setSelected(option, shouldSelect);
					break;
				}
			} else if (optionLocator.startsWith(valuePrefix)) {
				if (optionLocator.substring(valuePrefix.length()).equals(option.getAttribute("value"))) {
					wasSelected = true;
					setSelected(option, shouldSelect);
					break;
				}
			} else if (optionLocator.startsWith(idPrefix)) {
				if (optionLocator.substring(idPrefix.length()).equals(option.getAttribute("id"))) {
					wasSelected = true;
					setSelected(option, shouldSelect);
					break;
				}
			} else if (optionLocator.startsWith(labelPrefix)) {
				if (option.getTextContent().matches(optionLocator.substring(labelPrefix.length()))) {
					wasSelected = true;
					setSelected(option, shouldSelect);
					break;
				}
			} else {
				if (option.getTextContent().matches(optionLocator)) {
					wasSelected = true;
					setSelected(option, shouldSelect);
					break;
				}
			}
		}
		if (wasSelected) {
			fireEvent(select, "change");
		} else {
			throw new IllegalArgumentException("No such option: " + optionLocator);
		}
	}

	private void setSelected(Element element, boolean value) {
		setProperty(element, "selected", "" + value);
	}

	private void setProperty(Element element, String property, String value) {
		session.execute(session.domExpression(element) + "." + property + " = " + value);
	}

	public void selectFrame(String locator) {
		if (locator.startsWith("index=")) {
			int index = Integer.parseInt(locator.substring(locator.indexOf("=") + 1));
			session.setWindowExpression(session.getWindowExpression() + ".frames[" + index + "]");
		} else if (locator.startsWith("relative=")) {
			String relation = locator.substring(locator.indexOf("=") + 1);
			if ("top".equals(relation)) {
				session.setWindowExpression("window");
				return;
			} else if ("parent".equals(relation) || "up".equals(relation)) {
				session.setWindowExpression(session.getWindowExpression() + ".parent");
			}
		} else if (new DomLocatorStrategy().canLocate(locator)) {
			if (locator.startsWith("dom=")) {
				locator = locator.substring("dom=".length());
			}
			if (!"undefined".equals(session.execute("typeof " + locator + ".tagName"))) {
				session.setWindowExpression(locator + ".contentWindow");
			} else {
				session.setWindowExpression(locator);
			}
		} else {
			Element frame = locate(locator);
			String domExpression = session.domExpression(frame);
			session.setWindowExpression(domExpression + ".contentWindow");
		}
	}

	public void selectWindow(String windowID) {
		throw new UnsupportedOperationException();
	}

	public void setBrowserLogLevel(String logLevel) {
		throw new UnsupportedOperationException();
	}

	public void setContext(String context) {
	}

	public void setCursorPosition(String locator, String position) {
		Element element = locate(locator);
		// fireEvent(element, "focus");
		session.setCursorPosition(element, Integer.parseInt(position));
	}

	public void setMouseSpeed(String pixels) {
		throw new UnsupportedOperationException();
	}

	public void setSpeed(String value) {
		this.speed = Integer.parseInt(value);
		if (speed < 0) {
			speed = 0;
		}
	}

	public void setTimeout(String timeout) {
		session.setEventLoopTimeout(Integer.parseInt(timeout));
	}

	public void shiftKeyDown() {
		user.shiftDown();
	}

	public void shiftKeyUp() {
		user.shiftUp();
	}

	public void shutDownSeleniumServer() {
		throw new UnsupportedOperationException();
	}

	public void start() {
		session.openBrowser();
	}

	public void stop() {
		session.closeBrowser();
	}

	public void submit(String formLocator) {
		Element form = locate(formLocator);
		NodeList inputs = form.getElementsByTagName("input");
		for (int i = 0; i < inputs.getLength(); i++) {
			Element item = (Element) inputs.item(i);
			if ("submit".equals(item.getAttribute("type"))) {
				click(item);
				break;
			}
		}
	}

	public void type(String locator, String value) {
		session.execute(session.domExpression(locate(locator)) + ".value = ''");
		typeKeys(locator, value);
	}

	public void typeKeys(String locator, String value) {
		click(locator);

		// Don't like this, but it makes it more consistent in IE.
		focus(locator);
		user.type(value);
		waitForIdle();
	}

	public void uncheck(String locator) {
		if (isChecked(locator)) {
			click(locator);
		}
	}

	public void waitForCondition(String script, String timeout) {
		throw new UnsupportedOperationException();
	}

	public void waitForFrameToLoad(String frameAddress, String timeout) {
	}

	public void waitForPageToLoad(String timeout) {
	}

	public void waitForPopUp(String windowID, String timeout) {
		throw new UnsupportedOperationException();
	}

	public void windowFocus() {
		session.getBrowser().forceFocus();
	}

	public void windowMaximize() {
		session.getBrowser().getShell().setMaximized(true);
	}

	public BrowserSession getBrowserSession() {
		return session;
	}

	public void addAjaxURLExclusionPattern(String pattern) {
		ajaxWaitStrategy.addURLExclusionPattern(pattern);
	}

	public void addLocationURLExclusionPattern(String pattern) {
		locationChangedWaitStrategy.addURLExclusionPattern(pattern);
	}

	private void removeLocationURLExclusionPattern(String pattern) {
		locationChangedWaitStrategy.removeURLExclusionPattern(pattern);
	}

	public void setExcludedWaitURLs(String urls) {
		for (String pattern : urls.split(",")) {
			addLocationURLExclusionPattern(pattern);
		}
	}

	private Element locate(String locator) {
		waitForIdle();
		Element locate = session.locate(locator);
		session.scrollIntoView(locate);
		return locate;
	}

	private void click(Element element) {
		Point center = session.center(element);
		user.click(center.x, center.y);
		waitForIdle();
		// showPoint(center);
	}

	// private void showPoint(Point point) {
	// session.execute(
	// "function() { var div = document.createElement('div'); div.setAttribute('style', 'position: absolute; border: 1px solid red; top: "
	// + (point.y - 2) + "; left: " + (point.x - 2) +
	// "; width: 5px; height: 5px;'); document.body.appendChild(div); setTimeout(function() {div.parentNode.removeChild(div);}, 2000);}()"
	// );
	// }

	private void addStringMatchers() {
		stringMatchers.add(new RegexpMatcher());
		stringMatchers.add(new ExactMatcher());
		stringMatchers.add(new GlobMatcher());
	}

	private void addWaitStrategies(final BrowserSession session) {
		locationChangedWaitStrategy = new LocationChangedWaitStrategy();
		session.addWaitStrategy(locationChangedWaitStrategy);
		ajaxWaitStrategy = new AjaxWaitStrategy();
		session.addWaitStrategy(ajaxWaitStrategy);
		session.addWaitStrategy(new DocumentReadyWaitStrategy());
	}

	private void addLocatorStrategies(final BrowserSession session) {
		session.addLocatorStrategy(new DomLocatorStrategy());
		session.addLocatorStrategy(new CssSelectorLocatorStrategy());
		session.addLocatorStrategy(new XPathLocatorStrategy());
		session.addLocatorStrategy(new LinkLocatorStrategy());
		session.addLocatorStrategy(new IdLocatorStrategy());
		session.addLocatorStrategy(new NameLocatorStrategy());
		session.addLocatorStrategy(new AltLocatorStrategy());
		session.addLocatorStrategy(new ClassLocatorStrategy());
		session.addLocatorStrategy(new IdentifierLocatorStrategy());
	}

	private void addTitleListenerToTrackCurrentTitle(final BrowserSession session) {
		session.getBrowser().addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
				title = event.title;
			}
		});
	}

	private void addLocationListenerToTrackCurrentUrl(final BrowserSession session) {
		session.getBrowser().addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				browserUrl = event.location;
			}

			public void changing(LocationEvent event) {
			}
		});
	}

	private void waitForIdle() {
		session.waitForIdle();
	}

	public void closeAllDialogs() {
		for (DialogHandler handler : getDialogHandlers()) {
			while (handler.isDialogPresent()) {
				handler.getMessage();
			}
		}
	}

	private DialogHandler[] getDialogHandlers() {
		return new DialogHandler[] { alertHandler, confirmationHandler, promptHandler };
	}

	public void addScript(String scriptContent, String scriptTagId) {
		throw new UnsupportedOperationException();
	}

	public void captureEntirePageScreenshot(String filename, String kwargs) {
		throw new UnsupportedOperationException();
	}

	public String captureEntirePageScreenshotToString(String kwargs) {
		throw new UnsupportedOperationException();
	}

	public String captureScreenshotToString() {
		throw new UnsupportedOperationException();
	}

	public void mouseDownRight(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseDownRightAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void mouseUpRight(String locator) {
		throw new UnsupportedOperationException();
	}

	public void mouseUpRightAt(String locator, String coordString) {
		throw new UnsupportedOperationException();
	}

	public void removeScript(String scriptTagId) {
		throw new UnsupportedOperationException();
	}

	public String retrieveLastRemoteControlLogs() {
		throw new UnsupportedOperationException();
	}

	public void rollup(String rollupName, String kwargs) {
		throw new UnsupportedOperationException();
	}

	public void setExtensionJs(String extensionJs) {
		throw new UnsupportedOperationException();
	}

	public void showContextualBanner() {
		throw new UnsupportedOperationException();
	}

	public void showContextualBanner(String className, String methodName) {
		throw new UnsupportedOperationException();
	}

	public void start(String optionsString) {
		throw new UnsupportedOperationException();
	}

	public void start(Object optionsObject) {
		throw new UnsupportedOperationException();
	}

	public void useXpathLibrary(String libraryName) {
		throw new UnsupportedOperationException();
	}
}
