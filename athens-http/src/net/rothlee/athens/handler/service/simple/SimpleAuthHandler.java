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
package net.rothlee.athens.handler.service.simple;

import net.rothlee.athens.message.AthensRequest;
import net.rothlee.athens.message.DefaultAthensResponse;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author roth2520@gmail.com
 */
public class SimpleAuthHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (e.getMessage() instanceof AthensRequest) {
			AthensRequest request = (AthensRequest) e.getMessage();
			String authorization = request.getHeaders().get(
					HttpHeaders.Names.AUTHORIZATION);
			if (authorization != null) {
				authorization = authorization.split("\\s")[1];
				ChannelBuffer decodedBuffer = Base64.decode(ChannelBuffers
						.copiedBuffer(authorization, CharsetUtil.US_ASCII));
				String str = decodedBuffer.toString(CharsetUtil.US_ASCII);
				String[] authInfo = str.split(":");
				String id = authInfo[0];
				String password = authInfo[1];
				if (id.equals(password)) {
					super.messageReceived(ctx, e);
					return;
				}
			}
			DefaultAthensResponse response = new DefaultAthensResponse(request);
			response.getHeaders().put("WWW-Authenticate", "Basic");
			response.setStatus(HttpResponseStatus.UNAUTHORIZED);
			Channels.write(ctx.getChannel(), response);
			return;
		}
		super.messageReceived(ctx, e);
	}
}
