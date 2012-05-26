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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

import net.rothlee.athens.analyzer.core.TransferClients;
import net.rothlee.athens.analyzer.message.AnalyzeRequest;
import net.rothlee.athens.analyzer.message.AnalyzeTags;
import net.rothlee.athens.message.attach.AthensCookies;
import net.rothlee.athens.message.attach.AthensParams;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeClientTest {

	public static void main(String[] args) throws UnknownHostException {
		
		AtomicLong seqNum = new AtomicLong();
		
		while (true) {
			AnalyzeRequest request = new AnalyzeRequest();
			request.setRequestSeq(seqNum.getAndIncrement());
			request.setCookies(AthensCookies.create());
			request.setParams(AthensParams.create());
			request.setTags(AnalyzeTags.create());
			request.setOriginAddress(InetAddress.getByName("localhost"));
			request.setRemoteAddress(new InetSocketAddress("localhost", 28080));
			request.setLocalAddress(new InetSocketAddress("localhost", 38080));
			request.setPath("/path");
			request.setMethod("GET");

			TransferClients.transfer(request);
		}
	}
	
}
