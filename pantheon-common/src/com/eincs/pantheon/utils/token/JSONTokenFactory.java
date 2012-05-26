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
package com.eincs.pantheon.utils.token;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author roth2520@gmail.com
 */
public class JSONTokenFactory implements TokenFactory<JSONToken> {

	@Override
	public JSONToken fromBytes(byte[] bytes) throws TokenException {
		
		JSONToken token = new JSONToken();
		try {
			JSONObject tokenObj = new JSONObject(ChannelBuffers.copiedBuffer(
					bytes).toString(CharsetUtil.UTF_8));

			if (tokenObj.has("expiry")) {
				token.setExpiry(tokenObj.getLong("expiry"));
			}

			if (tokenObj.has("timestamp")) {
				token.setTimestamp(tokenObj.getLong("timestamp"));
			}

			if (tokenObj.has("content")) {
				token.setContent(tokenObj.getJSONObject("content"));
			}

			if (tokenObj.has("signature")) {
				ChannelBuffer base64 = Base64.decode(
						ChannelBuffers.copiedBuffer(
								tokenObj.getString("signature"),
								CharsetUtil.UTF_8));
				byte[] signature = new byte[base64.readableBytes()];
				base64.readBytes(signature, 0, base64.readableBytes());
				token.setSignature(signature);
			}

		} catch (JSONException e) {
			throw new TokenException(e);
		}
		return token;
	}

	@Override
	public byte[] toBytes(JSONToken token) throws TokenException {
		JSONObject tokenObj = new JSONObject();
		try {

			if (token.getExpiry() > 0) {
				tokenObj.put("expiry", token.getExpiry());
			}

			if (token.getTimestamp() > 0) {
				tokenObj.put("timestamp", token.getTimestamp());
			}

			if (token.getContent() != null) {
				tokenObj.put("content", token.getContent());
			}

			if (token.getSignature() != null) {
				tokenObj.put(
						"signature",
						Base64.encode(
								ChannelBuffers.copiedBuffer(token
										.getSignature())).toString(
								CharsetUtil.UTF_8));
			}
			return tokenObj.toString().getBytes();
		} catch (JSONException e) {
			throw new TokenException(e);
		}
	}
}
