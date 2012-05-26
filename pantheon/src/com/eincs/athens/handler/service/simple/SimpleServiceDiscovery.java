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
package com.eincs.athens.handler.service.simple;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.eincs.athens.message.AthensRequest;
import com.eincs.athens.message.DefaultAthensResponse;

/**
 * @author roth2520@gmail.com
 */
public class SimpleServiceDiscovery extends SimpleChannelHandler {

	private final SimpleServices services;
	public SimpleServiceDiscovery(SimpleServices services) {
		this.services = services;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		
		if(e.getMessage() instanceof AthensRequest) {
			AthensRequest request = (AthensRequest) e.getMessage();
			if(services.match(request)) {
				SimpleService service = services.getService(request);
				request.getTags().put(SimpleServiceNames.SERVICE, service);
				super.messageReceived(ctx, e);		
			} else {
				DefaultAthensResponse response = new DefaultAthensResponse(request);
				response.setStatus(HttpResponseStatus.NOT_FOUND);
				Channels.write(ctx.getChannel(), response);
			}
			return;
		}
		super.messageReceived(ctx, e);
	}
}
