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
package com.eincs.athens;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.analyzer.RateLimitAnalyzer;
import com.eincs.athens.core.Analyzers;
import com.eincs.athens.db.leveldb.AthensDBFactory;
import com.eincs.athens.db.leveldb.AthensDBFactory.AthensDB;
import com.eincs.athens.db.leveldb.LevelDBStatisticsDB;
import com.eincs.athens.message.AthensReport;
import com.eincs.athens.message.AthensRequest;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzerMain {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		AthensDBFactory factory = new AthensDBFactory();
		AthensDB athensDB = factory.open("./database/statistics/statistics.db");
		LevelDBStatisticsDB statisticsDB = new LevelDBStatisticsDB(athensDB);

		// register analzyer
		Analyzers.getInstance()
				.addAnalyzer(new RateLimitAnalyzer(statisticsDB));

		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						new AnalyzeTransferRequestHandler());
			}
		});

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(8081));
	}

	public static class AnalyzeTransferRequestHandler extends
			SimpleChannelUpstreamHandler {

		private static final Logger logger = LoggerFactory
				.getLogger(AnalyzeTransferRequestHandler.class);

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {

			if (e.getMessage() instanceof AthensRequest) {
				AthensRequest request = (AthensRequest) e.getMessage();

				logger.info("analyze {}", request.toString());
				AthensReport report = Analyzers.getInstance().invokeAnalyzers(
						request);

				// Notify report only if needed
				// type=RELEASE or panalty > 0 must be notify
				if (report.needNotify()) {
					Channels.write(ctx.getChannel(), report);
				}
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
}
