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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;


import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.eincs.pantheon.AddressProvider;

/**
 * @author roth2520@gmail.com
 */
public class HttpAddressProvider implements AddressProvider {
	
	private final Channel channel;
	private final HttpRequest httpRequest;
	private InetSocketAddress remoteAddress;
	private InetSocketAddress localAddress;
	private InetAddress originAddress;
	
	public HttpAddressProvider(Channel channel, HttpRequest httpRequest){
		this.channel = channel;
		this.httpRequest = httpRequest;
	}
	
	public InetAddress getOriginAddress() {
		if (originAddress == null){
			HttpRequest request = httpRequest;
			List<String> forwardAddresses = request.getHeaders("X-Forwarded-For");
			if (!forwardAddresses.isEmpty()) {
				try {
					originAddress = InetAddress.getByName(forwardAddresses.get(0));
				} catch (Exception e) {
				}
			}
		}
		return originAddress;
	}
	
	@Override
	public InetSocketAddress getRemoteAddress() {
		if (remoteAddress == null){
			remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
		}
		return remoteAddress;
	}
	
	@Override
	public InetSocketAddress getLocalAddress() {
		if (localAddress == null){
			localAddress = (InetSocketAddress) channel.getLocalAddress();
		}
		return localAddress;
	}
}
