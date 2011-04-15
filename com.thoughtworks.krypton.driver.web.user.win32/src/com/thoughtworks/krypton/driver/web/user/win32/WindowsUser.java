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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Shell;

import com.thoughtworks.krypton.driver.web.user.KeyTranslator;
import com.thoughtworks.krypton.driver.web.user.User;

public class WindowsUser implements User {
	private final Shell shell;
	int handle = -1;
	private int modifiers;
	private KeyTranslator translator = new WindowsKeyTranslator();
	private boolean mozilla = true;
	private boolean findBrowser = true;
	private Point cursorLocation;

	public WindowsUser(Shell shell) {
		this.shell = shell;
	}

	public WindowsUser(Shell window, boolean findBrowser) {
		this(window);
		this.findBrowser = findBrowser;
	}

	public void click(int x, int y) {
		Point pt = shell.toDisplay(x, y);
		moveCursor(pt);

		int lParam = (y << 16) | (x & 0xffff);

		OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, 0, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONUP, 0, lParam);
		
		restoreCursor();
	}

	public void doubleClick(int x, int y) {
		Point pt = shell.toDisplay(x, y);
		moveCursor(pt);

		int lParam = (y << 16) | (x & 0xffff);

		OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, 0, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONUP, 0, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONDBLCLK, OS.MK_LBUTTON, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONUP, 0, lParam);

		restoreCursor();
	}
	
	public void dragAndDrop(int startX, int startY, int endX, int endY) {
		Point pt = shell.toDisplay(startX, startY);
		moveCursor(pt);

		int lParam = (startY << 16) | (startX & 0xffff);

		OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, 0, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, lParam);

		restoreCursor();
		// dragEvents(startX, startY, endX, endY, 10);

		lParam = (endY << 16) | (endX & 0xffff);

		pt = shell.toDisplay(endX, endY);
		moveCursor(pt);
		OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, OS.MK_LBUTTON, lParam);
		OS.PostMessage(getHandle(), OS.WM_LBUTTONUP, 0, lParam);

		restoreCursor();
	}

	// private void dragEvents(int startX, int startY, int endX, int endY, int
	// speed) {
	// int x = startX;
	// int y = startY;
	//        
	// int xDelta = startX < endX ? speed : -speed;
	// int yDelta = startY < endY ? speed : -speed;
	//        
	// while (xDelta != 0 || yDelta != 0) {
	// if (Math.abs(y - endY) < speed) {
	// yDelta = 0;
	// }
	// if (Math.abs(x - endX) < speed) {
	// xDelta = 0;
	// }
	// x += xDelta;
	// y += yDelta;
	//
	// int lParam = (x << 16) | (y & 0xffff);
	// shell.getDisplay().setCursorLocation(x, y);
	// OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, OS.MK_LBUTTON, lParam);
	// }
	// }

	public boolean isShiftDown() {
		return (modifiers & OS.MK_SHIFT) > 0;
	}

	public void key(int keyCode) {
		if (translator.shouldTranslateKey(keyCode)) {
			keyEvent(0, translator.translate(keyCode).keyCode);
		} else {
			keyEvent(keyCode, 0);
		}
	}

	public void key(int keyCode, int modifiers) {
		throw new UnsupportedOperationException();
	}

	public void rightClick(int x, int y) {
		Point pt = shell.toDisplay(x, y);
		moveCursor(pt);

		int lParam = (y << 16) | (x & 0xffff);

		OS.PostMessage(getHandle(), OS.WM_MOUSEMOVE, 0, lParam);
		OS.PostMessage(getHandle(), OS.WM_RBUTTONDOWN, OS.MK_RBUTTON, lParam);
		OS.PostMessage(getHandle(), OS.WM_RBUTTONUP, 0, lParam);

		restoreCursor();
	}

	public void shiftDown() {
		modifiers |= OS.MK_SHIFT;
	}

	public void shiftUp() {
		modifiers ^= OS.MK_SHIFT;
	}

	public void type(String string) {
		for (int i = 0; i < string.length(); i++) {
			char charAt = string.charAt(i);
			keyEvent(isShiftDown() ? Character.toUpperCase(charAt) : charAt, 0);
		}
	}

	private void keyEvent(int c, int keyCode) {
		int sc = OS.MapVirtualKey(keyCode, 0);
		OS.PostMessage(getHandle(), OS.WM_KEYDOWN, keyCode, 1 | sc << 16);
		pumpEvents();
		if (c > 0) {
			OS.PostMessage(getHandle(), OS.WM_CHAR, c, sc << 16);
			pumpEvents();
		}
		OS.PostMessage(getHandle(), OS.WM_KEYUP, keyCode, 1 | 1 << 31 | 1 << 30
				| sc << 16);
		pumpEvents();
	}

	private void pumpEvents() {
		while (!shell.isDisposed() && shell.getDisplay().readAndDispatch())
			;
	}
	
	private void moveCursor(Point pt) {
//		if (mozilla) {		
			cursorLocation = shell.getDisplay().getCursorLocation();
			shell.getDisplay().setCursorLocation(pt.x, pt.y);
//		}
	}

	private void restoreCursor() {
//		if (mozilla) {		
			shell.getDisplay().setCursorLocation(cursorLocation);
//		}
	}

	private int getHandle() {
		if (!findBrowser) {
			return handle;
		}
		if (mozilla) {			
			this.handle = findControl(shell.handle, "MozillaWindowClass");
			if (handle == -1) {
				mozilla = false;
			}
		}
		if (!mozilla) {
			this.handle = findControl(shell.handle, "Internet Explorer_Server");
		}
		return handle;
	}

	private int findControl(int handle, String windowClass) {
		char[] className = new char[32];
		int length = OS.GetClassNameW(handle, className, className.length);
		String classNameString = new String(className, 0, length);

		int hwd = OS.GetWindow(handle, OS.GW_CHILD);
		if (classNameString.equals(windowClass) && hwd == 0) {
			return handle;
		}
		while (hwd != 0) {
			int found = findControl(hwd, windowClass);
			if (found > 0) {
				return found;
			}
			hwd = OS.GetWindow(hwd, OS.GW_HWNDNEXT);
		}
		return -1;
	}
}
