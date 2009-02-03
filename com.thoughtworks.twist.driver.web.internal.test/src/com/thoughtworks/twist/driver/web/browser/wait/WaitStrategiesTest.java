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
package com.thoughtworks.twist.driver.web.browser.wait;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.thoughtworks.twist.driver.web.browser.AbstractBaseBrowserSessionWithWebServer;
import com.thoughtworks.twist.driver.web.browser.BrowserFamily;

import static org.junit.Assert.*;

import static com.thoughtworks.twist.driver.web.browser.BrowserFamily.*;

public class WaitStrategiesTest extends AbstractBaseBrowserSessionWithWebServer {

    private static final int REFRESHES = 3;

    @Test
    public void shouldVerifyWebServerIsResponding() throws Exception {
        String path = "/hello-world-servlet";
        handler.addServletWithMapping(HelloWorldServlet.class, path);

        HttpURLConnection connection = connectTo(path);
        try {
            assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
            assertTrue(reader(connection).readLine().contains("Hello World"));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Test
    public void shouldRespondToBrowser() throws Exception {
        String path = "/hello-world-servlet";
        handler.addServletWithMapping(HelloWorldServlet.class, path);

        load(localUrl(path));

        assertEquals("Hello World", session.dom().getDocumentElement().getTextContent());
    }

    @Test
    public void shouldWaitForPageToLoadUsingLocationChangedWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
        int timeout = BlockingHelloWorldServlet.BLOCKING_TIME * 2;
        session.addWaitStrategy(new LocationChangedWaitStrategy());

        session.openBrowser();

        loadAndRefreshPageThreeTimes(path, timeout);
    }

    // Safari lacks the onbeforeunload event.
    @Test
    public void shouldWaitForPageToLoadUsingOnBeforeUnloadWaitStrategy() throws Exception {
    	if (BrowserFamily.fromSystemProperty() == BrowserFamily.SAFARI) {
    		return;
    	}
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
        int timeout = BlockingHelloWorldServlet.BLOCKING_TIME * 2;

        OnBeforeUnloadWaitStrategy waitStrategy = new OnBeforeUnloadWaitStrategy();
        session.addWaitStrategy(waitStrategy);

        session.openBrowser();

        loadAndRefreshPageThreeTimes(path, timeout);
    }

    @Test
    public void shouldWaitForPageToLoadUsingDocumentReadyWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(SlowDocumentHelloWorldServlet.class, path);
        int timeout = SlowDocumentHelloWorldServlet.BLOCKING_TIME * 3;

        final boolean[] wasBusy = new boolean[1];
        DocumentReadyWaitStrategy waitStrategy = new DocumentReadyWaitStrategy() {
            public boolean isBusy() {
                boolean busy = super.isBusy();
                if (busy) {
                    wasBusy[0] = true;
                }
                return busy;
            }
        };
        
        if (IE != session.getBrowserFamily()) { 
            session.addWaitStrategy(new LocationChangedWaitStrategy());
        }
        session.addWaitStrategy(waitStrategy);

        session.openBrowser();

        int i;
        for (i = 0; i < REFRESHES; i++) {
            wasBusy[0] = false;
            assertReloadHelloWorld(path, timeout);
            assertTrue(wasBusy[0]);
        }
        assertEquals(REFRESHES, i);
    }

    @Test
    public void shouldNotWaitForPageToLoadUsingDocumentReadyWaitStrategyForFastDOM() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
        int timeout = BlockingHelloWorldServlet.BLOCKING_TIME * 3;

        final boolean[] wasBusy = new boolean[1];
        DocumentReadyWaitStrategy waitStrategy = new DocumentReadyWaitStrategy() {
            public boolean isBusy() {
                boolean busy = super.isBusy();
                if (busy) {
                    wasBusy[0] = true;
                }
                return busy;
            }
        };
        
        session.addWaitStrategy(new LocationChangedWaitStrategy());
        session.addWaitStrategy(waitStrategy);

        session.openBrowser();

