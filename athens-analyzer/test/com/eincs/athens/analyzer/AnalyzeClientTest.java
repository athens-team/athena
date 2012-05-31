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
package com.eincs.athens.analyzer;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

import com.eincs.athens.analyzer.core.TransferClients;
import com.eincs.athens.analyzer.message.AnalyzeRequest;
import com.eincs.athens.analyzer.message.AnalyzeTags;
import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonParams;


/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeClientTest {

	public static void main(String[] args) throws UnknownHostException {
		
		AtomicLong seqNum = new AtomicLong();
		
		while (true) {
			AnalyzeRequest request = new AnalyzeRequest();
			request.setRequestSeq(seqNum.getAndIncrement());
			request.setCookies(PanteonCookies.create());
			request.setParams(PanteonParams.create());
			request.setTags(AnalyzeTags.create());
			TransferClients.transfer(request);
		}
	}
	
}
