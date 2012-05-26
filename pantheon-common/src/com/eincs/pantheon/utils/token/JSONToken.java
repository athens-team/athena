/*
 * Copyright 2012 Athens Team
 * 
 * This file to you under the Apache License, version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eincs.pantheon.utils.token;

import org.json.JSONObject;


/**
 * @author roth2520@gmail.com
 * @param <T>
 */
public class JSONToken implements Token<JSONObject> {

	private long expiry;
	private long timestamp;
	private byte[] signature;
	private JSONObject content; 
	
	@Override
	public long getExpiry() {
		return expiry;
	}

	@Override
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Override
	public byte[] getSignature() {
		return signature;
	}

	@Override
	public void setContent(JSONObject content) {
		this.content = content;
	}

	@Override
	public JSONObject getContent() {
		return content;
	}
}
