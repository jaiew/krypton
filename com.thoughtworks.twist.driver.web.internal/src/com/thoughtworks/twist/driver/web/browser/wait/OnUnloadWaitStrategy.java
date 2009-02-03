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
package com.thoughtworks.twist.driver.web.browser.wait;import org.eclipse.swt.browser.ProgressEvent;import org.eclipse.swt.browser.ProgressListener;import com.thoughtworks.twist.driver.web.browser.BrowserSession;import com.thoughtworks.twist.driver.web.browser.JavascriptException;public class OnUnloadWaitStrategy implements ProgressListener, WaitStrategy {    private BrowserSession session;    boolean changed;    boolean isUnloading;    public void init(BrowserSession session) {        this.session = session;        this.session.getBrowser().addProgressListener(this);        completed(null);    }    public boolean isBusy() {        try {            if (session.getBrowser().isDisposed()) {                return false;            }            String unloading = session.execute("typeof(Twist) !== 'undefined' ? Twist.unloading : undefined");            System.out.println(unloading);            if (changed) {                if ("undefined".equals(unloading)) {                    System.out.println("waiting for unload");                    return true;                } else {                    System.out.println("should detect the real unload next");                }            }            if (Boolean.parseBoolean(unloading)) {                System.out.println("detected actual unload");                changed = false;                isUnloading = true;                return true;            } else if (isUnloading) {                System.out.println("detected actual reload");                isUnloading = false;                return false;            }            return false;        } catch (JavascriptException e) {            e.printStackTrace();            return false;        }    }    public void changed(ProgressEvent event) {        if (!isUnloading && event.current == 0) {            System.out.println("detected change");            changed = true;        }    }    public void completed(ProgressEvent event) {        System.out.println("completed");        session.getBrowser()                .execute("if (!Twist) { var Twist = {}; } Twist.unloading = undefined; window.addEventListener('unload', function(event) { Twist.unloading = true; }, true);");    }}
