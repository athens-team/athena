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
package com.eincs.pantheon.handler.codec.http;

import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;



import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.HttpResponseWrapper;
import com.eincs.pantheon.message.PanteonContentType;

/**
 * @author roth2520@gmail.com
 */
public class PanteonHttpResponse extends HttpResponseWrapper {

	private final PanteonHttpRequest request;
	private ChannelBuffer resultBuffer;
	private PanteonContentType contentType;
	private Charset charset;
	
	public PanteonHttpResponse(PanteonHttpRequest request) {
		this(request, HttpResponseStatus.OK);
	}
	
	public PanteonHttpResponse(PanteonHttpRequest request, HttpResponseStatus status) {
		super(new DefaultHttpResponse(HTTP_1_1, status));
		this.request = request;
		this.charset = CharsetUtil.UTF_8;
	}

	public PanteonHttpRequest getRequest() {
		return request;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean hasContentType() {
		return contentType!=null;
	}
	
	public void setContentType(PanteonContentType contentType) {
		this.contentType = contentType;
	}
	
	public PanteonContentType getContentType() {
		return contentType;
	}

	public String getContentTypeWithEncoding() {
		String result = null;
		if(charset!=null) {
			result = contentType.getNotationWithEncoding(charset);	
		} else {
			result = contentType.getNotation();
		}
		return result;
	}
	
	public boolean hasResultBytes() {
		return resultBuffer != null && resultBuffer.readableBytes() > 0;
	}

	public ChannelBuffer getResultBuffer() {
		return resultBuffer;
	}
	
	public void setResultBuffer(ChannelBuffer result) {
		resultBuffer = result;
	}
}
