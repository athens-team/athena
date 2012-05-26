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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.analyzer.message.AnalyzeRequest;

/**
 * @author Jung-Haeng Lee
 */
public class TransferClients {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TransferClients.class);

	private static final TransferClients instance = new TransferClients();
	private static Executor executor = Executors.newFixedThreadPool(10);
	private static TransferClient client = new TransferClient("localhost", 8081);
	
	public static TransferClients getInstnace() {
		return instance;
	}

	public static void transfer(final AnalyzeRequest analyzeRequest) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Channel ch = client.getChannel();
					if (ch != null) {
						Channels.write(ch, analyzeRequest);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}
}
