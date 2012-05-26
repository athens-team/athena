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
package com.eincs.athens.analyzer.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.analyzer.handler.AnalyzeTransferReportHandler;

/**
 * @author Jung-Haeng Lee
 */
public class TransferClient {

	private static final Logger logger = LoggerFactory
			.getLogger(TransferClient.class);
	
	private Executor executor = Executors.newFixedThreadPool(16);
	private final String host;
	private final int port;
	private ClientBootstrap bootstrap;
	private Channel channel;

	public TransferClient(String host, int port) {
		this.host = host;
		this.port = port;
		init();
	}

	private void init() {
		// Configure the client.
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						new ExecutionHandler(executor),
						new AnalyzeTransferReportHandler(),
						new ConnectionHandler());
			}
		});
	}

	public Channel getChannel() {
		if (channel != null) {
			return channel;
		} else {
			synchronized (this) {
				// double check for multithreading
				if (channel != null) {
					return channel;
				} else {
					ChannelFuture future = bootstrap
							.connect(new InetSocketAddress(host, port));
					// wait for connect finish
					future.getChannel();
				}
				return channel;
			}
		}
	}

	private class ConnectionHandler extends SimpleChannelHandler {

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelConnected(ctx, e);
			channel = e.getChannel();
		}
		
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			super.channelClosed(ctx, e);
			channel = null;
		}
		
	}
}
