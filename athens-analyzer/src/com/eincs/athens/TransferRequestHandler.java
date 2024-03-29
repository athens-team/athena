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
package com.eincs.athens;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.core.Analyzers;
import com.eincs.athens.message.AthensReport;
import com.eincs.athens.message.AthensRequest;

/**
 * @author Jung-Haeng Lee
 */
public class TransferRequestHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(TransferRequestHandler.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (e.getMessage() instanceof AthensRequest) {
			AthensRequest request = (AthensRequest) e.getMessage();

			logger.info("analyze {}", request.toString());
			AthensReport report = Analyzers.getInstance().invokeAnalyzers(
					request);

			// Notify report only if needed
			// type=RELEASE or panalty > 0 must be notify
			if (report.needNotify()) {
				Channels.write(ctx.getChannel(), report);
			}
			return;
		}
		super.messageReceived(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getChannel().close();
		logger.error(e.getCause().getMessage(), e.getCause());
	}
}