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

import org.apache.commons.codec.binary.Base64;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.DataUtils;
import com.eincs.athens.db.data.BlockKey;
import com.eincs.athens.handler.AthensBlockFilter;
import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.PanteonContentType;
import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.PanteonResponse;
import com.google.inject.Inject;

/**
 * @author Jung-Haeng Lee
 */
@Bind(path = "/releaseBlock", method = { "POST" })
public class ReleaseBlockService implements SimpleService {

	private static final Logger logger = LoggerFactory
			.getLogger(ReleaseBlockService.class);
	
	private final AthensBlockFilter blockFilter;
	
	@Inject
	private ReleaseBlockService(AthensBlockFilter blockFilter) {
		this.blockFilter = blockFilter;
	}
	
	@Override
	public void doServe(PanteonRequest request, PanteonResponse response)
			throws Exception {
		
		final String ipAddr = request.getParams().getParam("ip_addr",
				String.class);
		final String userId = request.getParams().getParam("user_id",
				String.class);
		final String path = request.getParams().getParam("path",
				String.class);
		final String method = request.getParams().getParam("method",
				String.class);
		
		if (ipAddr != null) {
			byte[] ipAddrBytes = Base64.decodeBase64(ipAddr);
			BlockKey blockKey = BlockKey.createKeyByAddress(ipAddrBytes,
					method, path);
		}

		if (userId != null) {
			byte[] userIdBytes = Base64.decodeBase64(userId);
			BlockKey blockKey = BlockKey.createKeyByAddress(userIdBytes,
					method, path);
		}
		
		final String responseString = DataUtils.toResponseString(true);
		response.setContentType(PanteonContentType.TEXT_PLAIN);
		response.setContents(ChannelBuffers.copiedBuffer(responseString,
				CharsetUtil.UTF_8));
	}
}
