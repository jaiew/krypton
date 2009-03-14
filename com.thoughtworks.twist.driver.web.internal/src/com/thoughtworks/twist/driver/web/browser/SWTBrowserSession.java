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
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Parser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.tools.javac.util.Position;
import com.thoughtworks.twist.driver.web.browser.jsmin.JSMin;
import com.thoughtworks.twist.driver.web.browser.locator.ElementNotFoundException;
import com.thoughtworks.twist.driver.web.browser.locator.LocatorStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.WaitStrategy;
import com.thoughtworks.twist.driver.web.browser.wait.WaitTimedOutException;

public class SWTBrowserSession implements BrowserSession {
	private static final String[] BOOLEAN_ATTRIBUTES = { "checked", "selected", "disabled", "readonly", "multiple" };

	private static final String XHTML1_STRICT_DOCTYPE = "<!DOCTYPE html\n" + "PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
			+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";

	private static final int DEFAULT_EVENTLOOP_TIMEOUT = 10 * 1000;

	private static Map<String, String> resourcesByName = new HashMap<String, String>();
	private static Set<String> verifiedJavaScripts = new HashSet<String>();
	private static Map<String, String> minifiedJavaScripts = new HashMap<String, String>();
	private static Map<String, XPathExpression> compiledXPaths = new HashMap<String, XPathExpression>();

	private Shell shell;
	private DocumentBuilder documentBuilder;

	private Browser browser;

	private List<LocatorStrategy> locatorStrategies = new ArrayList<LocatorStrategy>();
	private List<WaitStrategy> waitStrategies = new ArrayList<WaitStrategy>();

	private int eventLoopTimeout = DEFAULT_EVENTLOOP_TIMEOUT;

	private XPath xpath;
	private DOMParser parser;

	private Document document;

	private String windowExpression = "window";

	private Logger log = LoggerFactory.getLogger(getClass());

	private BrowserFamily browserFamily;

