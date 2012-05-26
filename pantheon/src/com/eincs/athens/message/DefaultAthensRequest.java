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
package com.eincs.athens.message;

import java.net.InetAddress;
import java.net.InetSocketAddress;


import org.jboss.netty.handler.codec.http.HttpMethod;

import com.eincs.athens.AddressProvider;
import com.eincs.athens.message.attach.AthensAttaches;
import com.eincs.athens.message.attach.AthensParams;

/**
 * @author roth2520@gmail.com
 */
public class DefaultAthensRequest extends DefaultAthensMessage implements
		AthensRequest {

	private HttpMethod method;
	private String url;
	private String path;
	private AthensParams params;
	private AthensAttaches attaches;
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
	public AthensParams getParams() {
		return this.params;
	}

	@Override
	public AthensAttaches getAttachs() {
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

	public AthensAttaches getAttaches() {
		return attaches;
	}

	public void setAttaches(AthensAttaches attaches) {
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

	public void setParams(AthensParams params) {
		this.params = params;
	}
}