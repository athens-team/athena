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
package com.eincs.pantheon.handler.codec.http;

import java.util.Map.Entry;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.eincs.pantheon.PanteonLifeCycle;
import com.eincs.pantheon.PanteonLifeCycles;
import com.eincs.pantheon.message.PanteonResponse;
import com.eincs.pantheon.message.DefaultPanteonRequest;
import com.eincs.pantheon.message.attach.PanteonAttaches;
import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonHeaders;
import com.eincs.pantheon.message.attach.PanteonParams;
import com.eincs.pantheon.message.attach.PanteonTags;

/**
 * @author roth2520@gmail.com
 */
public class PanteonHttpProcessor extends SimpleChannelHandler {

	private final PanteonLifeCycles lifeCycles = new PanteonLifeCycles();

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (e.getMessage() instanceof PanteonHttpRequest) {
			final PanteonHttpRequest httpRequest = (PanteonHttpRequest) e
					.getMessage();
			final PanteonHttpResponse httpResponse = new PanteonHttpResponse(
					httpRequest);

			/* request properties */
			DefaultPanteonRequest request = new DefaultPanteonRequest();
			request.setMethod(httpRequest.getMethod());
			request.setUrl(httpRequest.getUri());
			request.setPath(httpRequest.getPath());
			request.setHeaders(PanteonHeaders.create(httpRequest.getHeaders()));
			request.setParams(PanteonParams.create(httpRequest.getParams()));
			request.setAttaches(PanteonAttaches.create(httpRequest
					.getFileUploads()));
			request.setCookies(PanteonCookies.create(httpRequest.getCookies()));

			/* internals */
			PanteonLifeCycle lifeCycle = lifeCycles.createLifeCycle();
			request.setLifeCycle(lifeCycle);
			request.setAddressProvider(new HttpAddressProvider(e.getChannel(),
					httpRequest));

			/* tags */
			PanteonTags tags = PanteonTags.create();
			tags.put(HttpNames.HTTP_REQUEST, httpRequest);
			tags.put(HttpNames.HTTP_RESPONSE, httpResponse);

			request.setTags(tags);

			Channels.fireMessageReceived(ctx, request, e.getRemoteAddress());
			return;
		}
		super.messageReceived(ctx, e);
		return;
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (e.getMessage() instanceof PanteonResponse) {
			final PanteonResponse response = (PanteonResponse) e.getMessage();

			final PanteonHttpResponse httpResponse = (PanteonHttpResponse) response
					.getTags().get(HttpNames.HTTP_RESPONSE);

			/* set properties */
			httpResponse.setContentType(response.getContentType());
			httpResponse.setCharset(response.getCharset());
			httpResponse.setStatus(response.getStatus());
			httpResponse.setProtocolVersion(HttpVersion.HTTP_1_1);
			httpResponse.setResultBuffer(response.getContents());
			
			/* set cookies */
			if (response.getCookies().size() > 0) {
				CookieEncoder encoder = new CookieEncoder(true);
				for (Cookie cookie : response.getCookies()) {
					encoder.addCookie(cookie);
				}
				httpResponse.addHeader(HttpHeaders.Names.SET_COOKIE,
						encoder.encode());
			}
			
			/* set headers */
			for (Entry<String, String> entry : response.getHeaders().entrySet()) {
				HttpHeaders.setHeader(httpResponse, entry.getKey(),
						entry.getValue());
			}

			lifeCycles.complete(response);

			Channels.write(ctx, e.getFuture(), httpResponse,
					e.getRemoteAddress());
			return;
		}
		super.writeRequested(ctx, e);
		return;
	}
}
