/*
 * Copyright 2012 Athens Team
 *
 * This file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.rothlee.athens.message.attach;

import java.util.Iterator;
import java.util.Set;

import net.rothlee.athens.utils.collections.SetWrapper;

import org.jboss.netty.handler.codec.http.Cookie;

import com.google.common.collect.Sets;

/**
 * @author roth2520@gmail.com
 */
public final class AthensCookies extends SetWrapper<Cookie> {

	public static AthensCookies create() {
		AthensCookies result = new AthensCookies();
		return result;
	}
	
	public static AthensCookies create(Set<Cookie> cookie) {
		AthensCookies result = new AthensCookies();
		result.addAll(cookie);
		return result;
	}
	
	private AthensCookies() { super(); }
	
	public Cookie getCookieByName(String name) {
		Iterator<Cookie> iter = iterator();
		while(iter.hasNext()) {
			Cookie cookie = iter.next();
			if(cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}
	
	public Set<Cookie> getCookiesByName(String name) {
		Set<Cookie> result = Sets.newHashSet();
		Iterator<Cookie> iter = iterator();
		while(iter.hasNext()) {
			Cookie cookie = iter.next();
			if(cookie.getName().equals(name)) {
				result.add(cookie);
			}
		}
		return result;
	}
}