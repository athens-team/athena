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
import java.util.Iterator;
import java.util.List;

import com.beust.jcommander.internal.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class Statistics implements Serializable {

	private static final long serialVersionUID = 7143689667989007723L;

	private long timestamp;

	private List<Integer> countList;

	public Statistics() {
		countList = Lists.newArrayList(6);
		countList.add(0);
		countList.add(0);
		countList.add(0);
		countList.add(0);
		countList.add(0);
		countList.add(0);
		this.timestamp = System.currentTimeMillis() + 10000;
	}

	public static Statistics creatStatisticsByCount(long timestamp, int count) {
		Statistics result = new Statistics();
		result.countList.set(5, count);
		result.timestamp = timestamp + 10000;
		return result;
	}

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
		if (timestamp <= this.timestamp) {
			if (timestamp + 60000 <= this.timestamp) {
				return;
			} else {
				int index = (int) (5 - ((this.timestamp - timestamp) / 10000));
				countList.set(index, countList.get(index) + count);
				return;
			}
		}
		if (timestamp > 50000 + this.timestamp) {
			countList.clear();
			countList.add(0);
			countList.add(0);
			countList.add(0);
			countList.add(0);
			countList.add(0);
			this.timestamp = timestamp;
			countList.add(count);
			return;
		}
		int index = (int) (timestamp - this.timestamp);
		if (index <= 10000) {

		} else if (index <= 20000) {
			countList.remove(0);
			countList.add(0);
		} else if (index <= 30000) {
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
		} else if (index <= 40000) {
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
		} else {
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
			countList.remove(0);
			countList.add(0);
		}
		countList.remove(0);
		this.timestamp = timestamp +10000;
		countList.add(count);
		return;
	}

	public int getSumOfCount() {
		Iterator<Integer> iterator = countList.iterator();
		Integer sum = 0;
		while (iterator.hasNext()) {
			sum += iterator.next();
		}
		return sum;
	}

	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("countList=").append(countList).append(",");
		sb.append("countSum=").append(getSumOfCount()).append(",");
		sb.append("stimestamp=").append(getTimestamp());
		sb.append("]");
		return sb.toString();
	}
}
