package com.thoughtworks.krypton.driver.cocoa;

import org.rococoa.NSClass;
import org.rococoa.NSObject;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSRect;

public interface NSView extends NSObject {

	public static final Class CLASS = Rococoa.createClass("NSView", Class.class);

	public interface Class extends NSClass {
	}

	NSRect frame();

}
