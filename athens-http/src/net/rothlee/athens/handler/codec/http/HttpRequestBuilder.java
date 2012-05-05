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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jboss.netty.handler.codec.http.Attribute;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpDataFactory;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

/**
 * @author roth2520@gmail.com
 */
public class HttpRequestBuilder {

	private final AthensHttpRequest request;
	private final String path;

	private QueryStringDecoder queryDecoder;
	private HttpPostRequestDecoder postDataDecoder;
	
	public HttpRequestBuilder(AthensHttpRequest request) {
		this.queryDecoder = new QueryStringDecoder(request.getUri());

		this.request = request;
		this.path = queryDecoder.getPath();
	}

	/**
	 * Http요청에 대한 decoding를 시작한다.<br>
	 * <ol>
	 * <li>POST 혹은 DELETE의 경우, HttpPostRequestDecoder를 이용하여 파싱한다. Multipart인 경우
	 * 등을 처리할 수 있으며, chunked요청의 경우에는 offerDecode메서드를 호출하여 추가적으로 데이터를 넣을 수 있다.</li>
	 * <li>GET의 경우에는 Uri에서 QueryString을 파싱한다.</li>
	 * </ol>
	 * 
	 * @param factory
	 *            POST/DELETE 메소드에 대한 요청을 decode할 때 각 Entity에 대한 객체를 만들어내는
	 *            Factory객체
	 * @throws IncompatibleDataDecoderException
	 * @throws ErrorDataDecoderException
	 *             데이터를 decode하다가 데이터가 잘 못되어 문제가 생기는 경우
	 */
	public void beginDecode(HttpDataFactory factory) throws ErrorDataDecoderException, IncompatibleDataDecoderException {

		/* prepare post data */
		final HttpMethod method = request.getMethod();
		if (method == HttpMethod.POST) {
			postDataDecoder = new HttpPostRequestDecoder(factory, request);

		} else if (method == HttpMethod.PUT) {
			postDataDecoder = new HttpPostRequestDecoder(factory, request);

		} else if (method == HttpMethod.DELETE) {
			postDataDecoder = new HttpPostRequestDecoder(factory, request);
		}
	}

	/**
	 * Chunked 요청인 경우에, {@link HttpChunk}를 더 넣어 decoding 한다.
	 * 
	 * @param chunk
	 *            {@link HttpChunk}
	 * @throws ErrorDataDecoderException
	 *             데이터를 decode하다가 데이터가 잘 못되어 문제가 생기는 경우
	 */
	public void offerDecode(HttpChunk chunk) throws ErrorDataDecoderException {
		
		/* parse chunk data */
		if (hasPostDataDecoder()) {
			postDataDecoder.offer(chunk);
		} else {
			throw new IllegalStateException(String.format("method %s can't have chunked data.", request.getMethod().getName()));
		}
	}

	/**
	 * 모든 decoding이 끝난 경우 호출한다.
	 * <ol>
	 * <li>chunked요청인 경우 chunked요청에 대한 모든 데이터를 처리한후 isLast=true일 때 호출되어야 한다.</li>
	 * <li>chunked요청이 아닌 경우, 바로 호출 한다.</li>
	 * </ol>
	 * 
	 * @throws NotEnoughDataDecoderException chunked요청일때 chunked데이터를 모두 처리 하지 않았는데도 불구하고 호출된 경우.
	 * @throws IOException decoding 하다가 에러가 나는 경우.
	 */
	public AthensHttpRequest doneDecode() throws NotEnoughDataDecoderException, IOException {
		
		/* decoded result */
		Map<String, List<String>> params;
		List<InterfaceHttpData> datas;
		
		/*
		 * POST, PUT, DELETE의 경우, Entity-Body로 보내온 Attribute들을 Parameter로 사용한다.
		 * 그 외의 경우, Query Decoder에서 파싱된 Parameter를 사용한다.
		 */
		if(hasPostDataDecoder()) {
			datas = Collections.unmodifiableList(postDataDecoder.getBodyHttpDatas());
			params = getParams(datas);
		} else {
			datas = null;
			params = queryDecoder.getParameters();
		}
		
		/* set decoded result to request */
		request.setPath(path);
		request.setParams(params);
		request.setHttpDatas(datas);
		
		return request;
	}

	public AthensHttpRequest getRequest() {
		return request;
	}

	public boolean isChunked() {
		return request.isChunked();
	}

	public boolean hasPostDataDecoder() {
		return postDataDecoder != null;
	}
	
	public void release() {
		/* release decoder */
		if (hasPostDataDecoder()) {
			postDataDecoder.cleanFiles();
			postDataDecoder = null;
		}
	}
	
	/**
	 * HttpData로 있는 Attribute를  Parameter Map 형태로 바꿔준다.
	 * @param datas 요청받은 InterfaceHttpData
	 * @return Parameter Map
	 * @throws IOException Atribute를 읽다가 에러가 나는 경우
	 */
	private static Map<String, List<String>> getParams(List<InterfaceHttpData> datas) throws IOException {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		for (InterfaceHttpData data : datas) {
			switch (data.getHttpDataType()) {
			case Attribute: {
				final Attribute attr = (Attribute) data;
				final String name = attr.getName();
				final String value = attr.getValue();

				List<String> values = params.get(name);
				if (values == null) {
					values = new ArrayList<String>();
				}
				values.add(value);
				params.put(name, values);
				break;
			}
			}
		}
		return Collections.unmodifiableMap(params);
	}
}