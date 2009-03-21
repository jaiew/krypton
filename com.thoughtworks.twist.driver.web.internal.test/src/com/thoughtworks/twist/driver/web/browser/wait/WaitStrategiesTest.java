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

import static com.thoughtworks.twist.driver.web.browser.BrowserFamily.IE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.twist.driver.web.browser.AbstractBaseBrowserSessionWithWebServer;
import com.thoughtworks.twist.driver.web.browser.BrowserFamily;

public class WaitStrategiesTest extends AbstractBaseBrowserSessionWithWebServer {
	private static final int BLOCKING_TIME = 500;
	private static final int SET_TIMEOUT_PRECISION = 50;
	private static final int REFRESHES = 3;

	@Before
	public void setTimeoutToOneSecond() {
		SlowLoadingImageServlet.timesCalled = 0;
		session.setEventLoopTimeout(BLOCKING_TIME * 4);
	}

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

		assertEquals("Hello World", session.dom().getElementsByTagName("body").item(0).getTextContent());
	}

	@Test
	public void shouldWaitForPageToLoadUsingLocationChangedWaitStrategy() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2;
		session.addWaitStrategy(new LocationChangedWaitStrategy());

		session.openBrowser();

		loadAndRefreshPageThreeTimes(path, timeout);
	}

	// Safari lacks the onbeforeunload event.
	@Test
	public void shouldWaitForPageToLoadUsingOnBeforeUnloadWaitStrategy() throws Exception {
		if (BrowserFamily.SAFARI == BrowserFamily.fromSystemProperty()) {
			return;
		}
		String path = "/blocking-servlet";
		handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2;

		OnBeforeUnloadWaitStrategy waitStrategy = new OnBeforeUnloadWaitStrategy();
		session.addWaitStrategy(waitStrategy);

		session.openBrowser();

		loadAndRefreshPageThreeTimes(path, timeout);
	}

	@Test
	public void shouldWaitForPageToLoadUsingDocumentReadyWaitStrategy() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(SlowDocumentHelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2; // 3

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

//		session.addWaitStrategy(new LocationChangedWaitStrategy());
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
	public void shouldWaitForPageButNotLargeImageToLoadUsingDocumentReadyWaitStrategy() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(SlowDocumentHelloWorldServletWithImage.class, path);
		handler.addServletWithMapping(SlowLoadingImageServlet.class, "/image-servlet/*");
		int timeout = BLOCKING_TIME * 2; // 3

		final boolean[] wasBusy = new boolean[1];
		final int[] timesBusy = new int[1];
		DocumentReadyWaitStrategy waitStrategy = new DocumentReadyWaitStrategy() {
			public boolean isBusy() {
				boolean busy = super.isBusy();
				if (busy) {
					if (!wasBusy[0]) {
						timesBusy[0]++;
					}
					wasBusy[0] = true;
				}
				return busy;
			}
		};

//		session.addWaitStrategy(new LocationChangedWaitStrategy());
		session.addWaitStrategy(waitStrategy);

		session.openBrowser();

		int i;
		for (i = 0; i < REFRESHES; i++) {
			wasBusy[0] = false;
			assertReloadHelloWorld(path, timeout);

			int servedBytes = SlowLoadingImageServlet.servedBytes;
			assertTrue(servedBytes >= 0);
			assertTrue("only half of image should have been served at most, full size: " + SlowLoadingImageServlet.IMAGE_BYTES.length
					+ " served: " + SlowLoadingImageServlet.servedBytes, servedBytes < (SlowLoadingImageServlet.IMAGE_BYTES.length / 2));
		}
		assertEquals(REFRESHES, i);
		assertEquals(REFRESHES, timesBusy[0]);

		// Safari will request the image more than once per refresh for some
		// reason
		assertTrue(SlowLoadingImageServlet.timesCalled >= REFRESHES);
	}

	@Test
	public void shouldNotWaitForPageToLoadUsingDocumentReadyWaitStrategyForFastDOM() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2; // 3

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

