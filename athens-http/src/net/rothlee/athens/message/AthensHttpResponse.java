/*
 * Copyright 2012 Athens Team
 *
 * This file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.rothlee.athens.message;

import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author roth2520@gmail.com
 */
public class AthensHttpResponse extends HttpResponseWrapper {

	private final AthensHttpRequest request;
	private byte[] resultBytes;

	public AthensHttpResponse(AthensHttpRequest request, HttpResponseStatus httpResponseStatus) {
		this(request, httpResponseStatus, null);
	}
	
	public AthensHttpResponse(AthensHttpRequest request, HttpResponseStatus status, byte[] resultBytes) {
		super(new DefaultHttpResponse(HTTP_1_1, status));
		this.request = request;
		this.resultBytes = (resultBytes!=null)?resultBytes:new byte[0];
	}

	public AthensHttpRequest getRequest() {
		return request;
	}

	public boolean hasResultBytes() {
		return resultBytes != null && resultBytes.length > 0;
	}
	
	public byte[] getResultBytes() {
		return resultBytes;
	}
	
	public void setResultBytes(byte[] bytes) {
		resultBytes = bytes;
	}
	
	public void setResultString(String result) {
		resultBytes = result.getBytes();
	}
}
