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
package com.thoughtworks.twist.driver.web.user.cocoa;

import java.util.Enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.Shell;

import com.apple.cocoa.application.NSApplication;
import com.apple.cocoa.application.NSEvent;
import com.apple.cocoa.application.NSWindow;
import com.apple.cocoa.foundation.NSPoint;
import com.thoughtworks.twist.driver.web.user.User;
import com.thoughtworks.twist.driver.web.user.KeyTranslator.TransaltedKey;

@SuppressWarnings({ "deprecation", "restriction" })
public class CocoaUser implements User {
	private NSWindow window;
	private CocoaKeyTranslator translator;
	private int modifiers;
	private final Shell shell;

	private static NSWindow findCocoaWindowForShell() {
		NSApplication application = NSApplication.sharedApplication();
		Enumeration<?> windows = application.windows()
				.reverseObjectEnumerator();
		return (NSWindow) windows.nextElement();
	}

	public CocoaUser(Shell shell) {
		this(findCocoaWindowForShell(), shell);
	}

	public CocoaUser(NSWindow window, Shell shell) {
		this.window = window;
		this.shell = shell;
		translator = new CocoaKeyTranslator();
		ensureWindowCanAcceptEvents();
	}

	public void click(int x, int y) {
		click(x, y, OS.kEventMouseButtonPrimary, NSEvent.LeftMouseDown,
				NSEvent.LeftMouseUp);
	}

	public void doubleClick(int x, int y) {
		click(x, y);
		mouseEvent(NSEvent.LeftMouseDown, x, y, OS.kEventMouseButtonPrimary, 2,
				1);
		mouseEvent(NSEvent.LeftMouseUp, x, y, OS.kEventMouseButtonPrimary, 2, 0);
	}

	public void rightClick(int x, int y) {
		click(x, y, OS.kEventMouseButtonSecondary, NSEvent.RightMouseDown,
				NSEvent.RightMouseUp);
	}

	public void dragAndDrop(int startX, int startY, int endX, int endY) {
		int button = OS.kEventMouseButtonPrimary;
		mouseEvent(NSEvent.MouseMoved, startX, startY, button, 1, 0);
		mouseEvent(NSEvent.LeftMouseDown, startX, startY, button, 1, 1);
		// dragEvents(startX, startY, endX, endY, button, 10);
		mouseEvent(NSEvent.LeftMouseDragged, endX, endY, button, 1, 1);
		mouseEvent(NSEvent.LeftMouseUp, endX, endY, button, 1, 0);
	}

	// private void dragEvents(int startX, int startY, int endX, int endY, int
	// button, int speed) {
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
	// mouseEvent(NSEvent.LeftMouseDragged, x, y, button, 1, 1);
	// }
	// }

	public void type(String string) {
		for (int i = 0; i < string.length(); i++) {
			key(string.charAt(i));
		}
	}

	public void key(int c) {
		if (translator.shouldTranslateKey(c)) {
			TransaltedKey transaltedKey = translator.translate(c);
			key(transaltedKey.charCode, transaltedKey.keyCode, true);
		} else {
			key(isShiftDown() ? Character.toUpperCase(c) : c, 0, true);
		}
	}

	public void key(int c, int modifiers) {
		boolean shift = (modifiers & SWT.SHIFT) > 0;
		boolean control = (modifiers & SWT.CONTROL) > 0;
		boolean alt = (modifiers & SWT.ALT) > 0;
		boolean command = (modifiers & SWT.COMMAND) > 0;

		if (shift) {
			shiftDown();
		}
		if (control) {
			controlDown();
		}
		if (alt) {
			altDown();
		}
		if (command) {
			commandDown();
		}

		key(c);

		if (command) {
			commandUp();
		}
		if (alt) {
			altUp();
		}
		if (control) {
			controlUp();
		}
		if (shift) {
			shiftUp();
		}
	}

	public void shiftDown() {
		modifiers |= NSEvent.ShiftKeyMask;
	}

	public void shiftUp() {
		modifiers ^= NSEvent.ShiftKeyMask;
	}

	public boolean isShiftDown() {
		return (modifiers & NSEvent.ShiftKeyMask) > 0;
	}

	public void commandDown() {
		modifiers |= NSEvent.CommandKeyMask;
	}

	public void commandUp() {
		modifiers ^= NSEvent.CommandKeyMask;
	}

	public void controlDown() {
		modifiers |= NSEvent.ControlKeyMask;
	}

	public void controlUp() {
		modifiers ^= NSEvent.ControlKeyMask;
	}

	public void altDown() {
		modifiers |= NSEvent.AlternateKeyMask;
	}

	public void altUp() {
		modifiers ^= NSEvent.AlternateKeyMask;
	}

	private void key(int c, int keyCode, boolean wasTranslated) {
		keyEvent(NSEvent.KeyDown, (char) c, (short) keyCode, wasTranslated);
		keyEvent(NSEvent.KeyUp, (char) c, (short) keyCode, wasTranslated);
	}

	private void click(int x, int y, int button, int mouseDownEventType,
			int mouseUpEventType) {
		mouseEvent(NSEvent.MouseMoved, x, y, button, 1, 0);
		mouseEvent(mouseDownEventType, x, y, button, 1, 1);
		mouseEvent(mouseUpEventType, x, y, button, 1, 0);
	}

	private void mouseEvent(int type, final int x, final int y, int button,
			int clickCount, float pressure) {
		NSEvent mouseEvent = NSEvent.mouseEvent(type, new NSPoint(x, window
				.contentView().frame().height()
				- y), NSEvent.MouseEnteredMask, 0.0, window.windowNumber(),
				NSApplication.sharedApplication().context(), 0, clickCount,
				pressure);

		ensureWindowCanAcceptEvents();
		window.sendEvent(mouseEvent);
		window.displayIfNeeded();
	}

	private void keyEvent(int type, final char c, final short keyCode,
			boolean wasTranslated) {
		String string = new String(new char[] { c });
		int mask = modifiers;
		// int mask = NSEvent.MouseEnteredMask | modifiers;
		// if (c == 0 || wasTranslated) {
		// mask = mask | NSEvent.OtherMouseDownMask | NSEvent.
		// OtherMouseDraggedMask;
		// }

		NSEvent keyEvent = NSEvent.keyEvent(type, new NSPoint(), mask, 0.0, 0,
				NSApplication.sharedApplication().context(), string, string,
				false, keyCode);

		ensureWindowCanAcceptEvents();
		window.sendEvent(keyEvent);
		window.displayIfNeeded();
	}

	private void ensureWindowCanAcceptEvents() {
		shell.setFocus();
		if (window != null) {			
			window.becomeKeyWindow();
		}
	}
}
