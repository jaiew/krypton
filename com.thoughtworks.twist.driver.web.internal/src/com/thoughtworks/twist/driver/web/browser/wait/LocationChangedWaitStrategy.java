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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class LocationChangedWaitStrategy implements LocationListener, WaitStrategy {
    private boolean changing;
    
    List<String> exclusionPatterns = new ArrayList<String>();
    Log log = LogFactory.getLog(getClass());

    public void init(BrowserSession session) {
        session.browser.addLocationListener(this);
        
        exclusionPatterns.add("javascript:.*");
        exclusionPatterns.add("about:blank");
        exclusionPatterns.add("about:config");
    }

    public synchronized boolean isBusy() {
        return changing;
    }

    public synchronized void changed(LocationEvent event) {
        changing = false;
        log.trace("changed: " + event.location);
    }

    public synchronized void changing(LocationEvent event) {
        String location = event.location;

        for (String pattern : exclusionPatterns) {
            if (location.matches(pattern)) {
                log.trace("skipping excluded " + location);
                return;
            }
        }
        log.trace("changing: " + location + " " + exclusionPatterns);
        changing = true;
    }
    
    public void addURLExclusionPattern(String pattern) {
        exclusionPatterns.add(pattern);
    }

    public void removeURLExclusionPattern(String pattern) {
        exclusionPatterns.remove(pattern);
    }
}
