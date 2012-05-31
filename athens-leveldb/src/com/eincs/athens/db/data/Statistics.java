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
import java.util.Iterator;
import java.util.List;

import com.beust.jcommander.internal.Lists;

/**
 * @author Junseong So
 */
public class Statistics implements Serializable {

	private static final long serialVersionUID = 7143689667989007723L;

	private static final long DEFAULT_TOTAL_LENGTH = 60000;

	private static final long DEFAULT_SLICE_LENGTH = 10000;

	public static Statistics fromBytes(byte[] result) throws IOException,
			ClassNotFoundException {
		if (result == null) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Statistics obj = (Statistics) ois.readObject();
		return obj;
	}

	private long totalLength = DEFAULT_TOTAL_LENGTH;

	private long sliceLength = DEFAULT_SLICE_LENGTH;

	private long timestamp;

	private List<Integer> countList;

	public Statistics() {
		this(DEFAULT_TOTAL_LENGTH, DEFAULT_SLICE_LENGTH);
	}

	public Statistics(long totalLength, long sliceLength) {
		this.totalLength = totalLength;
		this.sliceLength = sliceLength;
		this.timestamp = System.currentTimeMillis() + sliceLength;
		countList = Lists.newArrayList();
		while ((totalLength -= sliceLength) >= 0) {
			countList.add(0);
		}
	}

	public static Statistics creatStatisticsByCount(long timestamp, int count) {
		Statistics result = new Statistics();
		int index = result.countList.size() - 1;
		result.countList.set(index, result.countList.get(index) + count);
		result.timestamp = timestamp + result.sliceLength-1;
		return result;
	}

	public long getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}

	public long getSliceLength() {
		return sliceLength;
	}

	public void setSliceLength(long sliceLength) {
		this.sliceLength = sliceLength;
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
		if (timestamp == this.timestamp) {
			int index = (int) (countList.size() - 1);
			countList.set(index, countList.get(index) + count);
			return;
		}
		if (timestamp < this.timestamp) {
			if (timestamp + totalLength <= this.timestamp) {
				return;
			} else {
				int index = (int) (countList.size() - 1 - ((this.timestamp - timestamp) / sliceLength));
				countList.set(index, countList.get(index) + count);
				return;
			}
		}
		if (timestamp > totalLength - sliceLength + this.timestamp) {
			countList.clear();
			long temp = totalLength;
			while ((temp -= sliceLength) > 0) {
				countList.add(0);
			}
			this.timestamp = timestamp + sliceLength-1;
			countList.add(count);
			return;
		}
		int index = (int) (timestamp - this.timestamp);
		for (int i = 0; i <= totalLength - sliceLength; i += sliceLength) {
			if (index <= sliceLength) {
				while (index > sliceLength) {
					countList.remove(0);
					countList.add(0);
					index -= sliceLength;
				}
				countList.remove(0);
				countList.add(count);
				this.timestamp = timestamp + sliceLength-1;
				return;
			}
		}
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
