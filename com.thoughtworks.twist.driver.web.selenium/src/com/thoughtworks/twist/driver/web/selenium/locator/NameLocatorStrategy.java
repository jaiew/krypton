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
package com.thoughtworks.twist.driver.web.selenium.locator;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.thoughtworks.twist.driver.web.browser.BrowserSession;

public class NameLocatorStrategy extends AttributeLocatorStrategy {
	private List<Filter> filters = new ArrayList<Filter>();

	public NameLocatorStrategy() {
		super("name", "name");
		filters.add(new NameFilter());
		filters.add(new IndexFilter());
		filters.add(new ValueFilter());
	}

	public Element locate(BrowserSession session, String locator) {
		locator = locator.substring(prefix.length());
		String[] locatorAndFilter = locator.split(" ");
		List<Node> locateAll = session.locateAll("//*[@" + attribute
				+ "='" + locatorAndFilter[0] + "']");

		if (locatorAndFilter.length > 1) {
			for (Filter filter : filters) {
				if (filter.canFilter(locatorAndFilter[1])) {
					List<Element> matches = filter.matches(locateAll,
							locatorAndFilter[1]);
					if (!matches.isEmpty()) {						
						return matches.get(0);
					}
				}
			}
			return null;
		} else {
			if (locateAll.isEmpty()) {
				return null;
			}
			return (Element) locateAll.get(0);
		}
	}

	private static class AttributeFilter implements Filter {
		private String attribute;
		private String prefix;

		public AttributeFilter(String attribute) {
			this.attribute = attribute;
			this.prefix = attribute + "=";
		}

		public boolean canFilter(String filter) {
			return filter.startsWith(prefix);
		}

		public List<Element> matches(List<Node> list, String filter) {
			if (filter.startsWith(prefix)) {
				filter = filter.substring(prefix.length());
			}
			List<Element> matching = new ArrayList<Element>();
			for (int i = 0; i < list.size(); i++) {
				Element element = (Element) list.get(i);
				if (filter.equals(element.getAttribute(attribute))) {
					matching.add(element);
				}
			}
			return matching;
		}
	}

	private static class IndexFilter implements Filter {
		private static final String PREFIX = "index=";

		public boolean canFilter(String filter) {
			return filter.startsWith(PREFIX);
		}

		public List<Element> matches(List<Node> list, String filter) {
			int index = Integer.parseInt(filter.substring(PREFIX.length()));
			List<Element> matching = new ArrayList<Element>();
			for (int i = 0; i < list.size(); i++) {
				Element element = (Element) list.get(i);
				if (i == index) {
					matching.add(element);
				}
			}
			return matching;
		}
	}

	private final class NameFilter extends AttributeFilter {
		public NameFilter() {
			super("name");
		}
	}

	private final class ValueFilter extends AttributeFilter {
		public ValueFilter() {
			super("value");
		}

		public boolean canFilter(String filter) {
			return true;
		}
	}

	private static interface Filter {
		boolean canFilter(String filter);

		List<Element> matches(List<Node> list, String filter);
	}

}
