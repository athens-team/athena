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
package com.eincs.pantheon.message;

import java.net.InetAddress;
import java.net.InetSocketAddress;


import org.jboss.netty.handler.codec.http.HttpMethod;

import com.eincs.pantheon.AddressProvider;
import com.eincs.pantheon.message.attach.PanteonAttaches;
import com.eincs.pantheon.message.attach.PanteonParams;

/**
 * @author roth2520@gmail.com
 */
public class DefaultPanteonRequest extends DefaultPanteonMessage implements
		PanteonRequest {

	private HttpMethod method;
	private String url;
	private String path;
	private PanteonParams params;
	private PanteonAttaches attaches;
	private AddressProvider addrProvider;

	@Override
	public HttpMethod getMethod() {
		return this.method;
	}

	@Override
	public String getUri() {
		return this.url;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public PanteonParams getParams() {
		return this.params;
	}

	@Override
	public PanteonAttaches getAttachs() {
		return this.attaches;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return addrProvider.getRemoteAddress();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return addrProvider.getLocalAddress();
	}

	@Override
	public InetAddress getOriginAddress() {
		return addrProvider.getOriginAddress();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PanteonAttaches getAttaches() {
		return attaches;
	}

	public void setAttaches(PanteonAttaches attaches) {
		this.attaches = attaches;
	}

	public AddressProvider getAddressProvider() {
		return addrProvider;
	}

	public void setAddressProvider(AddressProvider addrProvider) {
		this.addrProvider = addrProvider;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setParams(PanteonParams params) {
		this.params = params;
	}
}