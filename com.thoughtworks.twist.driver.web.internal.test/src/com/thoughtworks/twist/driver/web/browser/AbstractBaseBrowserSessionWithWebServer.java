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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;

public abstract class AbstractBaseBrowserSessionWithWebServer extends AbstractBaseBrowserSession {
    protected static final int WEBSERVER_PORT = 9999;

    protected long idleTime;

    protected Server server;
    protected ServletHandler handler;

    @Before
    public void startWebServer() throws Exception {
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(WEBSERVER_PORT);
        server.addConnector(connector);

        handler = new ServletHandler();
        server.addHandler(handler);

        server.start();
    }

    @After
    public void stopWebServer() throws Exception {
        server.stop();
    }

    protected HttpURLConnection connectTo(String path) throws IOException, MalformedURLException {
        HttpURLConnection connection = (HttpURLConnection) new URL(localUrl(path)).openConnection();
        connection.connect();
        return connection;
    }

    protected String localUrl(String path) throws MalformedURLException {
        return new URL("http", "localhost", WEBSERVER_PORT, path).toExternalForm();
    }

    protected BufferedReader reader(HttpURLConnection connection) throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    protected void doLocalAjaxRequest(String path) throws MalformedURLException {
        session.execute("var http = " + createXMLHttpRequest() + "; http.open('GET', '" + localUrl(path) + "', true);  http.send('');");
        session.pumpEvents();
    }

    protected String createXMLHttpRequest() {
    	return BrowserFamily.fromSystemProperty().newXmlHttpRequestCode();
    }

    protected void timedWaitForIdle() {
        long now = System.currentTimeMillis();
        session.waitForIdle();
        idleTime = System.currentTimeMillis() - now;
    }

    @SuppressWarnings("serial")
    public static class HelloWorldServlet extends HttpServlet {
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write("<html><head/><body id=\"helloworld\">Hello World</body></html>");
            writer.close();
        }
    }
}
