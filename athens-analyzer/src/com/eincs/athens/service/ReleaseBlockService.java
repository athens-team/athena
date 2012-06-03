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

import java.net.InetSocketAddress;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.DataUtils;
import com.eincs.athens.core.MysqlHandler;
import com.eincs.athens.core.TransferSessions;
import com.eincs.athens.db.StatisticsDB;
import com.eincs.athens.db.data.StatisticsKey;
import com.eincs.athens.message.AnalyzeResult;
import com.eincs.athens.message.AnalyzeResultType;
import com.eincs.athens.message.AnalyzeTargetKey;
import com.eincs.athens.message.AthensReport;
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
	
	private final StatisticsDB statisticsDB;
	
	@Inject
	private ReleaseBlockService(StatisticsDB statisticsDB) {
		this.statisticsDB = statisticsDB;
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
			byte[] ipAddrBytes = new InetSocketAddress(ipAddr, 0)
					.getAddress().getAddress();
			
			AnalyzeTargetKey targetKey = AnalyzeTargetKey
					.createKeyByAddress(ipAddrBytes, path, method);
			AthensReport report = new AthensReport();
			report.setRequestSeq(0);
			report.setTargetKey(targetKey);
			report.setResult(AnalyzeResult.create(AnalyzeResultType.RELEASE));
			
			StatisticsKey statKey = StatisticsKey.createKeyByAddress(
					ipAddrBytes, method, path);
			statisticsDB.removeStatistics(statKey);
			
			MysqlHandler.remove(ipAddr, method, path);
			TransferSessions.broadcast(report);
		}

		if (userId != null) {
			byte[] userIdBytes = userId.getBytes();
			
			AnalyzeTargetKey targetKey = AnalyzeTargetKey
					.createKeyByAddress(userIdBytes, path, method);
			AthensReport report = new AthensReport();
			report.setRequestSeq(0);
			report.setTargetKey(targetKey);
			report.setResult(AnalyzeResult.create(AnalyzeResultType.RELEASE));

			StatisticsKey statKey = StatisticsKey.createKeyByUserId(
					userIdBytes, method, path);
			statisticsDB.removeStatistics(statKey);
			
			MysqlHandler.remove(userId, method, path);
			TransferSessions.broadcast(report);
		}
		
		final String responseString = DataUtils.toResponseString(true);
		response.setContentType(PanteonContentType.TEXT_PLAIN);
		response.setContents(ChannelBuffers.copiedBuffer(responseString,
				CharsetUtil.UTF_8));
	}
}
