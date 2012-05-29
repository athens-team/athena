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
package com.eincs.athens.db.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * @author Jung-Haeng Lee
 */
public class StatisticsKey implements Serializable {

	private static final long serialVersionUID = 4096509791999065431L;

	public static StatisticsKey createKeyByAddress(InetSocketAddress address,
			String path) {
		StatisticsKey result = new StatisticsKey();
		result.setAddress(address);
		result.setPath(path);
		return result;
	}

	public static StatisticsKey createKeyByUserId(String userId, String path) {
		StatisticsKey result = new StatisticsKey();
		result.setUserId(userId);
		result.setPath(path);
		return result;
	}

	public static StatisticsKey createKeyByBytes(byte[] bytes)
			throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		try {
			return (StatisticsKey) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("cant create BlockKey from bytes.", e);
		}
	}

	private InetSocketAddress address;

	private String userId;

	private String path;

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}
}