	public SWTBrowserSession(Browser browser, BrowserFamily browserFamily) {
		this.browserFamily = browserFamily;
		try {
			this.shell = browser.getShell();
			this.browser = browser;

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			documentBuilder = factory.newDocumentBuilder();
			documentBuilder.setEntityResolver(new LocalEntityResolver());

			xpath = XPathFactory.newInstance().newXPath();

			HTMLConfiguration configuration = new HTMLConfiguration();
			configuration.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
			configuration.setProperty("http://cyberneko.org/html/properties/names/attrs", "lower");

			parser = new DOMParser(configuration);

			log.info("Created BrowserSession using browser {}", getBrowserFamily());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void openBrowser() {
		log.debug("Opening Browser");
		if (!shell.isVisible()) {
			shell.setVisible(Boolean.parseBoolean(System.getProperty("twist.driver.web.visible", "true")));
			shell.setFullScreen(Boolean.parseBoolean(System.getProperty("twist.driver.web.fullscreen", "false")));
			shell.setMinimized(Boolean.parseBoolean(System.getProperty("twist.driver.web.minimized", "false")));
			shell.setMaximized(Boolean.parseBoolean(System.getProperty("twist.driver.web.maximized", "false")));
		}
	}

	public void closeBrowser() {
		log.debug("Closing Browser");
		if (!shell.isDisposed()) {
			shell.close();
			shell.dispose();
			browser.dispose();
		}
	}

	public synchronized void execute(String statements) {
		verifyJavaScript(statements);
		browser.execute(statements);
	}

	public synchronized String evaluate(String expression) {
		return new StatusTransport().evaluate(expression);
	}

	public void inject(String script) {
		inject(script, getClass());
	}

	public void inject(final String script, final Class<?> baseClass) {
		log.trace("Injecting {} base class is {}", script, baseClass);
		String code = minifiedJavaScripts.get(script);
		if (code == null) {
			code = readResource(script, baseClass);
			verifyJavaScript(code);
			code = minifyJavaScript(script, code);
			minifiedJavaScripts.put(script, code);
		}
		browser.execute(code);
	}

	public BrowserFamily getBrowserFamily() {
		return browserFamily;
	}

	public Document dom() {
		if (document != null) {
			return document;
		}
		String dom = "";
		try {
			inject("twist-dom.js");
			return parseDOMUsingInnerHTML();
		} catch (Exception e) {
			log.error(dom, e);
			throw new RuntimeException(e);
		}
	}

	private Document parseDOMUsingInnerHTML() throws SAXException, IOException {
		long now = System.currentTimeMillis();
		String dom = evaluate("Twist.domFromInnerHTML(" + getDocumentExpression() + ".documentElement)");
		parser.parse(new InputSource(new StringReader(dom)));
		document = parser.getDocument();
		postProcessAttributes(document);
		log.warn("innerHTML took: {} ms. ({} chars)", (System.currentTimeMillis() - now), dom.length());
		return document;
	}

	private Document parseDOMUsingConstructedXML() throws SAXException, IOException {
		long now = System.currentTimeMillis();
		String dom = XHTML1_STRICT_DOCTYPE + domAsString();
		document = documentBuilder.parse(new InputSource(new StringReader(dom)));
		log.warn("Parsing DOM took: {} ms. ({} chars)", (System.currentTimeMillis() - now), dom.length());
		return document;
	}

	private void postProcessAttributes(Document document) {
		NodeList allElements = document.getElementsByTagName("*");
		for (int i = 0; i < allElements.getLength(); i++) {
			Element element = (Element) allElements.item(i);
			patchId(element);
			patchAttributesForIE(element);
		}
	}

	private void patchAttributesForIE(Element element) {
		if (browserFamily == BrowserFamily.IE) {
			String tagName = element.getTagName();
			String type = element.getAttribute("type");
			String domExpression = domExpression(element);
			for (String attribute : BOOLEAN_ATTRIBUTES) {
				element.setAttribute(attribute, element.hasAttribute(attribute) + "");
			}
			if ("input".equals(tagName)) {
				if ("password".equals(type)) {
					element.setAttribute("value", evaluate(domExpression + ".value"));
				}
				if ("".equals(type)) {
					element.setAttribute("type", "text");
				}
				if (!element.hasAttribute("value")) {
					element.setAttribute("value", "");
				}
			}
		}
	}

	private void patchId(Element element) {
		Attr id = element.getAttributeNode("id");
		if (id != null) {
			element.setIdAttributeNode(id, true);
		}
	}

	private String domAsString() {
		inject("twist-dom.js");
		return evaluate("Twist.dom(" + getDocumentExpression() + ".documentElement)");
	}

	public Rectangle boundingRectangle(Element element) {
		inject("twist-bounding-rectangle.js");
		String rectangle = evaluate("Twist.boundingRectangle(" + domExpression(element) + ")");
		String[] values = rectangle.split(",");
		Rectangle boundingRectangle = new Rectangle(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]),
				Integer.parseInt(values[3]));
		log.trace("Bounding rectangle of {} is {}", element, boundingRectangle);
		return boundingRectangle;
	}

	public Point center(Element element) {
		Rectangle rectangle = boundingRectangle(element);
		Point center = new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
		log.trace("Center of {}  is {}", element, center);
		return center;
	}

	public Element locate(String locator) {
		long now = System.currentTimeMillis();
		pumpEvents();
		for (LocatorStrategy strategy : locatorStrategies) {
			if (strategy.canLocate(locator)) {
				Element element = strategy.locate(this, locator);
				if (element != null) {
					log.debug("Located {} using '{}' (took {} ms.)", new Object[] {element, locator, (System.currentTimeMillis() - now)});
					return element;
				}
			}
		}
		throw new ElementNotFoundException(locator);
	}

