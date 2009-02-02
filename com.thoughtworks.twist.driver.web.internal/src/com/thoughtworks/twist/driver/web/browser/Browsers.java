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
package com.thoughtworks.twist.driver.web.browser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.thoughtworks.twist.driver.web.browser.wait.LocationChangedWaitStrategy;
import com.thoughtworks.twist.driver.web.user.User;
import com.thoughtworks.twist.driver.web.user.UserFactory;

public enum Browsers {
    MOZILLA {
        public Browser create(Composite composite) {
            return new Browser(composite, SWT.MOZILLA);
        }
        
        void init(BrowserSession session) {
            configureXULRunnerUsingAboutConfig(session);
            loadAboutBlank(session);
        }

        private void configureXULRunnerUsingAboutConfig(BrowserSession session) {
            session.browser.setUrl("about:config");
            session.waitForActivity();
            
            waitForAboutConfigToInitialize(session);

            setStringPreference(session, "general.useragent.extra.firefox", "Gecko/2008070206 Firefox/3.0.1");
            setStringPreference(session, "general.useragent.extra.firefox", "Firefox/2.0.0.13");

            setBooleanPreference(session, "security.warn_entering_secure", false);
            setBooleanPreference(session, "security.warn_entering_weak", false);
            setBooleanPreference(session, "security.warn_leaving_secure", false);
            setBooleanPreference(session, "security.warn_submit_insecure", false);
            setBooleanPreference(session, "security.warn_viewing_mixed", false);
        }

        private void loadAboutBlank(BrowserSession session) {
            session.browser.setUrl("about:blank");
            session.pumpEvents();
        }

        private void waitForAboutConfigToInitialize(BrowserSession session) {
            long timeout = System.currentTimeMillis() + 4000;
            boolean configLoaded = false;
            while (!configLoaded) {
                try {
                    session.execute("gPrefBranch");
                    configLoaded = true;
                } catch (JavascriptException e) {
                    if (!referenceError("gPrefBranch").equals(e.getMessage())) {
                        throw e;
                    }
                    if (System.currentTimeMillis() >= timeout) {
                        throw new IllegalStateException("Could not load about:config properly");                    
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        }

        private void setPreference(BrowserSession session, String type, String key, Object value) {
            Object javaScriptValue = value instanceof String ? "'" + value + "'" : value;
            session.execute("gPrefBranch.set" + type + "Pref('" + key + "', " + javaScriptValue + ")");
            if (!value.toString().equals(session.execute("gPrefBranch.get" + type + "Pref('" + key + "')"))) {
                throw new IllegalStateException("Failed to change preference " + key + " to " + value);
            }
        }

        private void setStringPreference(BrowserSession session, String key, String value) {
            setPreference(session, "Char", key, value);
        }

        private void setBooleanPreference(BrowserSession session, String key, boolean value) {
            setPreference(session, "Bool", key, value);
        }

        public String referenceError(String reference) {
            return "ReferenceError: " + reference + " is not defined";
        }
    },

    IE {
        public Browser create(Composite composite) {
            String platform = SWT.getPlatform();
            if ("win32".equals(platform)) {                
                return new Browser(composite, SWT.NONE);
            }
            throw new IllegalStateException("Cannot create Internet Explorer on platform: " + platform);
        }

        public String referenceError(String reference) {
            return "[object Error]";
        }
    },

    SAFARI {
        public Browser create(Composite composite) {
            String platform = SWT.getPlatform();
            if ("carbon".equals(platform) || "cocoa".equals(platform)) {                
                return new Browser(composite, SWT.NONE);
            }
            throw new IllegalStateException("Cannot create Safari on platform: " + platform);
        }

        public String referenceError(String reference) {
            return "ReferenceError: Can't find variable: " + reference;
        }
    };

    static {
        Logger.getLogger("").setLevel(Level.FINEST);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
    }
    
    Log log = LogFactory.getLog(getClass());

    public BrowserSession create() {
        final Shell shell = new Shell();
        GridLayout layout = new GridLayout();
        shell.setLayout(layout);
        shell.setSize(1024, 768);
        
        Browser.clearSessions();
        log.debug("Creating browser: " + name());
        Browser browser = create(shell);
        
        layoutBrowser(browser);

        UserFactory userFactory = new UserFactory();
//        log.debug("Creating user: " + userFactory.getUserForPlatform());
        User user = userFactory.createUser(shell);
        BrowserSession session = new BrowserSession(browser, user);
        init(session);
        
        initEmptyDocument(session);

        Text location = addLocationBar(shell);
        Label userAgent = addUserAgentLabel(shell);

        addListeners(shell, location, browser);
        String userAgentString = session.execute("navigator.userAgent");
        userAgent.setText(userAgentString);
        log.info("Initialized BrowserSession using " + userAgentString);

        return session;
    }

//    private void addTwistLogo(Shell shell) {
//        Image image = new Image(shell.getDisplay(), Browsers.class.getResourceAsStream("/products-twist.gif"));        
//        Label logo = new Label(shell, SWT.NONE);
//        GridData data = new GridData();
//        logo.setLayoutData(data);
//        logo.setImage(image);
//    }

    private void addListeners(final Shell shell, final Text location, final Browser browser) {
        browser.addLocationListener(new LocationListener() {
            public void changed(LocationEvent event) {
                if (event.top) {
                    location.setText(event.location);
                }
            }

            public void changing(LocationEvent event) {
            }
        });
        browser.addTitleListener(new TitleListener() {
            public void changed(TitleEvent event) {
                shell.setText(event.title);
            }
        });
    }

    private Label addUserAgentLabel(Shell shell) {
        Label userAgent = new Label(shell, SWT.CENTER);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        userAgent.setLayoutData(data);
        return userAgent;
    }

    private void layoutBrowser(Browser browser) {
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        browser.setLayoutData(data);
    }

    private Text addLocationBar(final Shell shell) {
        Text location = new Text(shell, SWT.BORDER);
        location.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        location.setLayoutData(data);
        return location;
    }

    private void initEmptyDocument(BrowserSession session) {
        LocationChangedWaitStrategy waitStrategy = new LocationChangedWaitStrategy();
        session.addWaitStrategy(waitStrategy);
        session.browser.setUrl("about:blank");
        session.waitForIdle();
        session.removeWaitStrategy(waitStrategy);
    }

    void init(BrowserSession session) {
    }

    abstract Browser create(Composite composite);
    
    public abstract String referenceError(String reference);
    
    public static Browsers fromSystemProperty() {
    	return Browsers.valueOf(System.getProperty("twist.driver.web.browser", "mozilla").toUpperCase());
    }
}
