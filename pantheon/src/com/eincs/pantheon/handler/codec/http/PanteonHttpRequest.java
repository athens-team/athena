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
package com.eincs.pantheon.handler.codec.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.jboss.netty.handler.codec.http.Attribute;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.FileUpload;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.InterfaceHttpData.HttpDataType;

import com.eincs.pantheon.HttpRequestWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author roth2520@gmail.com
 */
public class PanteonHttpRequest extends HttpRequestWrapper {

	private String path;
	private List<InterfaceHttpData> datas;
	private Map<String, List<String>> params;
	
	public PanteonHttpRequest(HttpRequest request) {
		super(request);
		this.path = null;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return Strings.nullToEmpty(path);
	}
	
	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}
	
	public boolean hasParams() {
		return params != null && params.size() > 0;
	}
	
	public Map<String, List<String>> getParams() {
		final Map<String, List<String>> result = Maps.newHashMap();
		if(hasParams()) {
			result.putAll(params);
		}
		return Collections.unmodifiableMap(result);
	}
	
	public void setHttpDatas(List<InterfaceHttpData> datas) {
		this.datas = datas;
	}

	public boolean hasHttpDatas() {
		return datas != null && datas.size() > 0;
	}
	public List<InterfaceHttpData> getHttpDatas() {
		return datas;
	}
	
	public boolean hasFileUploads() {
		if (hasHttpDatas()) {
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() == HttpDataType.FileUpload) { return true; }
			}
		}
		return false;
	}
	
	public List<FileUpload> getFileUploads() {
		final List<FileUpload> result = Lists.newArrayList();
		if (hasHttpDatas()) {
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() == HttpDataType.FileUpload) {
					result.add((FileUpload) data);
				}
			}
		}
		return Collections.unmodifiableList(result);
	}
	
	public boolean hasAttributes() {
		if (hasHttpDatas()) {
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() == HttpDataType.Attribute) { return true; }
			}
		}
		return false;
	}
	
	public List<Attribute> getAttributes() {
		final List<Attribute> result = Lists.newArrayList();
		if (hasHttpDatas()) {
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					result.add((Attribute) data);
				}
			}
		}
		return Collections.unmodifiableList(result);
	}
	
	public boolean isKeepAlive() {
		return HttpHeaders.isKeepAlive(getRequest());
	}

	public String getUserAgent() {
		return getHeader(HttpHeaders.Names.USER_AGENT);
	}

	public Set<Cookie> getCookies() {
		Set<Cookie> result = Sets.newHashSet();
		List<String> cookieHeaders = getHeaders(HttpHeaders.Names.COOKIE);
		CookieDecoder decoder = new CookieDecoder();
		for(String cookieHeader : cookieHeaders) {
			result.addAll(decoder.decode(cookieHeader));
		}
		return result;
	}
}
