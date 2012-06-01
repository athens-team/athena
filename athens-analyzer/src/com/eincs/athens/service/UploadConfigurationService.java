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
package com.eincs.athens.service;

import java.io.File;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.AnalyzerModule;
import com.eincs.athens.DataUtils;
import com.eincs.athens.conf.AnalyzersConf;
import com.eincs.athens.core.Analyzers;
import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.PanteonContentType;
import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.PanteonResponse;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.thoughtworks.xstream.XStreamException;

/**
 * @author Jung-Haeng Lee
 */
@Bind(path = "/uploadConfig", method = { "POST" })
public class UploadConfigurationService implements SimpleService {

	private static final Logger logger = LoggerFactory
			.getLogger(UploadConfigurationService.class);

	private final AnalyzersConf analyzersConf;

	@Inject
	private UploadConfigurationService(AnalyzersConf analyzersConf) {
		this.analyzersConf = analyzersConf;
	}

	@Override
	public void doServe(PanteonRequest request, PanteonResponse response)
			throws Exception {

		final String content = request.getParams().getParam("content",
				String.class);

		try {
			analyzersConf.loadFromString(content);
		} catch (XStreamException e) {
			String result = DataUtils.toResponseString(false);
			response.setContentType(PanteonContentType.TEXT_PLAIN);
			response.setContents(ChannelBuffers.copiedBuffer(result,
					CharsetUtil.UTF_8));
			return;
		}
		analyzersConf.save();
		AnalyzerModule analyzerModule = new AnalyzerModule();
		Module module = Modules.combine(analyzerModule);
		Injector injector = Guice.createInjector(module);
		Analyzers.getInstance().configure(injector);

		String result = DataUtils.toResponseString(true);
		response.setContentType(PanteonContentType.TEXT_PLAIN);
		response.setContents(ChannelBuffers.copiedBuffer(result,
				CharsetUtil.UTF_8));
	}
}
