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

import com.eincs.pantheon.utils.collections.MapWrapper;

/**
 * @author roth2520@gmail.com
 */
public final class AthensTags extends MapWrapper<String, Object> {

	public static AthensTags create() {
		AthensTags result = new AthensTags();
		return result;
	}
	
	private AthensTags() { super(); }
}