//		session.addWaitStrategy(new LocationChangedWaitStrategy());
		session.addWaitStrategy(waitStrategy);

		session.openBrowser();

		int i;
		for (i = 0; i < REFRESHES; i++) {
			wasBusy[0] = false;
			assertReloadHelloWorld(path, timeout);

			// The IE DocumentWaitStrategy will (probably) always be busy
//			if (IE != session.getBrowserFamily()) {
//				assertFalse(wasBusy[0]);
//			}
		}
		assertEquals(REFRESHES, i);
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

		assertTrue("idle < blocking: " + idleTime + " < " + BLOCKING_TIME, idleTime < BLOCKING_TIME);
	}

	@Test
	public void shouldWaitForAjaxRequestUsingAjaxWaitStrategy() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2;

		final String[] doneOrder = new String[] { "" };
		new BrowserFunction(session.getBrowser(), "ajaxDoneAt") {
			public Object function(Object[] arguments) {
				doneOrder[0] += "ajax, ";
				return super.function(arguments);
			}

		};
		AjaxWaitStrategy strategy = new AjaxWaitStrategy() {
			public void decreaseNumberOfActiveAjaxRequests() {
				doneOrder[0] += "strategy";
				super.decreaseNumberOfActiveAjaxRequests();
			}
		};
		session.addWaitStrategy(strategy);

		session.openBrowser();
		load(localUrl(path));

		doLocalAjaxRequest(path);
		assertEquals(1, strategy.getNumberOfActiveAjaxRequests());
		timedWaitForIdle();

		assertEquals("onreadystate call back was called after strategy became idle.", "ajax, strategy", doneOrder[0]);

		assertTrue("idle < timeout: " + idleTime + " < " + timeout, idleTime < timeout);
		assertTrue("idle >= blocking: " + idleTime + " >= " + BLOCKING_TIME, idleTime >= BLOCKING_TIME);
		assertEquals(0, strategy.getNumberOfActiveAjaxRequests());
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
		assertEquals(0, strategy.getNumberOfActiveAjaxRequests());
		timedWaitForIdle();

		assertTrue("idle < blocking: " + idleTime + " < " + BLOCKING_TIME, idleTime < BLOCKING_TIME);
		assertEquals(0, strategy.getNumberOfActiveAjaxRequests());
	}

	@Test
	public void shouldNotWaitForAjaxRequestWithoutAjaxWaitStrategy() throws Exception {
		String path = "/blocking-servlet";
		handler.addServletWithMapping(BlockingHelloWorldServlet.class, path);

		session.openBrowser();
		load(localUrl(path));

		doLocalAjaxRequest(path);
		timedWaitForIdle();

		assertTrue("idle < blocking: " + idleTime + " < " + BLOCKING_TIME, idleTime < BLOCKING_TIME);
	}

	@Test
	public void shouldNotWaitForSetTimeouWithoutSetTimeoutWaitStrategy() throws Exception {
		String path = "/hello-world-servlet";
		handler.addServletWithMapping(HelloWorldServlet.class, path);

		session.openBrowser();
		load(localUrl(path));

		doSetTimeoutCall("''", BLOCKING_TIME);
		timedWaitForIdle();

		int slowTimeoutID = Integer.parseInt(session.evaluate("slowTimeoutID"));
		assertTrue("slowTimeoutID > 0: " + slowTimeoutID + " > 0", slowTimeoutID > 0);
		assertTrue("idle < blocking: " + idleTime + " < " + BLOCKING_TIME, idleTime < BLOCKING_TIME);
	}

	@Test
	public void shouldWaitForSetTimeoutUsingSetTimeoutWaitStrategy() throws Exception {
		String path = "/hello-world-servlet";
		handler.addServletWithMapping(HelloWorldServlet.class, path);
		int timeout = BLOCKING_TIME * 2;
		SetTimeoutWaitStrategy waitStrategy = new SetTimeoutWaitStrategy();
		session.addWaitStrategy(waitStrategy);

		session.openBrowser();
		load(localUrl(path));

		doSetTimeoutCall("'Hello'", BLOCKING_TIME);
		assertEquals(1, waitStrategy.getNumberOfActiveSetTimeouts());
		int slowTimeoutID = Integer.parseInt(session.evaluate("slowTimeoutID"));
		assertTrue("slowTimeoutID > 0: " + slowTimeoutID + " > 0", slowTimeoutID > 0);

		timedWaitForIdle();

		assertTrue("idle < timeout: " + idleTime + " < " + timeout, idleTime < timeout);
		assertTrue("idle == blocking (precision " + SET_TIMEOUT_PRECISION + "): " + idleTime + " == " + BLOCKING_TIME, Math.abs(idleTime
				- BLOCKING_TIME) < SET_TIMEOUT_PRECISION);

		assertEquals(getExpectedMessageAsIEDoesntSupportParameters(), session.evaluate("Twist.slowSetTimeoutCalledWith"));
		assertEquals(0, waitStrategy.getNumberOfActiveSetTimeouts());
	}

	private String getExpectedMessageAsIEDoesntSupportParameters() {
		return BrowserFamily.IE == BrowserFamily.fromSystemProperty() ? "Hello IE" : "Hello";
	}

	@Test
	public void shouldWaitForSetTimeoutAndStopWhenClearingTimeoutUsingSetTimeoutWaitStrategy() throws Exception {
		String path = "/hello-world-servlet";
		handler.addServletWithMapping(HelloWorldServlet.class, path);
		final int timeout = BLOCKING_TIME * 2;
		SetTimeoutWaitStrategy waitStrategy = new SetTimeoutWaitStrategy();
		session.addWaitStrategy(waitStrategy);

		session.openBrowser();
		load(localUrl(path));

		doSetTimeoutCall("'Hello'", timeout);
		assertEquals(1, waitStrategy.getNumberOfActiveSetTimeouts());

		int timeoutID = Integer.parseInt(session.evaluate("slowTimeoutID"));
		int slowTimeoutID = Integer.parseInt(session.evaluate("slowTimeoutID"));
		assertTrue("slowTimeoutID > 0: " + slowTimeoutID + " > 0", slowTimeoutID > 0);

		clearTimeoutInSeparateThread(timeoutID, BLOCKING_TIME);
		timedWaitForIdle();

		assertTrue("idle < timeout: " + idleTime + " < " + timeout, idleTime < timeout);
		assertTrue("idle >= blocking: " + idleTime + " >= " + BLOCKING_TIME, idleTime >= BLOCKING_TIME);

		assertEquals("undefined", session.evaluate("Twist.slowSetTimeoutCalledWith"));
		assertEquals(0, waitStrategy.getNumberOfActiveSetTimeouts());
	}

	private void clearTimeoutInSeparateThread(final int timeoutID, final int delay) {
		new Thread() {
			public void run() {
				session.getBrowser().getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							Thread.sleep(delay);
							session.execute("window.clearTimeout(" + timeoutID + ")");
						} catch (InterruptedException e) {
						}
					}
				});
			}
		}.start();
	}

	private void doSetTimeoutCall(String parameter, int delay) {
		session.execute("slowTimeoutID = window.setTimeout(slowSetTimeout, " + delay + ", " + parameter
				+ "); function slowSetTimeout(message) { Twist.slowSetTimeoutCalledWith = message ? message : 'Hello IE'; }");
		session.pumpEvents();
	}

	private void loadAndRefreshPageThreeTimes(String path, int timeout) throws Exception {
		int i;
		for (i = 0; i < REFRESHES; i++) {
			assertReloadHelloWorld(path, timeout);
		}
		assertEquals(REFRESHES, i);
	}

	private void assertReloadHelloWorld(String path, int timeout) throws MalformedURLException {
		session.getBrowser().execute("window.location = '" + localUrl(path) + "'");
		timedWaitForIdle();
		assertTrue("idle < timeout: " + idleTime + " < " + timeout, idleTime < timeout);
		assertTrue("idle >= blocking: " + idleTime + " >= " + BLOCKING_TIME, idleTime >= BLOCKING_TIME);
		assertEquals("Hello World", session.dom().getElementById("helloworld").getTextContent());
	}

	@SuppressWarnings("serial")
	public static class SlowDocumentHelloWorldServlet extends HttpServlet {
		private static final int CHUNK_SIZE = 1024;
		static final StringBuffer CHUNK_OF_DATA;
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
	public static class SlowDocumentHelloWorldServletWithImage extends SlowDocumentHelloWorldServlet {
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			try {
				response.setHeader("Cache-Control", "no-cache");
				OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
				writer.write("<html><head></head><body>");
				writer.write("<div>" + CHUNK_OF_DATA.toString() + "</div><img src=\"/image-servlet/" + System.currentTimeMillis() + "\"/>");
				writer.flush();
				Thread.sleep(BLOCKING_TIME);
				writer.write("<div id=\"helloworld\">Hello World</div></body></html>");
				writer.close();
			} catch (InterruptedException e) {
			}
		}
	}

	@SuppressWarnings("serial")
	public static class SlowLoadingImageServlet extends HttpServlet {
		private static final String IMAGE_FORMAT = "jpg";
		private static final int CHUNKS = 10;

		private static int servedBytes;
		private static int timesCalled;

		private static final byte[] IMAGE_BYTES;
		static {
			try {
				BufferedImage largeImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
				Graphics g = largeImage.createGraphics();
				g.drawLine(0, 0, largeImage.getWidth() - 1, largeImage.getHeight() - 1);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(largeImage, IMAGE_FORMAT, out);
				IMAGE_BYTES = out.toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// This method is only synchronized because of Safari is making multiple
		// requests to the image and we use a static to track the number of
		// served bytes.
		public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			try {
				timesCalled++;
				response.setHeader("Cache-Control", "no-cache");
				response.setContentType("image/" + IMAGE_FORMAT);
				OutputStream os = response.getOutputStream();
				int chunkSize = IMAGE_BYTES.length / CHUNKS;
				servedBytes = 0;
				for (int i = 0; i < CHUNKS; i++) {
					os.write(IMAGE_BYTES, CHUNKS * i, chunkSize);
					os.flush();
					Thread.sleep(BLOCKING_TIME * 2 / CHUNKS);
					servedBytes += chunkSize;
				}
				os.close();
			} catch (InterruptedException e) {
			}
		}
	}

	@SuppressWarnings("serial")
	public static class BlockingHelloWorldServlet extends HelloWorldServlet {
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
