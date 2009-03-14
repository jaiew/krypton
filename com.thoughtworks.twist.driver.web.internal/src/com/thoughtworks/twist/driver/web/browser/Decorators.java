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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.widgets.Display;

public class Decorators {
	public static final class SWTThreadingDecorator<T> implements InvocationHandler {
		private static final List<String> STACK_TRACE_FILTER = Arrays.asList("java.lang.Thread", "java.util.concurrent.ThreadPoolExecutor",
								"com.thoughtworks.selenium.SeleneseTestCase", "$Proxy", "org.eclipse.swt.widgets", "org.eclipse.swt.SWT",
								"com.thoughtworks.twist.driver.web.browser.Decorators");
		private T anInstance;

		private SWTThreadingDecorator(T anInstance) {
			this.anInstance = anInstance;
		}

		public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
			if (Display.getCurrent() == Display.getDefault()) {
				try {
					return method.invoke(anInstance, args);
				} catch (InvocationTargetException e) {
					throw unwind(e);
				} finally {
					Display.getDefault().update();
				}
			}
			try {
				final Object[] result = new Object[1];
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						try {
							result[0] = method.invoke(anInstance, args);
						} catch (Exception e) {
							throw new RuntimeException(e);
						} finally {
							Display.getDefault().update();
						}
					}
				});
				return result[0];
			} catch (RuntimeException e) {
				 throw unwind(e);
			}
		}

		private Throwable unwind(Throwable t) {
			Throwable original = t;
			List<StackTraceElement> stack = new ArrayList<StackTraceElement>();
			do {
				if (!(t instanceof InvocationTargetException)) {
					stack.addAll(0, Arrays.asList(t.getStackTrace()));
				}
				original = t;
				t = t.getCause();
			} while (t != null);
			for (Iterator<StackTraceElement> iterator = stack.iterator(); iterator.hasNext();) {
				StackTraceElement element = (StackTraceElement) iterator.next();
				for (String prefix : STACK_TRACE_FILTER) {
					if (element.getClassName().startsWith(prefix)) {
						iterator.remove();
					}
				}
			}
			original.setStackTrace(stack.toArray(new StackTraceElement[0]));
			return original;
		}

		public T getInstance() {
			return anInstance;
		}
	}

	private static final class LoggingDecorator<T> implements InvocationHandler {
		private T anInstance;
		private Logger log;

		private LoggingDecorator(T anInstance) {
			this.anInstance = anInstance;
			this.log = LoggerFactory.getLogger(anInstance.getClass());
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				log.debug("{}: {}", method.getName(), args);
				return method.invoke(anInstance, args);
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T wrapWithLogging(Class<T> anInterface, T anInstance) {
		return (T) Proxy.newProxyInstance(Decorators.class.getClassLoader(), new Class[] { anInterface }, new LoggingDecorator<T>(
				anInstance));
	}

	@SuppressWarnings("unchecked")
	public static <T> T wrapWithSWTThreading(Class<T> anInterface, T anInstance) {
		return (T) Proxy.newProxyInstance(Decorators.class.getClassLoader(), new Class[] { anInterface }, new SWTThreadingDecorator<T>(
				anInstance));
	}
}
