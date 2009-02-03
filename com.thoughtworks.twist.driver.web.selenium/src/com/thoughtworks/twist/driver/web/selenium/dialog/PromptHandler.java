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
package com.thoughtworks.twist.driver.web.selenium.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class PromptHandler
		implements
			ProgressListener,
			StatusTextListener, DialogHandler {
	private static final String JAVASCRIPT_PROMPT = "javascript-prompt: ";

	private final BrowserSession session;
	private List<String> prompts = new ArrayList<String>();
    Log log = LogFactory.getLog(getClass());

	public PromptHandler(BrowserSession session) {
		this.session = session;
		session.getBrowser().addProgressListener(this);
		session.getBrowser().addStatusTextListener(this);
		completed(null);
	}

	public void changed(ProgressEvent event) {
	}

	public void completed(ProgressEvent event) {
		session.getBrowser()
				.execute("if (!Twist) { var Twist = {}; } if (Twist.promptAnswer === undefined) { Twist.promptAnswer = ''; } window.prompt = function(message, value) { window.status = '"
						+ JAVASCRIPT_PROMPT
						+ "' + message; window.status =''; var answer = Twist.promptAnswer; if (value) answer = value; Twist.promptAnswer = ''; return answer; }");
		session.pumpEvents();
	}

	public void changed(StatusTextEvent event) {
		if (event.text.startsWith(JAVASCRIPT_PROMPT)) {
			prompts
					.add(event.text.substring(JAVASCRIPT_PROMPT.length()));
			log.info("prompt: "
					+ prompts.get(prompts.size() - 1));
		}
	}

	public String getMessage() {
		return prompts.remove(0);
	}

	public boolean isDialogPresent() {
		return !prompts.isEmpty();
	}

	public void answerOnNextPrompt(String answer) {
		session.getBrowser().execute("if (!Twist) { var Twist = {}; } Twist.promptAnswer = '" + answer + "';");
		session.pumpEvents();
	}
}
