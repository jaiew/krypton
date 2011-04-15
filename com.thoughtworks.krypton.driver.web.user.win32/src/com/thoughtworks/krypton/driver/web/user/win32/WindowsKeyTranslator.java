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
package com.thoughtworks.krypton.driver.web.user.win32;

import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Display;

import com.thoughtworks.krypton.driver.web.user.KeyTranslator;

public class WindowsKeyTranslator implements KeyTranslator {
    private static Method untranslateKey;
    static {
        try {
            untranslateKey = Display.class.getDeclaredMethod("untranslateKey", int.class);
            untranslateKey.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TransaltedKey translate(int virtualKey) {
        return new TransaltedKey(0, keyCode(virtualKey));
    }

    public boolean shouldTranslateKey(int virtualKey) {
        return keyCode(virtualKey) != 0;
    }

    private int keyCode(int virtualKey) {
        try {
            return (Integer) untranslateKey.invoke(null, virtualKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
