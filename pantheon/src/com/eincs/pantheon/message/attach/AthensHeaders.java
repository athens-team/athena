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
package com.eincs.pantheon.message.attach;

import java.util.List;


import com.eincs.pantheon.utils.collections.MapWrapper;
import com.google.common.collect.Lists;

/**
 * @author roth2520@gmail.com
 */
public class AthensHeaders extends MapWrapper<String, String> {

	public static AthensHeaders create() {
		List<Entry<String, String>> emptyList = Lists.newArrayList();
		return create(emptyList);
	}
	
	public static AthensHeaders create(List<Entry<String, String>> headers) {
		AthensHeaders result = new AthensHeaders();
		for(Entry<String, String> entry : headers) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	private AthensHeaders() { super(); }

	public void setCookie(String cookie) {
	}

}
