package com.thoughtworks.krypton.driver.cocoa;

import org.rococoa.NSClass;
import org.rococoa.NSObject;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;

public interface NSApplication extends NSObject {

	public static final Class CLASS = Rococoa.createClass("NSApplication", Class.class);

	public interface Class extends NSClass {
		NSApplication sharedApplication();
	}

	NSArray windows();

	NSObject /* NSGraphicsContext */context();

}