	public List<Node> locateAll(String xpathExpression) {
		try {
			XPathExpression compiled = compiledXPaths.get(xpathExpression);
			if (compiled == null) {
				compiled = xpath.compile(xpathExpression);
				compiledXPaths.put(xpathExpression, compiled);
			}
			log.debug("Locating all elements using '{}'", xpathExpression);
			List<Node> result = new ArrayList<Node>();
			NodeList nodeList = (NodeList) compiled.evaluate(dom(), XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				result.add(nodeList.item(i));
			}
			return result;
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public void fireEvent(Element element, String eventName) {
		inject("twist-events.js", getClass());
		log.debug("Firing JavaScript event '{}' on {}",  eventName, element);
		execute("Twist.fireEvent(" + domExpression(element) + ", '" + eventName + "')");
	}

	public void setCursorPosition(Element element, int position) {
		inject("twist-cursor-position.js", getClass());
		log.debug("Setting cursor positon to {} at {}", position, element);
		execute("Twist.setCursorPosition(" + domExpression(element) + ", " + position + ")");
	}

	public int getCursorPosition(Element element) {
		inject("twist-cursor-position.js", getClass());
		int position = Integer.parseInt(evaluate("Twist.getCursorPosition(" + domExpression(element) + ")"));
		log.debug("Cursor positon is {} at {}", position, element);
		return position;
	}

	public void waitForIdle() {
		log.debug("Waiting for Browser to become idle");
		long timeout = System.currentTimeMillis() + eventLoopTimeout;
		int count = 0;
		while (!browser.isDisposed() && count < 2) {
			pumpEvents();
			if (System.currentTimeMillis() > timeout) {
				throw new WaitTimedOutException("event loop to become idle, busy strategies: " + getBusyWaitStrategies(), eventLoopTimeout);
			}
			if (areWaitStrategiesIdle()) {
				count++;
			}
		}
		emptyDocumentCache();
		pumpEvents();
	}

	public void pumpEvents() {
		while (!browser.isDisposed() && browser.getDisplay().readAndDispatch())
			;
	}

	private boolean areWaitStrategiesIdle() {
		for (WaitStrategy strategy : waitStrategies) {
			if (strategy.isBusy()) {
				return false;
			}
		}
		return true;
	}

	private List<WaitStrategy> getBusyWaitStrategies() {
		List<WaitStrategy> result = new ArrayList<WaitStrategy>();
		for (WaitStrategy strategy : waitStrategies) {
			if (strategy.isBusy()) {
				result.add(strategy);
			}
		}
		return result;
	}

	public void addLocatorStrategy(LocatorStrategy locatorStrategy) {
		locatorStrategies.add(locatorStrategy);
	}

	public void addWaitStrategy(WaitStrategy waitStrategy) {
		waitStrategy.init(this);
		waitStrategies.add(waitStrategy);
	}

	public String domExpression(Element element) {
		String expression = "";
		Element original = element;
		while (!(element.getParentNode() instanceof Document)) {
			expression = ".childNodes[" + element.getAttribute("twist.domindex") + "]" + expression;
			element = (Element) element.getParentNode();
		}
		String domExpression = getDocumentExpression() + ".documentElement" + expression;
		log.trace("DOM Expression of {} is '{}'", original, domExpression);
		return domExpression;
	}

	public void setEventLoopTimeout(int timeout) {
		this.eventLoopTimeout = timeout;
	}

	public String readResource(String resource, Class<?> baseClass) {
		String key = baseClass.getPackage().getName() + "." + resource;
		if (resourcesByName.containsKey(key)) {
			return resourcesByName.get(key);
		}
		InputStream in = null;
		try {
			InputStream resourceAsStream = baseClass.getResourceAsStream(resource);
			if (resourceAsStream == null) {
				throw new IllegalArgumentException("Could not find resource " + resource + " baseclass is " + baseClass.getName());
			}

			in = new BufferedInputStream(resourceAsStream);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b = -1;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			String resourceAsString = new String(out.toByteArray(), "UTF-8");
			resourcesByName.put(key, resourceAsString);
			return resourceAsString;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException nothingToDo) {
			}
		}
	}

	private void verifyJavaScript(String script) {
		if (verifiedJavaScripts.contains(script)) {
			return;
		}
		Context ctx = ContextFactory.getGlobal().enterContext();
		try {
			CompilerEnvirons compilerEnv = new CompilerEnvirons();
			compilerEnv.initFromContext(ctx);
			ErrorReporter compilationErrorReporter = compilerEnv.getErrorReporter();
			Parser parser = new Parser(compilerEnv, compilationErrorReporter);
			parser.parse(script, "", 1);
			verifiedJavaScripts.add(script);
		} finally {
			Context.exit();
		}
	}

	private String minifyJavaScript(String script, String code) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new JSMin(new ByteArrayInputStream(code.getBytes("UTF-8")), out).jsmin();
			String minified = new String(out.toByteArray(), "UTF-8");
			log.debug("Minified script {} from {} to {}", new Object[] {script, code.length(), minified.length()});
			return minified;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final class LocalEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (publicId.equals("-//W3C//DTD XHTML 1.0 Strict//EN")) {
				return new InputSource(new StringReader(readResource("xhtml1-strict.dtd", getClass())));
			}
			if (publicId.equals("-//W3C//ENTITIES Latin 1 for XHTML//EN")) {
				return new InputSource(new StringReader(readResource("xhtml-lat1.ent", getClass())));
			}
			if (publicId.equals("-//W3C//ENTITIES Symbols for XHTML//EN")) {
				return new InputSource(new StringReader(readResource("xhtml-symbol.ent", getClass())));
			}
			if (publicId.equals("-//W3C//ENTITIES Special for XHTML//EN")) {
				return new InputSource(new StringReader(readResource("xhtml-special.ent", getClass())));
			}
			return null;
		}
	}

	private class StatusTransport implements StatusTextListener {
		private static final String RETURN_VALUE = "return: ";
		private static final String EXCEPTION = "exception: ";

		private String returnValue;
		private JavascriptException exception;

		public void changed(StatusTextEvent event) {
			if (event.text.startsWith(RETURN_VALUE)) {
				returnValue = event.text.substring(RETURN_VALUE.length());
			}
			if (event.text.startsWith(EXCEPTION)) {
				exception = new JavascriptException(event.text.substring(EXCEPTION.length()));
			}
		}

		public String evaluate(final String expression) {
			log.debug("Executing JavaScript: {}", expression);
			String script = "try { window.status = '" + RETURN_VALUE + "' + (" + expression + ");} catch (e) { window.status = '"
					+ EXCEPTION + "' + e; }";
			verifyJavaScript(script);

			getBrowser().addStatusTextListener(StatusTransport.this);
			pumpEvents();
			getBrowser().execute(script);
			pumpEvents();
			getBrowser().removeStatusTextListener(StatusTransport.this);

			if (exception != null) {
				exception.fillInStackTrace();
				log.debug("Caught JavaScript exception", exception);
				throw exception;
			}
			log.debug("JavaScript returned: {}", returnValue);
			return returnValue;
		}
	}

	public String getText(Node node) {
		String text = normalizeNewlines(escapeAposForIE(getText(node, false))).trim();
		log.debug("Text of {} is: {}", node, text);
		return text;
	}

	private String escapeAposForIE(String text) {
		return text.replaceAll("&apos;", "'");
	}

	private String getText(Node node, boolean preformatted) {
		if (Node.TEXT_NODE == node.getNodeType()) {
			String text = node.getTextContent();
			if (!preformatted) {
				text = normalizeSpaces(text);
			}
			return text;
		}
		if (Node.ELEMENT_NODE == node.getNodeType()) {
			Element element = (Element) node;
			String tagName = element.getTagName().toLowerCase().intern();
			if ("pre" == tagName) {
				preformatted = true;
			}
			String text = "";
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				text += getText(element.getChildNodes().item(i), preformatted);
			}
			if (tagName == "p" || tagName == "br" || tagName == "hr" || tagName == "div") {
				text += "\n";
			}
			return text;
		}
		return "";
	}

	private String normalizeNewlines(String text) {
		return text.replace("/\r\n|\r/g", "\n");
	}

	private String normalizeSpaces(String text) {
		char nonBreakingSpace = (char) 160;
		return text.replaceAll("\\s+", " ").replace(nonBreakingSpace, ' ');
	}

	public boolean isVisible(Element element) {
		inject("twist-is-visible.js");
		return Boolean.parseBoolean(evaluate("Twist.isVisible(" + domExpression(element) + ")"));
	}

	public void setWindowExpression(String domExpression) {
		log.debug("Changing target window to: '{}'", domExpression);
		emptyDocumentCache();
		inject("twist-normalize-frame.js");
		windowExpression = evaluate("Twist.normalizeFrame(" + domExpression + ")");
		log.debug("Target window normalized as: '{}'", windowExpression);
	}

	private void emptyDocumentCache() {
		document = null;
	}

	public String getWindowExpression() {
		return windowExpression;
	}

	public String getDocumentExpression() {
		return windowExpression + ".document";
	}

	public Browser getBrowser() {
		return browser;
	}
}
