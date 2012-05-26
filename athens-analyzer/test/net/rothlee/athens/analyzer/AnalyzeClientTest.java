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
package net.rothlee.athens.analyzer;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import net.rothlee.athens.analyzer.message.AnalyzeRequest;
import net.rothlee.athens.analyzer.message.AnalyzeTags;
import net.rothlee.athens.analyzer.transfer.TransferClient;
import net.rothlee.athens.message.attach.AthensCookies;
import net.rothlee.athens.message.attach.AthensParams;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.Channels;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeClientTest {

	public static void main(String[] args) {
		
		AtomicLong seqNum = new AtomicLong();
		
		TransferClient client = new TransferClient("localhost", 8080);
		ChannelFuture future = client.connect();
		Channel channel = future.getChannel();
		
		AnalyzeRequest request = new AnalyzeRequest();
		request.setRequestSeq(seqNum.getAndIncrement());
		request.setCookies(AthensCookies.create());
		request.setParams(AthensParams.create());
		request.setTags(AnalyzeTags.create());
		request.setOriginAddress(new InetSocketAddress("localhost", 18080));
		request.setRemoteAddress(new InetSocketAddress("localhost", 28080));
		request.setLocalAddress(new InetSocketAddress("localhost", 38080));
		request.setPath("/path");
		request.setMethod("GET");
		
		Channels.write(channel, request);
		
	}
	
}
