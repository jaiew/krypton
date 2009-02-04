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
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Display;

public class Decorators {
	public static final class SWTThreadingDecorator<T> implements InvocationHandler {
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
				}
			}
			try {
				final Object[] result = new Object[1];
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						try {
							result[0] = method.invoke(anInstance, args);
						} catch (Exception e) {
							throw new RuntimeException(unwind(e));
						}
					}
				});
				return result[0];
			} catch (RuntimeException e) {
				throw unwind(e);
			}
		}

		private Throwable unwind(Throwable t) {
			while (t.getCause() != null) {
				t = t.getCause();
			}
			return t;
		}

		public T getInstance() {
			return anInstance;
		}
	}

	private static final class LoggingDecorator<T> implements InvocationHandler {
		private T anInstance;
		private Log log;

		private LoggingDecorator(T anInstance) {
			this.anInstance = anInstance;
			this.log = LogFactory.getLog(anInstance.getClass());
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				log.debug(method.getName() + ": " + Arrays.toString(args));
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
