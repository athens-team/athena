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
package com.eincs.athens.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.eincs.pantheon.message.PanteonRequest;

/**
 * @author Jung-Haeng Lee
 */
public class AthensBlockHandler extends SimpleChannelHandler {

	private final AthensBlockFilter blockFilter;
	
	public AthensBlockHandler(AthensBlockFilter blockFilter) {
		this.blockFilter = blockFilter;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof PanteonRequest) {
			PanteonRequest request = (PanteonRequest) e.getMessage();
			if(blockFilter.isBlocked(request)) {
				return;
			}
		}
		super.messageReceived(ctx, e);
	}
}
