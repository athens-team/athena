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
package com.eincs.pantheon.message;

import java.nio.charset.Charset;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.message.attach.PanteonCookies;
import com.eincs.pantheon.message.attach.PanteonHeaders;

/**
 * @author roth2520@gmail.com
 */
public class DefaultPanteonResponse extends DefaultPanteonMessage implements
		PanteonResponse {

	private final PanteonRequest request;

	private HttpResponseStatus status = HttpResponseStatus.OK;
	private ChannelBuffer contents = ChannelBuffers.EMPTY_BUFFER;
	private PanteonContentType contentType = PanteonContentType.TEXT_PLAIN;
	private Charset charset = CharsetUtil.UTF_8;
	
	public DefaultPanteonResponse(PanteonRequest request) {
		this.request = request;
		setTags(request.getTags());
		setLifeCycle(request);
		setHeaders(PanteonHeaders.create());
		setCookies(PanteonCookies.create());
	}

	public PanteonRequest getRequest() {
		return request;
	}

	@Override
	public HttpResponseStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(HttpResponseStatus status) {
		this.status = status;
	}

	@Override
	public ChannelBuffer getContents() {
		return contents;
	}

	@Override
	public void setContents(ChannelBuffer contents) {
		this.contents = contents;
	}

	@Override
	public PanteonContentType getContentType() {
		return contentType;
	}

	@Override
	public void setContentType(PanteonContentType contentType) {
		this.contentType = contentType;
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}