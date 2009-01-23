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
package com.thoughtworks.twist.driver.web.user;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingUser {

    public static User wrap(final User user) {
        return (User) Proxy.newProxyInstance(LoggingUser.class.getClassLoader(), new Class[] {User.class}, new InvocationHandler() {
            Log log = LogFactory.getLog(user.getClass());
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.debug(method.getName() + ": " + Arrays.toString(args));
                return method.invoke(user, args);
            }
        });
    }
}
