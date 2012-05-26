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
package net.rothlee.athens.analyzer.handler;

import net.rothlee.athens.analyzer.core.Analyzers;
import net.rothlee.athens.analyzer.message.AnalyzeReport;
import net.rothlee.athens.analyzer.message.AnalyzeRequest;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeTransferRequestHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AnalyzeTransferRequestHandler.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		if (e.getMessage() instanceof AnalyzeRequest) {
			AnalyzeRequest request = (AnalyzeRequest) e.getMessage();
			
			logger.info("analyze {}", request.toString());
			AnalyzeReport report = Analyzers.getInstance().invokeAnalyzers(
					request);
			
			Channels.write(ctx.getChannel(), report);
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