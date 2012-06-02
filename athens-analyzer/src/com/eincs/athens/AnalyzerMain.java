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

import com.eincs.athens.core.Analyzers;
import com.eincs.athens.message.AthensReport;
import com.eincs.athens.message.AthensRequest;
import com.eincs.athens.service.GetConfigurationService;
import com.eincs.athens.service.ReleaseBlockService;
import com.eincs.athens.service.UploadConfigurationService;
import com.eincs.pantheon.handler.service.simple.SimpleServices;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzerMain {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		AnalyzerModule analyzerModule = new AnalyzerModule();
		Module module = Modules.combine(analyzerModule);
		Injector injector = Guice.createInjector(module);

		configureAnalytics(injector);
		configureAnalyzer(injector);
	}

	private static void configureAnalyzer(Injector injector) {
		// register analzyer
		Analyzers.getInstance().configure(injector);

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
						new TransferRequestHandler());
			}
		});

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(8081));
	}
	
	private static void configureAnalytics(Injector injector) {
		
		SimpleServices services = new SimpleServices();
		services.putByAnnotation(injector
				.getInstance(ReleaseBlockService.class));
		services.putByAnnotation(injector
				.getInstance(UploadConfigurationService.class));
		services.putByAnnotation(injector
				.getInstance(GetConfigurationService.class));

		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(), 
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new AnalyticsPipelineFactory(services));

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(8082));
	}
}
