package com.thoughtworks.krypton.driver.cocoa;

import org.rococoa.NSClass;
import org.rococoa.NSObject;
import org.rococoa.Rococoa;

public interface NSWindow extends NSObject {

	public static final Class CLASS = Rococoa.createClass("NSWindow", Class.class);

	public interface Class extends NSClass {

	}

	void setAutodisplay(boolean autodisplay);

	void disableFlushWindow();

	void enableFlushWindow();

	void flushWindowIfNeeded();

	void becomeKeyWindow();

	NSView contentView();

	int windowNumber();
	
	void postEvent_atStart(NSEvent event, boolean atStartFlag);

}
