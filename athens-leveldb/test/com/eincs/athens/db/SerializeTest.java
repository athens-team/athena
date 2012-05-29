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
package com.eincs.athens.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.eincs.athens.db.data.Statistics;

/**
 * @author Jung-Haeng Lee
 */
public class SerializeTest {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		List<Integer> list = Lists.newArrayList();
		list.add(101);
		list.add(102);
		list.add(103);
		list.add(104);

		Statistics data = new Statistics();
		data.setCountList(list);
		data.setTimestamp(System.currentTimeMillis());

		// write object to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(data);
		byte[] bytes = baos.toByteArray();

		System.out.println(new String(data.toBytes()));
		System.out.println(new String(bytes));

		// read object from byte array
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Statistics obj = (Statistics) ois.readObject();
		System.out.println(obj.getCountList());
		System.out.println(obj.getTimestamp());

	}
}
