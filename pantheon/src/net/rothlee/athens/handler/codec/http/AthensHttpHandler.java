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
package net.rothlee.athens.handler.codec.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import java.io.IOException;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.DiskAttribute;
import org.jboss.netty.handler.codec.http.DiskFileUpload;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpDataFactory;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author roth2520@gmail.com
 */
public class AthensHttpHandler extends SimpleChannelHandler {

	private static final Logger logger = LoggerFactory.getLogger(AthensHttpHandler.class);
	private static final HttpDataFactory factory = new AthensHttpDataFactory();
	static {
		DiskFileUpload.deleteOnExitTemporaryFile = false;
		DiskFileUpload.baseDirectory = "./temp";
		DiskAttribute.deleteOnExitTemporaryFile = false;
		DiskAttribute.baseDirectory = "./temp";
	}

	private AthensHttpState currentState;
	private AthensHttpRequest currentRequest;
	private HttpRequestBuilder currentBuilder;

	public AthensHttpHandler() {
		this.currentState = READING;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

		Object message = e.getMessage();
		if (message instanceof HttpRequest) {
			/*
			 * 현재 상태에 따라 수신된 HttpRequest를 처리하고, 처리 후 다음 상태로 변경한다.
			 * 
			 * READING: 새로운 HttpRequest를 읽거야 하거나 읽고 있는 상태. 문제가 생기면 Error메세지를
			 * write후 READING 상태를 유지. isChunked=true 상태여서 chunked 데이터를 읽어야 하는
			 * 경우에는 READING_CHUNK로 다음 상태를 변경. 그냥 Thrifht interface를 호출하게 되는 경우에는
			 * 호출후 INVOKED상태가 된다.
			 * 
			 * READING_CHUNK: chunked 데이터를 읽어야 하거나 읽고 있는 상태. 모두 읽은후 chunked 데이터와
			 * 함께 Thrift Interface 호출 후 INVOKED 상태로 변경됨.
			 * 
			 * INVOKED: 모든 데이터를 읽고 Thrift Interface가 호출된 상태. Result가 오면 다시
			 * READING 상태로 바뀐다.
			 */
			AthensHttpState nextState = currentState.messageReceived(ctx, e);
			currentState = nextState;

		} else if (message instanceof HttpChunk) {

			AthensHttpState nextState = currentState.messageReceived(ctx, e);
			currentState = nextState;

		} else {
			super.messageReceived(ctx, e);
		}
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object message = e.getMessage();
		if (message instanceof AthensHttpResponse) {
			AthensHttpState nextState = currentState.writeRequested(ctx, e);
			currentState = nextState;
		} else {
			super.writeRequested(ctx, e);
		}
	}

