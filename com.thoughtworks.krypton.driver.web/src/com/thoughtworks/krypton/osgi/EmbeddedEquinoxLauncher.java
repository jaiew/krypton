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
package com.thoughtworks.krypton.osgi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class EmbeddedEquinoxLauncher {
	private Class<?> eclipseStarterClass;
	private Class<?> bundleContextClass;
	private Class<?> serviceReferenceClass;
	private Object bundleContext;
	private HashMap<String, String> configuration;

	public EmbeddedEquinoxLauncher(URL... optionalClasspath) throws Exception {
		URL eclipseUrl = getClass().getClassLoader().getResource("eclipse");
		if ("jar".equals(eclipseUrl.getProtocol())) {
			eclipseUrl = extractEclipseFromJar(eclipseUrl);
		}

		List<URL> classpath = new ArrayList<URL>();
		classpath.addAll(Arrays.asList(optionalClasspath));
		classpath.add(findFrameworkJar(eclipseUrl));

		ClassLoader frameworkLoader = new URLClassLoader(classpath.toArray(new URL[0]), getClass().getClassLoader());

		eclipseStarterClass = frameworkLoader.loadClass("org.eclipse.core.runtime.adaptor.EclipseStarter");
		bundleContextClass = frameworkLoader.loadClass("org.osgi.framework.BundleContext");
		serviceReferenceClass = frameworkLoader.loadClass("org.osgi.framework.ServiceReference");

		configuration = configureInstallArea(eclipseUrl);
	}

	public void addProperty(String key, String value) {
		configuration.put(key, value);
	}

	public void startup(String... args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		eclipseStarterClass.getMethod("setInitialProperties", Map.class).invoke(null, configuration);
		bundleContext = eclipseStarterClass.getMethod("startup", String[].class, Runnable.class).invoke(null, args, null);
	}

	public void shutdown() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		eclipseStarterClass.getMethod("shutdown").invoke(null);
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> service) throws Exception {
		Object serviceReference = bundleContextClass.getMethod("getServiceReference", String.class)
				.invoke(bundleContext, service.getName());
		System.out.println("Resolving ServiceReference " + serviceReference);
		Object serviceImplementation = bundleContextClass.getMethod("getService", serviceReferenceClass).invoke(bundleContext,
				serviceReference);
		System.out.println("Returning " + serviceImplementation);
		System.out.println();

		return (T) serviceImplementation;
	}

	private HashMap<String, String> configureInstallArea(URL eclipseUrl) {
		HashMap<String, String> configuration = new HashMap<String, String>();
		configuration.put("osgi.install.area", eclipseUrl.toExternalForm());
		return configuration;
	}

	private URL findFrameworkJar(URL eclipseUrl) throws URISyntaxException, MalformedURLException {
		URL osgiUrl = null;
		for (File file : new File(new File(eclipseUrl.toURI()), "plugins").listFiles()) {
			if (file.getName().startsWith("org.eclipse.osgi_")) {
				osgiUrl = file.toURL();
				break;
			}
		}
		return osgiUrl;
	}

	private URL extractEclipseFromJar(URL eclipseUrl) throws IOException, UnsupportedEncodingException, FileNotFoundException,
			MalformedURLException {
		String[] jarAndPath = eclipseUrl.toExternalForm().split("!/");

		System.out.println("Jar containing plugins directory: " + jarAndPath[0]);
		System.out.println("Jar plugins directory path: " + jarAndPath[1]);

		File jarFile = new File(URLDecoder.decode(jarAndPath[0].substring("jar:file:".length()), "UTF-8"));

		File driverRoot = new File(System.getProperty("java.io.tmpdir"), jarFile.getName());
		File eclipseDir = new File(driverRoot, "eclipse");
		if (driverRoot.isDirectory()) {
			System.out.println("Already extracted to " + driverRoot);
			return eclipseDir.toURL();
		}

		driverRoot.mkdirs();

		long currentTimeMillis = System.currentTimeMillis();
		extractJar(jarFile, driverRoot);
		System.out.println("Extraction time: " + (System.currentTimeMillis() - currentTimeMillis));

		return eclipseDir.toURL();
	}

	private void extractJar(File jarFile, File driverRoot) throws IOException, FileNotFoundException {
		JarFile jar = new JarFile(jarFile);
		for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
			JarEntry nextElement = entries.nextElement();
			if (nextElement.getName().startsWith("eclipse/")) {
				File file = new File(driverRoot, nextElement.getName());
				if (nextElement.isDirectory()) {
					file.mkdirs();
				} else {
					extractAndClose(jar.getInputStream(nextElement), new FileOutputStream(file), nextElement.getSize());
				}
			}
		}
	}

	private void extractAndClose(InputStream in, FileOutputStream out, long size) throws IOException, FileNotFoundException {
		FileChannel outChannel = null;
		ReadableByteChannel inChannel = null;
		try {
			inChannel = Channels.newChannel(in);
			outChannel = out.getChannel();
			outChannel.transferFrom(inChannel, 0, size);
		} finally {
			if (outChannel != null) {
				outChannel.close();
			}
			if (inChannel != null) {
				inChannel.close();
			}
		}
	}
}
