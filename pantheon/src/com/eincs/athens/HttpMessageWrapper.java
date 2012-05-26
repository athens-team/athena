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
package com.eincs.athens;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * @author roth2520@gmail.com
 */
public class HttpMessageWrapper implements HttpMessage {

	private final HttpMessage delegate;
	
	public HttpMessageWrapper(HttpMessage delegate) {
		this.delegate = delegate;
	}

	@Override
	public void addHeader(String arg0, Object arg1) {
		delegate.addHeader(arg0, arg1);
	}

	@Override
	public void clearHeaders() {
		delegate.clearHeaders();
	}

	@Override
	public boolean containsHeader(String arg0) {
		return delegate.containsHeader(arg0);
	}

	@Override
	public ChannelBuffer getContent() {
		return delegate.getContent();
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getContentLength() {
		return delegate.getContentLength();
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getContentLength(long arg0) {
		return delegate.getContentLength(arg0);
	}

	@Override
	public String getHeader(String arg0) {
		return delegate.getHeader(arg0);
	}

	@Override
	public Set<String> getHeaderNames() {
		return delegate.getHeaderNames();
	}

	@Override
	public List<Entry<String, String>> getHeaders() {
		return delegate.getHeaders();
	}

	@Override
	public List<String> getHeaders(String arg0) {
		return delegate.getHeaders(arg0);
	}

	@Override
	public HttpVersion getProtocolVersion() {
		return delegate.getProtocolVersion();
	}

	@Override
	public boolean isChunked() {
		return delegate.isChunked();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isKeepAlive() {
		return delegate.isKeepAlive();
	}

	@Override
	public void removeHeader(String arg0) {
		delegate.removeHeader(arg0);
	}

	@Override
	public void setChunked(boolean arg0) {
		delegate.setChunked(arg0);
	}

	@Override
	public void setContent(ChannelBuffer arg0) {
		delegate.setContent(arg0);
	}

	@Override
	public void setHeader(String arg0, Object arg1) {
		delegate.setHeader(arg0, arg1);
	}

	@Override
	public void setHeader(String arg0, Iterable<?> arg1) {
		delegate.setHeader(arg0, arg1);
	}

	@Override
	public void setProtocolVersion(HttpVersion arg0) {
		delegate.setProtocolVersion(arg0);	
	}
}