	private AthensHttpState READING = new AbstractInvokeState() {
	
		@Override
		public AthensHttpState messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
	
			/* Initialization */
			releaseBuilder();
	
			/* create thrift invovation instance */
			final HttpRequest request = (HttpRequest) e.getMessage();
			try {
				currentRequest = new AthensHttpRequest(request);
				currentBuilder = new HttpRequestBuilder(currentRequest);
				currentBuilder.beginDecode(factory);
	
			} catch (ErrorDataDecoderException ex) {
				ex.printStackTrace();
				/*
				 * chunked data decoder problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.BAD_REQUEST));
				releaseBuilder();
				return READING;
			} catch (IncompatibleDataDecoderException ex) {
				ex.printStackTrace();
				/*
				 * chunked data decoder problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.BAD_REQUEST));
				releaseBuilder();
				return READING;
			}
	
			/* check if chunked request */
			if (currentBuilder.isChunked()) {
				return READING_CHUNK;
			}
	
			/* process invocation */
			try {
				currentBuilder.doneDecode();
				sendUpstreamInvoke(ctx, e, currentRequest);
				
			} catch (IOException ex) {
				ex.printStackTrace();
				/*
				 * chunked data decoder problem not last chunk problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.INTERNAL_SERVER_ERROR));
				releaseBuilder();
				return READING;
			} catch (NotEnoughDataDecoderException ex) {
				logger.error(ex.getMessage(), ex);
				/*
				 * chunked data decoder problem not last chunk problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.INTERNAL_SERVER_ERROR));
				releaseBuilder();
				return READING;
			}
			return INVOKED;
		}
	};
	
	private AthensHttpState READING_CHUNK = new AbstractInvokeState() {
	
		@Override
		public AthensHttpState messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
	
			final HttpChunk chunk = (HttpChunk) e.getMessage();
			try {
				currentBuilder.offerDecode(chunk);
			} catch (ErrorDataDecoderException ex) {
				ex.printStackTrace();
				/*
				 * chunked data decoder problem not last chunk problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.BAD_REQUEST));
				Channels.close(e.getChannel());
				releaseBuilder();
				return READING;
			}
	
			/* check if isLast */
			if (!chunk.isLast()) {
				return READING_CHUNK;
			}
			
			/* process invocation */
			try {
				currentBuilder.doneDecode();
				sendUpstreamInvoke(ctx, e, currentRequest);
				
			} catch (IOException ex) {
				ex.printStackTrace();
				/*
				 * chunked data decoder problem not last chunk problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.INTERNAL_SERVER_ERROR));
				releaseBuilder();
				return READING;
			} catch (NotEnoughDataDecoderException ex) {
				logger.error(ex.getMessage(), ex);
				/*
				 * chunked data decoder problem not last chunk problem
				 */
				Channels.write(e.getChannel(), new AthensHttpResponse(currentRequest, HttpResponseStatus.INTERNAL_SERVER_ERROR));
				releaseBuilder();
				return READING;
			}
			return INVOKED;
			
		}
	};
	
	private AthensHttpState INVOKED = new AbstractInvokeState() {
	
		@Override
		public AthensHttpState messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			/*
			 * INVOKED 상태인데 Request가 날아오면 문제가 있는 경우이므로, 연결을 끊어버린다.
			 */
			Channels.close(e.getChannel());
			return READING;
		}
	};

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		releaseBuilder();
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	private void writeResponse(ChannelHandlerContext ctx, MessageEvent e, AthensHttpResponse response) {
		final Channel channel = e.getChannel();
		final ChannelFuture future = e.getFuture();
		final ChannelBuffer resultBytes = response.getResultBuffer();
		
		response.setHeader(HttpHeaders.Names.SERVER, "athens");
		
		/* content type */
		if (response.hasContentType()) {
			response.setHeader(CONTENT_TYPE, response.getContentTypeWithEncoding());
		}
		
		/* result string & keep alive */
		boolean keepAlive = currentRequest.isKeepAlive();
		if (response.isKeepAlive()) {
			response.setHeader("Keep-Alive", "timeout=60, max=120");
			response.setHeader(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
		} else {
			response.setContent(resultBytes);
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
					String.valueOf(resultBytes.readableBytes()));
		}

		/* keep-alive */
		Channels.write(ctx, e.getFuture(), response);
		if (!keepAlive) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			DefaultHttpChunk chunk = new DefaultHttpChunk(ChannelBuffers.copiedBuffer(resultBytes));
			Channels.write(channel, chunk);
			Channels.write(channel, HttpChunk.LAST_CHUNK);
		}
	}

	private void releaseBuilder() {
		if(currentBuilder!=null) {
			currentBuilder.release();
			currentBuilder = null;
		}
	}

	public interface AthensHttpState {
		public AthensHttpState messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception;
		public AthensHttpState writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception;
	}

	public abstract class AbstractInvokeState implements AthensHttpState {
	
		@Override
		public abstract AthensHttpState messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception;
	
		@Override
		public AthensHttpState writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			final AthensHttpResponse response = (AthensHttpResponse) e.getMessage();
			final AthensHttpRequest request = response.getRequest();
			if (request != null && request.equals(currentRequest)) {
				writeResponse(ctx, e, response);
				releaseBuilder();
				return READING;
			}
			return INVOKED;
		}
	
		public void sendUpstreamInvoke(ChannelHandlerContext ctx, MessageEvent e, AthensHttpRequest request) {
			ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), request, e.getRemoteAddress()));
		}
	
	}

}
