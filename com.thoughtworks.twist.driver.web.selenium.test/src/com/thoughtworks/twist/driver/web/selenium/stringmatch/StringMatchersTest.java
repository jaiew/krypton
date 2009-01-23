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
package com.thoughtworks.twist.driver.web.selenium.stringmatch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class StringMatchersTest {
    @Test
    public void shouldMatchRegexp() throws Exception {
        StringMatcher matcher = new RegexpMatcher();
        String pattern = "regexp:.*World";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello World"));
    }

    @Test
    public void shouldMatchGlobExplicitly() throws Exception {
        StringMatcher matcher = new GlobMatcher();
        String pattern = "glob:*Wor?d";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello World"));
    }

    @Test
    public void shouldMatchGlobImplicitly() throws Exception {
        StringMatcher matcher = new GlobMatcher();
        String pattern = "*Wor?d";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello World"));
    }
    
    @Test
    public void shouldMatchMultilineGlobImplicitly() throws Exception {
        StringMatcher matcher = new GlobMatcher();
        String pattern = "Hello*World";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello some other text\n World"));
    }

    @Test
    public void shouldMatchMultilineRegexp() throws Exception {
        StringMatcher matcher = new RegexpMatcher();
        String pattern = "regexp:Hello.*World";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello some other text\n World"));
    }

    @Test
    public void shoudlMatchExact() throws Exception {
        StringMatcher matcher = new ExactMatcher();
        String pattern = "exact:Hello World";
        assertTrue(matcher.canMatch(pattern));
        assertTrue(matcher.matches(pattern, "Hello World"));        
    }
}
