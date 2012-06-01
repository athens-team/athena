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
package com.eincs.athens.analytics;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.eincs.athens.HttpPostTest.StringResponseHandler;
import com.google.common.collect.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class ServiceTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://localhost:8082/uploadConfig");

		List<NameValuePair> parameters = Lists.newArrayList();
		;
		parameters
				.add(new BasicNameValuePair(
						"content",
						"<analyzerConf>"
								+ "<analyzers>\n"
								+ "<analyzer>\n"
								+ "<className>com.eincs.athens.analyzer.RateLimitAnalyzer</className>\n"
								+ "<options>\n" + " <entry>\n"
								+ "  <string>Stream</string>\n"
								+ " <int>1</int>\n" + "</entry>\n"
								+ "        <entry>\n"
								+ "         <string>Va2</string>\n"
								+ "        <string>asdf</string>\n"
								+ "     </entry>\n" + "   </options>\n"
								+ "  </analyzer>\n" + " </analyzers>\n"
								+ "</analyzerConf>"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
				"UTF-8");
		httpPost.setEntity(entity);

		String response = httpClient.execute(httpPost,
				new StringResponseHandler());
		System.out.println(response);
	}
}
