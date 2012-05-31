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

import java.util.concurrent.atomic.AtomicLong;


import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.eincs.athens.core.TransferClients;
import com.eincs.athens.message.AthensRequest;
import com.eincs.athens.message.TargetKey;
import com.eincs.pantheon.message.PanteonRequest;

/**
 * @author Jung-Haeng Lee
 */
public class AthensTransferHandler extends SimpleChannelHandler {

	private static AtomicLong analzyeReqSeq = new AtomicLong();
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof PanteonRequest) {
			final PanteonRequest request = (PanteonRequest) e
					.getMessage();
			final AthensRequest analyzeRequest = AthensRequest.create(
					analzyeReqSeq.getAndIncrement(),
					TargetKey.createKeyByAddress(request), request);
			
			TransferClients.transfer(analyzeRequest);
		}
		super.messageReceived(ctx, e);
	}
}
