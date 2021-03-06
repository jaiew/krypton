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
package com.thoughtworks.krypton.driver.web.user;

public interface User {
    void click(int x, int y);

    void rightClick(int x, int y);

    void doubleClick(int x, int y);

    void dragAndDrop(int startX, int startY, int endX, int endY);

    void type(String string);

    void key(int keyCode);

    void key(int keyCode, int modifiers);

    void shiftDown();

    void shiftUp();

    boolean isShiftDown();
}
