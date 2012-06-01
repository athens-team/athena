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

import com.eincs.athens.message.AnalyzeResult;

/**
 * @author Jung-Haeng Lee
 */
public class Block implements Serializable {

	private static final long serialVersionUID = -992933701670008684L;
	
	public static Block create(AnalyzeResult result) {
		long current = System.currentTimeMillis();
		Block block = new Block();
		block.setBlock(true);
		block.setCreatedTime(current);
		block.setExpiry(current+result.getPanalty());
		return block;
	}
	
	private boolean block;
	
	private long expiry;
	
	private long createdTime;

	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	public long getExpiry() {
		return expiry;
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}
	
	public static Block fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		Block block = null;
		
		if(null != bytes) {
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			ObjectInputStream ois = new ObjectInputStream (bis);
			block = (Block) ois.readObject();
		}
		    
		return block;
	}
}
