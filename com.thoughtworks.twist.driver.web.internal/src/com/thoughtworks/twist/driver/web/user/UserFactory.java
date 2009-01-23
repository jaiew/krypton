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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class UserFactory {
    private static Map<String, String> USER_IMPLEMENTATIONS_BY_PLATFORM;

    static {
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("carbon", "com.thoughtworks.twist.driver.web.user.cocoa.CocoaUser");
        map.put("win32", "com.thoughtworks.twist.driver.web.user.win32.WindowsUser");
        map.put("gtk", "com.thoughtworks.twist.driver.web.user.gtk.GtkUser");
        
        USER_IMPLEMENTATIONS_BY_PLATFORM = Collections.unmodifiableMap(map);
    }
    
    public User createUser(Shell shell) {
        String className = getUserForPlatform();
        if (className == null) {
            throw new IllegalStateException("Unsupported Platform: " + SWT.getPlatform());
        }
        return createUser(shell, className);
    }

    public String getUserForPlatform() {
        return USER_IMPLEMENTATIONS_BY_PLATFORM.get(SWT.getPlatform());
    }

    private User createUser(Shell shell, String className) {
        try {
            Class<?> userClass = Class.forName(className);
            return (User) userClass.getConstructor(Shell.class).newInstance(shell);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
