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
package com.thoughtworks.twist.driver.web.user.gtk;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.gtk.GdkEventButton;
import org.eclipse.swt.internal.gtk.GdkEventKey;
import org.eclipse.swt.internal.gtk.GdkEventMotion;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.thoughtworks.twist.driver.web.user.KeyTranslator;
import com.thoughtworks.twist.driver.web.user.User;

@SuppressWarnings("restriction")
public class GtkUser implements User {
	private static final int GDK_MOTION_NOTIFY = 3;
	private final Shell window;
	private KeyTranslator translator = new GtkKeyTranslator();
	private boolean shift;

	public GtkUser(Shell window) {
		this.window = window;
	}

	public void click(int x, int y) {
		mouseMoveEvent(x, y, 0);
		mouseEvent(OS.GDK_BUTTON_PRESS, x, y, 1);
		mouseEvent(OS.GDK_BUTTON_RELEASE, x, y, 1);
	}

	public void rightClick(int x, int y) {
		mouseMoveEvent(x, y, 0);
		mouseEvent(OS.GDK_BUTTON_PRESS, x, y, 2);
		mouseEvent(OS.GDK_BUTTON_RELEASE, x, y, 2);
	}

	public void doubleClick(int x, int y) {
		mouseMoveEvent(x, y, 0);
		mouseEvent(OS.GDK_BUTTON_PRESS, x, y, 1);
		mouseEvent(OS.GDK_BUTTON_RELEASE, x, y, 1);
		mouseEvent(OS.GDK_2BUTTON_PRESS, x, y, 1);
		mouseEvent(OS.GDK_BUTTON_RELEASE, x, y, 1);
	}

	public void dragAndDrop(int startX, int startY, int endX, int endY) {
		mouseMoveEvent(startX, startY, 0);
		mouseEvent(OS.GDK_BUTTON_PRESS, startX, startY, 1);
		mouseMoveEvent(endX, endY, OS.GDK_BUTTON1_MASK);
		mouseEvent(OS.GDK_BUTTON_RELEASE, endX, endY, 1);
	}

	public void type(String string) {
		for (char c : string.toCharArray()) {
			key(shift ? Character.toUpperCase(c) : c);
		}
	}

	public void key(int keyCode) {
		if (translator.shouldTranslateKey(keyCode)) {
			int translatedKey = translator.translate(keyCode).keyCode;
			keyEvent(OS.GDK_KEY_PRESS, translatedKey, false);
			keyEvent(OS.GDK_KEY_RELEASE, translatedKey, false);
		} else {
			keyEvent(OS.GDK_KEY_PRESS, keyCode, true);
			keyEvent(OS.GDK_KEY_RELEASE, keyCode, true);
		}
	}

	public void key(int keyCode, int modifiers) {
		throw new UnsupportedOperationException();
	}

	public boolean isShiftDown() {
		return shift;
	}

	public void shiftDown() {
		shift = true;
	}

	public void shiftUp() {
		shift = false;
	}

	private void mouseEvent(int type, final int x, final int y, int button) {
		int event = OS.malloc(GdkEventButton.sizeof);
		GdkEventButton gdkEventButton = new GdkEventButton();

		int handle = getHandle();
		Point point = window.toDisplay(x, y);

		gdkEventButton.type = type;
		gdkEventButton.x = x;
		gdkEventButton.y = y;
		gdkEventButton.x_root = point.x;
		gdkEventButton.y_root = point.y;
		gdkEventButton.window = handle;
		gdkEventButton.button = button;
		gdkEventButton.time = (int) System.currentTimeMillis();
		gdkEventButton.state = type == OS.GDK_BUTTON_RELEASE ? (button == 1 ?
		 OS.GDK_BUTTON1_MASK : OS.GDK_BUTTON2_MASK)
		 : 0;

		OS.memmove(event, gdkEventButton, GdkEventButton.sizeof);

		OS.gtk_main_do_event(event);
		OS.free(event);
	}

	private void mouseMoveEvent(int x, int y, int state) {
		int event = OS.malloc(GdkEventMotion.sizeof);
		Point point = window.toDisplay(x, y);
		
		OS.memmove(event, new int[] { GDK_MOTION_NOTIFY,
				getHandle(), 0,
				(int) System.currentTimeMillis(), 0, doubleToLongHiBits(x), doubleToLongLowBits(x), 
				doubleToLongHiBits(y), doubleToLongLowBits(y), 0, state, 0, 0, doubleToLongHiBits(point.x), doubleToLongLowBits(point.x), doubleToLongHiBits(point.y), doubleToLongLowBits(point.y) },
				GdkEventMotion.sizeof);
		
		OS.gtk_main_do_event(event);
		pumpEvents();
		OS.free(event);
	}

	private int doubleToLongHiBits(int value) {
		return (int) (Double.doubleToLongBits((double) value) >> 32);
	}

	private int doubleToLongLowBits(int value) {
		return (int) (Double.doubleToLongBits((double) value) & 0xffffffffl);
	}

	private void keyEvent(int type, int c, boolean unicode) {
		int event = OS.malloc(GdkEventKey.sizeof);
		OS.memmove(event, new int[] { type,
				getHandle(), 0,
				(int) System.currentTimeMillis(), 0,
				unicode ? OS.gdk_unicode_to_keyval(c) : c, 0, 0, 0 },
				GdkEventKey.sizeof);

		OS.gtk_main_do_event(event);
		pumpEvents();
		OS.free(event);
	}

	private void pumpEvents() {
		while (!window.isDisposed() && window.getDisplay().readAndDispatch())
			;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
	}

	private int getHandle() {
		for (Control c : window.getChildren()) {
			if (c instanceof Browser) {
				return findTargetWindow(OS.GTK_WIDGET_WINDOW(c.handle));
			}
		}
		return -1;
	}
	
	private int findTargetWindow(int window) {
		int windows = OS.gdk_window_get_children(window);
		int length = OS.g_list_length(windows);
		if (length == 0) {
			return window;
		}
		return findTargetWindow(OS.g_list_nth_data(windows, 0));
	}
}
