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
package com.eincs.athens.message;

import java.io.Serializable;

import com.eincs.pantheon.AddressProviders;
import com.eincs.pantheon.message.PanteonRequest;
import com.google.common.primitives.Bytes;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeTargetKey implements Serializable {

	private static final long serialVersionUID = -3604231145525755533L;

	public static AnalyzeTargetKey createKeyByAddress(byte[] address,
			String path, String method) {
		AnalyzeTargetKey result = new AnalyzeTargetKey();
		result.setAddress(address);
		result.setPath(path);
		result.setMethod(method);
		return result;
	}

	public static AnalyzeTargetKey createKeyByUser(byte[] userId, String path,
			String method) {
		AnalyzeTargetKey result = new AnalyzeTargetKey();
		result.setUserId(userId);
		result.setPath(path);
		result.setMethod(method);
		return result;
	}

	public static AnalyzeTargetKey createKeyByAddress(PanteonRequest request) {
		AnalyzeTargetKey result = new AnalyzeTargetKey();
		result.setAddress(AddressProviders.getRemoteOrOriginAddress(request)
				.getAddress());
		result.setPath(request.getPath());
		result.setMethod(request.getMethod().toString());
		return result;
	}

	public static AnalyzeTargetKey createKeyByUser(byte[] userId,
			PanteonRequest request) {
		AnalyzeTargetKey result = new AnalyzeTargetKey();
		result.setUserId(userId);
		result.setPath(request.getPath());
		result.setMethod(request.getMethod().toString());
		return result;
	}

	private byte[] address;

	private byte[] userId;

	private String method;

	private String path;

	public byte[] getAddress() {
		return address;
	}

	public void setAddress(byte[] address) {
		this.address = address;
	}

	public byte[] getUserId() {
		return userId;
	}

	public void setUserId(byte[] userId) {
		this.userId = userId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (userId != null) {
			sb.append("userid:").append(Bytes.asList(userId)).append(", ");
		}
		if (address != null) {
			sb.append("address:").append(Bytes.asList(address)).append(", ");
		}
		sb.append("method:").append(method).append(", ");
		sb.append("path:").append(path).append(", ");
		sb.append("]");
		return sb.toString();
	}

}
