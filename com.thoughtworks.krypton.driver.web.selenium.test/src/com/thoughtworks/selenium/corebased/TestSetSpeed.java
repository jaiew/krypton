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
package com.thoughtworks.selenium.corebased;
import com.thoughtworks.selenium.*;
/**
 * @author XlateHtmlSeleneseToJava
 * Generated from /private/tmp/selenium-rc/clients/java/target/selenium-server/tests/TestSetSpeed.html.
 */
public class TestSetSpeed extends SeleneseTestCase
{
   public void testSetSpeed() throws Throwable {
		try {
			

/* Test setSpeed */
			// storeSpeed|lastSpeed|
			String lastSpeed = selenium.getSpeed();

			/* The max value in slider is 1000, but setSpeed command can set higher than this */
			// setSpeed|1600|
			selenium.setSpeed("1600");
			// verifySpeed|1600|
			verifyEquals("1600", selenium.getSpeed());
			// setSpeed|500|
			selenium.setSpeed("500");
			// verifySpeed|500|
			verifyEquals("500", selenium.getSpeed());

			/* Negative value should be treated as 0 */
			// setSpeed|0|
			selenium.setSpeed("0");
			// verifySpeed|0|
			verifyEquals("0", selenium.getSpeed());
			// setSpeed|-100|
			selenium.setSpeed("-100");
			// verifySpeed|0|
			verifyEquals("0", selenium.getSpeed());
			// setSpeed|${lastSpeed}|
			selenium.setSpeed(lastSpeed);
			// pause|100|
			pause(100);

			checkForVerificationErrors();
		}
		finally {
			clearVerificationErrors();
		}
	}
}
