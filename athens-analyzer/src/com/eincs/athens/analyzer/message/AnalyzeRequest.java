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
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonParams;


/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeRequest implements Serializable {
	
	private static final long serialVersionUID = -5509385458282534203L;

	public static AnalyzeRequest create(long reqSeq, PanteonRequest request) {
		AnalyzeRequest result = new AnalyzeRequest();
		result.setRequestSeq(reqSeq);
		result.setLocalAddress(request.getLocalAddress());
		result.setOriginAddress(request.getOriginAddress());
		result.setRemoteAddress(request.getRemoteAddress());
		result.setMethod(request.getMethod().getName());
		result.setPath(request.getPath());
		result.setParams(request.getParams());
		result.setCookies(request.getCookies());
		result.setTags(AnalyzeTags.create());
		return result;
	}
	
	private long requestSeq;
	
	private InetSocketAddress remoteAddress;
	
	private InetSocketAddress localAddress;
	
	private InetAddress originAddress;
	
	private String method;
	
	private String path;
	
	private PanteonParams params;
	
	private PanteonCookies cookies;

	private AnalyzeTags tags;
	
	public long getRequestSeq() {
		return requestSeq;
	}

	public void setRequestSeq(long requestSeq) {
		this.requestSeq = requestSeq;
	}

	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetSocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public InetAddress getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(InetAddress inetAddress) {
		this.originAddress = inetAddress;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
		sb.append("method:").append(method).append(", ");
		sb.append("path:").append(path).append(", ");
		sb.append("params:").append(params).append(", ");
		sb.append("local:").append(localAddress).append(", ");
		sb.append("remote:").append(remoteAddress).append(", ");
		sb.append("origin:").append(originAddress);
		sb.append("]");
		return sb.toString();
	}
}
