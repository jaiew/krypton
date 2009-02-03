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
package com.thoughtworks.twist.driver.web.browser.wait;

import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class DocumentReadyWaitStrategy implements WaitStrategy, LocationListener {
    private BrowserSession session;
    private boolean hasListener;

    public void init(final BrowserSession session) {
        this.session = session;
        session.getBrowser().addLocationListener(this);
    }

    public void changed(LocationEvent event) {
        if ("Mozilla".equals(session.getBrowserName())) {
            session.getBrowser().execute("if (!Twist) { var Twist = {}; }" +
                    "if (!Twist.hasContentLoadListener) { document.addEventListener('DOMContentLoaded', function() { " +
            "Twist.contentLoaded = true; }, false); Twist.hasContentLoadListener = true; }");
            hasListener = true;
        }
    }

    public void changing(LocationEvent event) {
        hasListener = false;
    }

    public boolean isBusy() {
        if ("Safari".equals(session.getBrowserName()) || "IE".equals(session.getBrowserName())) {
            String readyState = session.execute("document.readyState");
            return !"complete".equals(readyState);
        } else if ("Mozilla".equals(session.getBrowserName())) {
            boolean isLoading = Boolean.parseBoolean(session
                    .execute("typeof document.getElementsByTagName == 'undefined' " +
                            (hasListener ? "|| typeof Twist.contentLoaded == 'undefined' " : "") +
                            "|| document.body == null"));
            return isLoading;
        }
        return false;
    }
}
