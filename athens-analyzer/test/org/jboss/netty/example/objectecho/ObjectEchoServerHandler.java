/*
 * Copyright 2011 The Netty Project
 * 
 * The Netty Project licenses this file to you under the Apache cense, version
 * 2.0 (the "License"); you may not use this file except compliance with the
 * License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, ftware distributed
 * under the License is distributed on an "AS IS" SIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.ee the License for the
 * specific language governing permissions and mitations under the License.
 */
package org.jboss.netty.example.objectecho;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * Handles both client-side and server-side handler depending on ich constructor
 * was called.
 */
public class ObjectEchoServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger
			.getLogger(ObjectEchoServerHandler.class.getName());

	private final AtomicLong transferredMessages = new AtomicLong();

	public long getTransferredMessages() {
		return transferredMessages.get();
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent
				&& ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Echo back the received object to the client.
		transferredMessages.incrementAndGet();
		e.getChannel().write(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				e.getCause());
		e.getChannel().close();
	}
}