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

import org.apache.hadoop.util.bloom.Key;

import com.eincs.athens.message.AnalyzeTargetKey;
import com.google.common.primitives.Bytes;

/**
 * @author Jung-Haeng Lee
 */
public class BlockKey implements Serializable {

	private static final long serialVersionUID = -3383825083055710667L;

	public static BlockKey createKey(AnalyzeTargetKey targetKey) {
		BlockKey key = new BlockKey();
		key.setAddress(targetKey.getAddress());
		key.setUserId(targetKey.getUserId());
		key.setPath(targetKey.getPath());
		key.setMethod(targetKey.getMethod());
		return key;
	}
	
	public static BlockKey createKeyByAddress(byte[] address, String method,
			String path) {
		BlockKey result = new BlockKey();
		result.setAddress(address);
		result.setPath(path);
		result.setMethod(method);
		return result;
	}

	public static BlockKey createKeyByUserId(byte[] userId, String method,
			String path) {
		BlockKey result = new BlockKey();
		result.setUserId(userId);
		result.setPath(path);
		result.setMethod(method);
		return result;
	}

	public static BlockKey createKeyByBytes(byte[] bytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		try {
			return (BlockKey) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("cant create BlockKey from bytes.", e);
		}
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

	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}

	public BlockKey fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		BlockKey key  = null;
		if(bytes != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			ObjectInputStream ois = new ObjectInputStream (bis);
			key = (BlockKey) ois.readObject();
		}
		return key;
	}
	
	public Key getBloomFilterKey() {
		byte[] useridBytes = userId;;
		byte[] addressBytes = address;
		
		if(addressBytes==null) {
			addressBytes = new byte[0];
		}
		
		if(useridBytes==null) {
			useridBytes = new byte[0];
		}
		
		byte[] value = Bytes.concat(addressBytes, useridBytes,
				method.getBytes(), path.getBytes());
		Key key = new Key(value);
		return key;
	}
}
