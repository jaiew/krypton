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
package com.thoughtworks.krypton.driver.web.browser.wait;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import com.thoughtworks.krypton.driver.web.browser.BrowserSession;

public class LocationChangedWaitStrategy implements LocationListener, WaitStrategy {
    private boolean changing;

    List<String> exclusionPatterns = new ArrayList<String>();
    Logger log = LoggerFactory.getLogger(getClass());

    public void init(BrowserSession session) {
        session.getBrowser().addLocationListener(this);

        addURLExclusionPattern("javascript:.*");
        addURLExclusionPattern("about:blank");
        addURLExclusionPattern("about:config");

        // This obviously needs to be configurable somehow.
        addURLExclusionPattern(".*analytics.live.com.*");
        addURLExclusionPattern(".*www.google.com/ig/ifpc_relay.*");
        addURLExclusionPattern(".*doubleclick.net.*");
        addURLExclusionPattern(".*adbrite.*");
    }

    public boolean isBusy() {
        return changing;
    }

    public void changed(LocationEvent event) {
        String location = event.location;
        if (isExcluded(location)) {
            return;
        }
        changing = false;
        log.trace("changed: {}", location);
    }

    public void changing(LocationEvent event) {
        String location = event.location;
        if (isExcluded(location)) {
            log.trace("skipping excluded {}", location);
            return;
        }
        log.trace("changing: {} {}", location, exclusionPatterns);
        changing = true;
    }

	private boolean isExcluded(String location) {
		for (String pattern : exclusionPatterns) {
            if (location.matches(pattern)) {
            	return true;
            }
        }
		return false;
	}
    
    public void addURLExclusionPattern(String pattern) {
        exclusionPatterns.add(pattern);
    }

    public void removeURLExclusionPattern(String pattern) {
        exclusionPatterns.remove(pattern);
    }
}
