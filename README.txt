Twist Driver for Web
====================

[A proof of Concept of driving SWT Browser using native OS events.]


Building:
=========

Requirements:
 Mac OS X, Linux or Windows
 Java 5
 Ant
 XULRunner 1.8.1.3 or higher for Mozilla support.

For running IE6 on macosx:
 Darwine
 ie4osx
 Java 5 inside the same  WINEPREFIX (default is /Applications/Internet\ Explorer\ 6.0.app/Contents/Resources/ie6/)



Unit/Integration Testing:
=========================

Lives in com.thoughtworks.twist.driver.web.test. JUnit 4 is used. You can run any tests from within Eclipse. Not all tests pass. The way to run tests for both Mozilla and Safari based on subclassing is simplistic (but works for now).


Acceptance Testing:
===================

Lives in com.thoughtworks.twist.driver.web.selenium.test. Uses the same test suite as Selenium RC with a modified SeleneseTestCase super class, which browser to use is currently specified here.

Most tests in com.thoughtworks.selenium.corebased should pass. The package corebaseskip contains the rest of the Selenium RC test suite, these mostly deal with windows.



Architecture:
=============

com.thoughtworks.twist.driver.web.internal and com.thoughtworks.twist.driver.web.user contain the actual low level driver. The two major types here are User and BrowserSession. A User abstracts event generation, and BrowserSession is the combination of an actual SWT Browser instance and a User controlling it. BrowserSession also provides a basic querying, locating and wait mechanism.

com.thoughtworks.twist.driver.web.selenium is an (partial) implementation of the Selenium interface on top of the BrowserSession. Some parts of this layer will most likely be factored down into com.thoughtworks.twist.driver.web.


Embedded OSGi:
==============

Because it's dependency on SWT, this driver is a set of OSGi bundles. To greatly simplfy the end user experience, they are all bundled up into a single jar file as a small Eclipse distro, which gets launched internally by the EmbeddedEquinoxLauncher using reflection. Hence, all environments use the same single jar and classpath.
