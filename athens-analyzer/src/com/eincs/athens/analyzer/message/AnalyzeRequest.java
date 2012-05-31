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
package com.eincs.athens.analyzer.message;

import java.io.Serializable;

import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonParams;


/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeRequest implements Serializable {
	
	private static final long serialVersionUID = -5509385458282534203L;

	public static AnalyzeRequest create(long reqSeq, TargetKey targetKey,
			PanteonRequest request) {
		AnalyzeRequest result = new AnalyzeRequest();
		result.setRequestSeq(reqSeq);
		result.setTargetKey(targetKey);
		result.setParams(request.getParams());
		result.setCookies(request.getCookies());
		result.setTags(AnalyzeTags.create());
		return result;
	}
	
	private long requestSeq;
	
	private TargetKey targetKey;
	
	private PanteonParams params;
	
	private PanteonCookies cookies;

	private AnalyzeTags tags;
	
	public long getRequestSeq() {
		return requestSeq;
	}

	public void setRequestSeq(long requestSeq) {
		this.requestSeq = requestSeq;
	}

	public TargetKey getTargetKey() {
		return targetKey;
	}

	public void setTargetKey(TargetKey targetKey) {
		this.targetKey = targetKey;
	}

	public PanteonParams getParams() {
		return params;
	}

	public void setParams(PanteonParams params) {
		this.params = params;
	}

	public PanteonCookies getCookies() {
		return cookies;
	}

	public void setCookies(PanteonCookies cookies) {
		this.cookies = cookies;
	}

	public AnalyzeTags getTags() {
		return tags;
	}

	public void setTags(AnalyzeTags tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("seq:").append(requestSeq).append(", ");
		sb.append("key:").append(targetKey).append(", ");
		sb.append("params:").append(params).append(", ");
		sb.append("cookies:").append(cookies).append(", ");
		sb.append("tags:").append(tags).append(", ");
		sb.append("]");
		return sb.toString();
	}
}
