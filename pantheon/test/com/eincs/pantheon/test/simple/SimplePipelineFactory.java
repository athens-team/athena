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
package com.eincs.pantheon.test.simple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.eincs.pantheon.handler.codec.http.AthensHttpHandler;
import com.eincs.pantheon.handler.codec.http.AthensHttpProcessor;
import com.eincs.pantheon.handler.service.simple.SimpleAuthHandler;
import com.eincs.pantheon.handler.service.simple.SimpleServiceDiscovery;
import com.eincs.pantheon.handler.service.simple.SimpleServiceInvoker;
import com.eincs.pantheon.handler.service.simple.SimpleServices;

/**
 * @author jhlee@vcnc.co.kr
 */
public class SimplePipelineFactory implements ChannelPipelineFactory {

	private final int DEFAULT_WORKER_THREAD_COUNT = Runtime.getRuntime()
			.availableProcessors() * 8;

	private final ExecutorService executor;

	public SimplePipelineFactory() {
		this.executor = Executors
				.newFixedThreadPool(DEFAULT_WORKER_THREAD_COUNT);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {

		SimpleServices services = new SimpleServices();
		services.putByAnnotation(new MenuService());
		services.putByAnnotation(new IndexService());
		services.putByAnnotation(new PrintCookieService());
		services.putByAnnotation(new SetCookieService());
		
		return Channels.pipeline(new HttpRequestDecoder(),
				new HttpResponseEncoder(),
				new HttpContentCompressor(),
				new AthensHttpHandler(),
				new ExecutionHandler(executor),
				new AthensHttpProcessor(),
				new SimpleAuthHandler(),
				new SimpleServiceDiscovery(services),
				new SimpleServiceInvoker());
	}

}
