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
package com.thoughtworks.twist.osgi;

import java.io.File;

import com.thoughtworks.selenium.Selenium;


public class Main {
	public static void main(String[] args) throws Exception {
		try {
			EmbeddedEquinoxLauncher launcher;

			if (isMac()) {
				verifyOnFirstThread();
				launcher = new EmbeddedEquinoxLauncher(getAndVerifyCocoaClasspath().toURL());
			} else {
				launcher = new EmbeddedEquinoxLauncher();
			}

			launcher.startup();
			
			Selenium web = launcher.create(Selenium.class);
			web.start();
			web.open("http://www.google.com");

			launcher.shutdown();
		} catch (Exception e) {
			throw e;
		}
	}

	private static File getAndVerifyCocoaClasspath() {
		File cocoaClasspath = new File("/System/Library/Java");
		if (!new File(cocoaClasspath, "/com/apple").isDirectory()) {
			throw new IllegalStateException("You need to have the cocoa Java bindings installed at /System/Library/Java (comes with XCode).");
		}
		return cocoaClasspath;
	}

	private static void verifyOnFirstThread() {
		if (!isRunningOnFirstThread()) {
			throw new IllegalArgumentException(
					"You need to start the JVM with -XstartOnFirstThread on Mac.");
		}
	}

	private static boolean isRunningOnFirstThread() {
		return "Thread-0".equals(Thread.currentThread().getName());
	}

	private static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}
}
