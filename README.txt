Setting up.

    Install/Setup XULRunner 1.9.X (1.8.X series will not do) . 
    Install XULrunner.
    Change target eclipse to twist-driver-web-osgi/eclipse/eclipse
    On OSX -> set OS to macosx , Windowing system to cocoa , Architecture to x86 (not x86_64) and locale to en_US (even if you are in India!)
    Set JRE to “JVM 1.5.0” ( JVM 1.6 on OSX is 64 bit and it seems to have problems working with 32 bit libs )
    Refresh and clean the projects. You should not have any errors now.
    You should be able to run the tests in eclipse. Most of them are located in com.thoughtworks.twist.driver.selenium

Running tests from the command line:

    The valid values for twist.driver.web.browser are: mozilla, ie and safari. Running ant -Dtwist.driver.web.browser=safari on a Mac should in theory work out of the box.
    IE: “ant selenium-ie” & “ant integration-ie”
    Mozilla : “ant selenium-mozilla” & “ant integration-mozilla”
    for all tests: “ant all-tests”
