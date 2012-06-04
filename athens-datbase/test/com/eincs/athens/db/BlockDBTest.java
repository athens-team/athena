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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

import org.iq80.leveldb.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.db.data.Block;
import com.eincs.athens.db.data.BlockKey;
import com.eincs.athens.db.leveldb.AthensDBFactory;
import com.eincs.athens.db.leveldb.AthensDBFactory.AthensDB;
import com.eincs.athens.db.leveldb.LevelDBBlockDB;

/**
 * @author Jung-Haeng Lee
 */
public class BlockDBTest {

	private static Logger logger = LoggerFactory.getLogger(BlockDBTest.class);

	private static AthensDBFactory factory;
	private static AthensDB athensDB;
	private static LevelDBBlockDB blockDB;
	private static ArrayList<BlockKey> keylist;

	private static boolean semaphore = false;

	private static int test_num = 60;

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {
		keylist = new ArrayList<BlockKey>();
		factory = new AthensDBFactory();
		blockDB = new LevelDBBlockDB(athensDB);

		Thread writeTest = new WriteTest();
		Thread readTest = new ReadTest();
		Thread deleteTest = new DeleteTest();

		athensDB = factory.open("database/block/block.db");
		blockDB = new LevelDBBlockDB(athensDB);

		writeTest.start();
		readTest.start();
		deleteTest.start();
	}

	private static class WriteTest extends Thread {

		private Random gen = new Random();

		public synchronized void run() {
			for (int i = 0; i < test_num; i++) {
				byte[] address = new InetSocketAddress("127.0.0.1", i)
						.getAddress().getAddress();
				BlockKey key = BlockKey.createKeyByAddress(address, "POST",
						"demo/demo.php" + " " + i);

				Block block = new Block();
				block.setBlock(true);
				int num;
				block.setCreatedTime(num = gen.nextInt(100));
				block.setExpiry(num + 10);

				// block을 DB에 집어 넣는다.
				blockDB.setBlock(key, block);
				keylist.add(key);

				try {
					System.out.println(block.getCreatedTime() + " 삽입");
					this.sleep(gen.nextInt(1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private static class ReadTest extends Thread {

		private Random gen = new Random();

		public synchronized void run() {
			try {
				this.sleep(200);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for (int i = 0; i < test_num; i++) {
				InetSocketAddress address = new InetSocketAddress("127.0.0.1",
						i);
				int num = gen.nextInt();
				if (num < 0) num *= -1;

				Block block = null;
				if (!keylist.isEmpty()) {
					BlockKey key = keylist.get(num % keylist.size());

					// block을 DB에 집어 넣는다.
					block = blockDB.getBlock(key);
				}

				try {
					System.out.println(block.getCreatedTime() + " 읽음");
					this.sleep(gen.nextInt(1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static class DeleteTest extends Thread {

		private Random gen = new Random();

		public synchronized void run() {
			try {
				this.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for (int i = 0; i < test_num / 2; i++) {
				InetSocketAddress address = new InetSocketAddress("127.0.0.1",
						i);
				int num = gen.nextInt();
				if (num < 0) num *= -1;

				Block block = null;
				int index = 0;
				long block_value = 0;
				if (!keylist.isEmpty()) {
					BlockKey key = keylist.get(index = (num % keylist.size()));

					block_value = blockDB.getBlock(key).getCreatedTime();

					blockDB.removeBlock(key);
					keylist.remove(index);
				}

				try {
					System.out.println(block_value + " 삭제");
					this.sleep(gen.nextInt(1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}