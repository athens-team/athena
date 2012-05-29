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
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.analyzer.handler.AnalyzeTransferReportHandler;
import com.eincs.pantheon.utils.client.NettyClient;
import com.eincs.pantheon.utils.client.NettyClient.NettyClientHandler;

/**
 * @author Jung-Haeng Lee
 */
public class TransferClient {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(TransferClient.class);

	private final Executor executor = Executors.newFixedThreadPool(16);
	private final NettyClient client;

	public TransferClient(InetSocketAddress address) {
		client = new NettyClient.Builder().setAddress(address)
				.setClientBootstrap(createBootstrap()).build();
	}

	private ClientBootstrap createBootstrap() {

		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
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
						new NettyClientHandler());
			}
		});
		
		return bootstrap;
	}

	public Channel getChannel() {
		return client.getChannel();
	}

}