        int i;
        for (i = 0; i < REFRESHES; i++) {
            wasBusy[0] = false;
            assertReloadHelloWorld(path, timeout);
            assertFalse(wasBusy[0]);
        }
        assertEquals(REFRESHES, i);
    }
    private void loadAndRefreshPageThreeTimes(String path, int timeout) throws Exception {
        int i;
        for (i = 0; i < REFRESHES; i++) {
            assertReloadHelloWorld(path, timeout);
        }
        assertEquals(REFRESHES, i);
    }

    private void assertReloadHelloWorld(String path, int timeout) throws MalformedURLException {
        session.getBrowser().setUrl(localUrl(path));
        timedWaitForIdle();
        assertTrue(idleTime < timeout);
        assertTrue(idleTime >= BlockingHelloWorldServlet.BLOCKING_TIME);
        assertEquals("Hello World", session.dom().getElementById("helloworld").getTextContent());
    }

    // This just doesn't work

    // @Test
    // public void shouldWaitForPageToLoadUsingOnUnloadWaitStrategy() throws
    // Exception {
    // String path = "/blocking-servlet";
    // handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
    // int timeout = BlockingHelloWorldServlet.BLOCKING_TIME * 2;
    //
    // render("<html/>");
    // OnUnloadWaitStrategy waitStrategy = new OnUnloadWaitStrategy(timeout);
    // assertFalse(waitStrategy.changed);
    // assertFalse(waitStrategy.isUnloading);
    //
    // session.addWaitStrategy(waitStrategy);
    //
    // session.openBrowser();
    //
    // loadAndRefreshPageThreeTimes(path, timeout);
    // }

    @Test
    public void shouldNotWaitForPageToLoadWithoutLocationChangedOrOnUnloadWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);

        session.openBrowser();
        session.getBrowser().setUrl(localUrl(path));

        timedWaitForIdle();

        assertTrue(idleTime < BlockingHelloWorldServlet.BLOCKING_TIME);
    }

    @Test
    public void shouldWaitForAjaxRequestUsingAjaxWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
        int timeout = BlockingHelloWorldServlet.BLOCKING_TIME * 2;
        session.addWaitStrategy(new AjaxWaitStrategy());

        session.openBrowser();
        load(localUrl(path));

        doLocalAjaxRequest(path);
        assertEquals(1, Integer.parseInt(session.execute("Twist.numberOfActiveAjaxRequests")));
        timedWaitForIdle();

        assertTrue(idleTime < timeout);
        assertTrue(idleTime >= BlockingHelloWorldServlet.BLOCKING_TIME);
        assertEquals(0, Integer.parseInt(session.execute("Twist.numberOfActiveAjaxRequests")));
    }

    @Test
    public void shouldNotWaitForExcludedAjaxRequestUsingAjaxWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
        AjaxWaitStrategy strategy = new AjaxWaitStrategy();
        strategy.addURLExclusionPattern(".*/blocking-.*");
        session.addWaitStrategy(strategy);

        session.openBrowser();
        load(localUrl(path));

        doLocalAjaxRequest(path);
        assertEquals(0, Integer.parseInt(session.execute("Twist.numberOfActiveAjaxRequests")));
        timedWaitForIdle();

        assertTrue(idleTime < BlockingHelloWorldServlet.BLOCKING_TIME);
        assertEquals(0, Integer.parseInt(session.execute("Twist.numberOfActiveAjaxRequests")));
    }

    @Test
    public void shouldNotWaitForAjaxRequestWithoutAjaxWaitStrategy() throws Exception {
        String path = "/blocking-servlet";
        handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);

        session.openBrowser();
        load(localUrl(path));

        doLocalAjaxRequest(path);
        timedWaitForIdle();

        assertTrue(idleTime < BlockingHelloWorldServlet.BLOCKING_TIME);
    }

    @SuppressWarnings("serial")
    public static class SlowDocumentHelloWorldServlet extends HttpServlet {
        private static final int CHUNK_SIZE = 1024;
        private static final int BLOCKING_TIME = 250;
        private static final StringBuffer CHUNK_OF_DATA;
        static {
            CHUNK_OF_DATA = new StringBuffer();
            for (int i = 0; i < CHUNK_SIZE; i++) {
                CHUNK_OF_DATA.append("x");
            }
        }

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                response.setHeader("Cache-Control", "no-cache");
                OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
                writer.write("<html><head/><body>");
                writer.write("<div>" + CHUNK_OF_DATA.toString() + "</div>");
                writer.flush();
                Thread.sleep(BLOCKING_TIME);
                writer.write("<div id=\"helloworld\">Hello World</div></body></html>");
                writer.close();
            } catch (InterruptedException e) {
            }
        }
    }

    @SuppressWarnings("serial")
    public static class BlockingHelloWorldServlet extends HelloWorldServlet {
        private static final int BLOCKING_TIME = 250;

        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                response.setHeader("Cache-Control", "no-cache");
                Thread.sleep(BLOCKING_TIME);
                super.doGet(request, response);
            } catch (InterruptedException e) {
            }
        }
    }
}
