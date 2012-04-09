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
package net.rothlee.athens.test;

import net.rothlee.athens.message.AthensHttpRequest;
import net.rothlee.athens.message.AthensHttpResponse;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author jhlee@vcnc.co.kr
 */
public class SimpleHttpHandler extends SimpleChannelHandler {

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		Object message = e.getMessage();
		if (message instanceof AthensHttpRequest) {
			AthensHttpRequest request = (AthensHttpRequest) message;
			AthensHttpResponse response = new AthensHttpResponse(request, HttpResponseStatus.OK);
			
			StringBuilder sb = new StringBuilder();
			sb.append("method:" + request.getMethod()).append("\n");
			sb.append("cookieString:" + request.getCookieString()).append("\n");
			sb.append("userAgent:" + request.getUserAgent()).append("\n");
			sb.append("path:" + request.getPath()).append("\n");
			sb.append("uri:" + request.getUri()).append("\n");
			
			response.setResultString(sb.toString());
			
			Channels.write(ctx.getChannel(), response);
			return;
		}
		super.messageReceived(ctx, e);
	};
}
