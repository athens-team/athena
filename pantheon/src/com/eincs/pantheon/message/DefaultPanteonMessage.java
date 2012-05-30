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
package com.eincs.pantheon.message;

import com.eincs.pantheon.PanteonLifeCycle;
import com.eincs.pantheon.PanteonLifeCycleLIstener;
import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonHeaders;
import com.eincs.pantheon.message.attach.PanteonTags;


/**
 * @author roth2520@gmail.com
 */
public class DefaultPanteonMessage implements PanteonMessage {

	protected PanteonTags tags;
	protected PanteonCookies cookies;
	protected PanteonHeaders headers;
	protected PanteonLifeCycle lifeCycle;
	
	public PanteonLifeCycle getLifeCycle() {
		return lifeCycle;
	}
	
	@Override
	public PanteonHeaders getHeaders() {
		return headers;
	}

	@Override
	public void addListener(PanteonLifeCycleLIstener listener) {
		this.lifeCycle.addListener(listener);
	}

	@Override
	public PanteonCookies getCookies() {
		return cookies;
	}
	
	@Override
	public PanteonTags getTags() {
		return tags;
	}

	public void setTags(PanteonTags tags) {
		this.tags = tags;
	}

	public void setHeaders(PanteonHeaders headers) {
		this.headers = headers;
	}
	
	public void setCookies(PanteonCookies cookies) {
		this.cookies = cookies;
	}

	public void setLifeCycle(PanteonLifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}
	
}