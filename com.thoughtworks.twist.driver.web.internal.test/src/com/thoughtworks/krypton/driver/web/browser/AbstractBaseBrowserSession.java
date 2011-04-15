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
package com.thoughtworks.krypton.driver.web.browser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.thoughtworks.krypton.driver.web.browser.BrowserFamily;
import com.thoughtworks.krypton.driver.web.browser.BrowserSession;
import com.thoughtworks.krypton.driver.web.browser.BrowserSessionFactory;
import com.thoughtworks.krypton.driver.web.browser.wait.LocationChangedWaitStrategy;

import static org.junit.Assert.*;

public abstract class AbstractBaseBrowserSession {
    protected BrowserSession session = new BrowserSessionFactory().create();

    @After
    public void closeBrowser() {
        session.closeBrowser();
    }

    protected void render(String html) throws InterruptedException {
        session.openBrowser();
        LocationChangedWaitStrategy listener = addLocationChangedListener();
        session.getBrowser().setText(html);
        waitForLocationToChange(listener);
    }

    protected void load(String url) {
        session.openBrowser();
        LocationChangedWaitStrategy listener = addLocationChangedListener();
//		session.execute(session.getDocumentExpression() + ".location = '" + url + "'");
        session.getBrowser().setUrl(url);
        waitForLocationToChange(listener);
    }

    private LocationChangedWaitStrategy addLocationChangedListener() {
        LocationChangedWaitStrategy listener = new LocationChangedWaitStrategy();
        session.getBrowser().addLocationListener(listener);
        return listener;
    }

    private void waitForLocationToChange(LocationChangedWaitStrategy listener) {
    	Display display = Display.getDefault();
    	display.sleep();
        session.pumpEvents();
        while (listener.isBusy()) {
        	session.pumpEvents();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
        session.pumpEvents();
        session.getBrowser().removeLocationListener(listener);
        session.waitForIdle();
    }

    private String xmlAsString(Document document) throws IOException {
        return xmlAsString(document.getDocumentElement());
    }

    protected String xmlAsString(Element element) throws IOException {
        StringWriter xmlAsString = new StringWriter();
        OutputFormat format = new OutputFormat();
        new XMLSerializer(xmlAsString, format).serialize(element);
        return xmlAsString.toString();
    }

    protected void assertXmlEquals(Document expected, Document actual) throws IOException {
        assertEquals(xmlAsString(expected), removeNewLineFromClosingBodyTag(xmlAsString(actual)));
    }

    private String removeNewLineFromClosingBodyTag(String html) throws IOException {
        return html.replace("\n</body>", "</body>");
    }

    protected void assertXmlEquals(Element expected, Element actual) throws IOException {
        assertEquals(xmlAsString(expected), xmlAsString(actual));
    }

    protected Document parse(String html) throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(html.getBytes()));
        wrapContentsOfTagsWithCData(document, "script");
        wrapContentsOfTagsWithCData(document, "style");
        wrapContentsOfTagsWithCData(document, "noscript");
        return document;
    }

    private void wrapContentsOfTagsWithCData(Document document, String tagName) {
        NodeList tags = document.getElementsByTagName(tagName);
        for (int i = 0; i < tags.getLength(); i++) {
            Node item = tags.item(i);
            CDATASection section = document.createCDATASection(item.getTextContent());
            while (item.hasChildNodes()) {
                item.removeChild(item.getFirstChild());
            }
            item.appendChild(section);
        }
    }

    protected void inject(String script) {
        session.getBrowser().execute(readResource(script));
    }

    protected String readResource(String resource) {
        return session.readResource(resource, getClass());
    }
    
	protected void executeAndWaitForIdle(String script) {
		session.evaluate(script);
        session.waitForIdle();
	}

    protected String referenceErrorMessage(String reference) {
    	return BrowserFamily.fromSystemProperty().referenceError(reference);
    }
}
