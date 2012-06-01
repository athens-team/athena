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

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.athens.conf.AnalyzersConf;
import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.PanteonContentType;
import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.PanteonResponse;

/**
 * @author Jung-Haeng Lee
 */
@Bind(path = "/uploadConfig", method = { "GET" })
public class UploadConfigurationService implements SimpleService {

	private final AnalyzersConf analyzersConf;
	
	private UploadConfigurationService(AnalyzersConf analyzersConf) {
		this.analyzersConf = analyzersConf;
	}
	
	@Override
	public void doServe(PanteonRequest request, PanteonResponse response)
			throws Exception {
		
		String result = analyzersConf.getXML();
		response.setContentType(PanteonContentType.TEXT_PLAIN);
		response.setContents(ChannelBuffers.copiedBuffer(result,
				CharsetUtil.UTF_8));
	}
}
