Twist Driver for Web
====================
Copyright 2008, 2009 ThoughtWorks Studios

Includes:
 - SWT, Copyright 2000, 2008 IBM Corporation and others. (Eclipse Public License v1.0)
 - Eclipse, Copyright 2003, 2008 IBM Corporation and others. (Eclipse Public License v1.0)
 - Equinox, Copyright 2008 IBM Corporation and others. (Eclipse Public License v1.0)
 - OSGi, Copyright OSGi Alliance (2000, 2008). (Apache License, Version 2.0)
 - Commons Logging Pacakge, (Apache License, Version 2.0)
 - Selenium RC, Copyright 2006 ThoughtWorks, Inc. (Apache License, Version 2.0)


The Twist Driver for Web drives the SWT Browser using native OS events.


Dependencies:
=============

Requirements:
 Mac OS X, Linux (with GTK) or Windows
 Java 5
 Ant 1.7
 svn

Mozilla:
 XULRunner 1.8.1.3 or higher for Mozilla - Firefox 2 support.
 XULRunner 1.9 or higher for Mozilla - Firefox 3 support.
 There will be a default XULRunner (most likely the last installed), but you can change which XULRunner to use with the org.eclipse.swt.browser.XULRunnerPath Java system (-D) property.

IE:
 Windows will use the default IE engine installed, so to test on both IE 6 and IE 7 you'll need two boxes.

For running IE6 on Mac OS X or Linux:
 Darwine or wine
 ies4osx or ies4linux
 JRE 5 inside the same WINEPREFIX (default is /Applications/Internet\ Explorer\ 6.0.app/Contents/Resources/ie6/ or ~/.ies4linux/ie6)
 Download the Win98 JRE from http://java.com/en/download/windows98me_manual.jsp?locale=en&host=java.com:80, install using something similar to WINEPREFIX=<see above> wine jre-1_5_0_17-windows-i586-p-s.exe

Safari:
 Mac OS X will use the default Safari WebKit, so to test on both Safari 2 and Safari 3 you'll need two boxes.


Building:
=========

Run ant -q to build all the projects. Runt ant all-tests to run the Selenium suite in parallel in all installed and supported browsers.
Ant and svn have to both be on the path.


Unit/Integration Testing:
=========================

Lives in com.thoughtworks.twist.driver.web.test. JUnit 4 is used. You can run any tests from within Eclipse. Not all tests pass. The way to run tests for both Mozilla and Safari based on subclassing is simplistic (but works for now).


Acceptance Testing:
===================

Lives in com.thoughtworks.twist.driver.web.selenium.test. Uses the same test suite as Selenium RC with a modified SeleneseTestCase super class, which browser to use is currently specified here.

Most tests in com.thoughtworks.selenium.corebased should pass. The package corebaseskip contains the rest of the Selenium RC test suite, these mostly deal with windows.



Architecture:
=============

com.thoughtworks.twist.driver.web.internal and com.thoughtworks.twist.driver.web.user contain the actual low level driver. The two major types here are User and BrowserSession. A User abstracts (native OS) event generation, and BrowserSession is the combination of an actual SWT Browser instance and a User controlling it. BrowserSession also provides a basic querying, locating and wait mechanism.

com.thoughtworks.twist.driver.web.selenium is an (partial) implementation of the Selenium interface on top of the BrowserSession. Some parts of this layer will most likely be factored down into com.thoughtworks.twist.driver.web.


Embedded OSGi:
==============

Because it's dependency on SWT, this driver is a set of OSGi bundles. To greatly simplfy the end user experience, they are all bundled up into a single jar file as a small Eclipse distro, which gets launched internally by the EmbeddedEquinoxLauncher using reflection. Hence, all environments use the same single jar and classpath.
