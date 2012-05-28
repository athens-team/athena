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
package com.eincs.pantheon.utils.client;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * @author Jung-Haeng Lee
 */
public class NettyClient {

	private static final Logger logger = LoggerFactory
			.getLogger(NettyClient.class);

	private InetSocketAddress address;
	private ClientBootstrap clientBootstrap;
	private Channel channel;
	
	private NettyClient() {
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	private void setClientBootstrap(ClientBootstrap clientBootstrap) {
		this.clientBootstrap = clientBootstrap;
	}

	public Channel getChannelNow() {
		return channel;
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
					ChannelFuture future = clientBootstrap.connect(address);
					// wait for connect finish
					future.getChannel();
				}
				return channel;
			}
		}
	}

	/**
	 * {@link NettyClientHandler NettyClientHandler} for {@link NettyClient
	 * NettyClient}<br>
	 * {@link ChannelPipeline ChannelPipeline} for {@link NettyClient
	 * NettyClient} must contain {@link NettyClientHandler NettyClientHandler}
	 * for connection state management
	 * 
	 * @author Jung-Haeng Lee
	 */
	public static class NettyClientHandler extends SimpleChannelHandler {

		private final NettyClient client;

		private NettyClientHandler(NettyClient client) {
			this.client = client;
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelConnected(ctx, e);
			client.channel = e.getChannel();
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			super.channelClosed(ctx, e);
			client.channel = null;
		}

	}

	/**
	 * {@link Builder Builder} for {@link NettyClient NettyClient}
	 * 
	 * @author Jung-Haeng Lee
	 */
	public static class Builder {

		private ClientBootstrap clientBootstrap;
		private InetSocketAddress address;

		public Builder setClientBootstrap(ClientBootstrap clientBootstrap) {
			ensureClientBootstrap(clientBootstrap);
			this.clientBootstrap = clientBootstrap;
			return this;
		}

		private void ensureClientBootstrap(ClientBootstrap clientBootstrap) {
			Preconditions.checkNotNull(clientBootstrap,
					"ClientBootstrap is required.");

			// ChannelPipeline for NettyClient must contain NettyClientHandler
			// for connection state management
			NettyClientHandler clientHandler = clientBootstrap.getPipeline()
					.get(NettyClientHandler.class);

			Preconditions.checkNotNull(clientHandler,
					"ClientBootstrap should contain NettyClientHandler.");
		}

		public Builder setAddress(InetSocketAddress address) {
			ensureAddress(address);
			this.address = address;
			return this;
		}

		private void ensureAddress(InetSocketAddress address) {
			Preconditions.checkNotNull(address, "Address is required.");
		}

		public NettyClient build() {
			ensureClientBootstrap(clientBootstrap);
			ensureAddress(address);

			NettyClient client = new NettyClient();
			client.setAddress(address);
			client.setClientBootstrap(clientBootstrap);
			return client;
		}
	}
}
