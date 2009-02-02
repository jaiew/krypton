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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.twist.driver.web.browser.AbstractBaseBrowserSessionWithWebServer;

public abstract class AbstractBrowserOperationsTest extends AbstractBaseBrowserSessionWithWebServer {
    private TwistSelenium selenium = new TwistSelenium("http://localhost:" + WEBSERVER_PORT, session);
    private String path;

    @Before
    public void registerHelloWorldServlet() {
        path = "/hello-world-servlet";
        handler.addServletWithMapping(HelloWorldServlet.class, path);
    }

    @Test
    public void shouldOpenUrl() throws Exception {
        selenium.open(localUrl(path));
        assertEquals("Hello World", session.dom().getDocumentElement().getTextContent());
    }
    
    @Test
    public void shouldOpenPathUrlAtCurrentHost() throws Exception {
        selenium.open(path);
        assertEquals("Hello World", session.dom().getDocumentElement().getTextContent());        
    }

    @Test
    public void shouldReturnCurrentLocation() throws Exception {
        selenium.open(localUrl(path));
        assertEquals(localUrl(path), selenium.getLocation());
    }

    @Test
    public void shouldGoBack() throws Exception {
        String nextHelloWorldServletPath = "/next-hello-world-servlet";
        handler.addServletWithMapping(HelloWorldServlet.class, nextHelloWorldServletPath);

        selenium.open(localUrl(path));
        selenium.open(localUrl(nextHelloWorldServletPath));
        assertEquals(localUrl(nextHelloWorldServletPath), selenium.getLocation());

        selenium.goBack();
        assertEquals(localUrl(path), selenium.getLocation());
    }

    @Test
    public void shouldReturnCurrentTitle() throws Exception {
        String path = "/title-servlet";
        handler.addServletWithMapping(TitleServlet.class, path);
        selenium.open(localUrl(path));
        assertEquals(TitleServlet.TITLE, selenium.getTitle());
    }

    @Test
    public void shouldCloseBrowser() throws Exception {
        selenium.open(localUrl(path));
        selenium.close();
        assertFalse(session.browser.getShell().isVisible());
    }

    // Selenium.getHtmlSource is supposed to return the loaded version of the page - we have no way to do that right now.
    
//    @Test
//    public void souldReturnHtmlSource() throws Exception {
//        String path = "/title-servlet";
//        handler.addServletWithMapping(TitleServlet.class, path);
//        selenium.open(localUrl(path));
//        assertEquals(TitleServlet.SOURCE, selenium.getHtmlSource());
//    }

    @Test
    public void shouldReturnEvaluatedJavascript() throws Exception {
        selenium.open(localUrl(path));
        assertEquals("2", selenium.getEval("1 + 1"));
        assertEquals("2", selenium.getExpression("1 + 1"));
    }

    @Test
    public void shoudlRefereshPage() throws Exception {
        String path = "/counting-title-servlet";
        handler.addServletWithMapping(CountingTitleServlet.class, path);
        selenium.open(localUrl(path));
        assertEquals("1", selenium.getTitle());
        selenium.refresh();
        assertEquals("2", selenium.getTitle());
    }

    @Test
    public void shouldEndSessionOnStop() throws Exception {
        selenium.open(localUrl(path));
        selenium.stop();
        assertTrue(session.browser.isDisposed());
    }

    @Test
    public void shouldFocusWindow() throws Exception {
        Shell otherWindow = null;
        try {
            otherWindow = new Shell();
            selenium.open(localUrl(path));
            otherWindow.open();
            otherWindow.forceFocus();
            assertTrue(otherWindow.isFocusControl());
            selenium.windowFocus();
            assertTrue(session.browser.isFocusControl());
        } finally {
            if (otherWindow != null) {
                otherWindow.dispose();
            }
        }
    }

    @Test
    public void shouldMaximizeWindow() throws Exception {
        selenium.open(localUrl(path));
        selenium.windowMaximize();
        assertTrue(session.browser.getShell().getMaximized());
    }
    
    @Test
    public void shouldStubOutAlerts() throws Exception {
        String message = "Hello World";
        selenium.open(localUrl(path));
        selenium.getEval("function() { alert('" + message + "'); return null}()");
        assertTrue(selenium.isAlertPresent());
        assertEquals(message, selenium.getAlert());
        assertFalse(message, selenium.isAlertPresent());
    }

    @SuppressWarnings("serial")
    public static class TitleServlet extends HttpServlet {
        private static final String TITLE = "Hello World";
        private static final String SOURCE = "<html><head><title>" + TITLE + "</title></head><body/></html>";

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write(SOURCE);
            writer.close();
        }
    }

    @SuppressWarnings("serial")
    public static class CountingTitleServlet extends HttpServlet {
        private static int count;

        public void init() {
            count = 1;
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write("<html><head><title>" + count++ + "</title></head></html>");
            writer.close();
        }
    }
}
