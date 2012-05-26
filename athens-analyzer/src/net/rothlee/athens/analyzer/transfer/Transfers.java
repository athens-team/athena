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
package net.rothlee.athens.analyzer.transfer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.rothlee.athens.analyzer.message.AnalyzeRequest;

import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jung-Haeng Lee
 */
public class Transfers {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Transfers.class);

	private static final Transfers instance = new Transfers();
	private static Executor executor = Executors.newFixedThreadPool(10);
	private static TransferClient client = new TransferClient("localhost", 8081);
	
	public static Transfers getInstnace() {
		return instance;
	}

	public static void transfer(final AnalyzeRequest analyzeRequest) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Channels.write(client.getChannel(), analyzeRequest);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}
}
