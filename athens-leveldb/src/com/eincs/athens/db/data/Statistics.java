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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.beust.jcommander.internal.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class Statistics implements Serializable {

	private static final long serialVersionUID = 7143689667989007723L;
	
	private long timestamp;
	
	private List<Integer> countList = Lists.newArrayList();
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<Integer> getCountList() {
		return countList;
	}

	public void setCountList(List<Integer> countList) {
		this.countList = countList;
	}

	public void addCount(long timestamp, int count) {
		// timestamp System.currentTimeMillis();
		// timestamp 10초 단위로 잘 쪼개야되고
		// Statistics는 무조건 전체 1분치를 저장함
	}
	
	public int getSumOfCount() {
		// countList에 있는거 전체 다 더한거
		// (1분동안 보낸거 합친거)
		return 0;
	}
	
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}
}
