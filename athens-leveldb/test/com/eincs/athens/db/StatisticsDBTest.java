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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.db.data.Statistics;
import com.eincs.athens.db.data.StatisticsKey;
import com.eincs.athens.db.leveldb.AthensDBFactory;
import com.eincs.athens.db.leveldb.LevelDBStatisticsDB;
import com.eincs.athens.db.leveldb.AthensDBFactory.AthensDB;
import com.eincs.athens.db.leveldb.LevelDBBlockDB;

/**
 * @author Jung-Haeng Lee
 */
public class StatisticsDBTest {

	private static Logger logger = LoggerFactory
			.getLogger(StatisticsDBTest.class);

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		AthensDBFactory factory = new AthensDBFactory();
		AthensDB athensDB = factory.open("./database/statistics/statistics.db");
		LevelDBStatisticsDB statisticsDB = new LevelDBStatisticsDB(athensDB);

		// test codes here:
		// blockDB.setBlock(key, block)
		Statistics test = Statistics.creatStatisticsByCount(System.currentTimeMillis(), 100);
		StatisticsKey key = StatisticsKey.createKeyByUserId("IDisNow", "IdontCare");
		test.addCount(System.currentTimeMillis()-5000, 100-10);
		test.addCount(System.currentTimeMillis()-10000, 100-20);
		test.addCount(System.currentTimeMillis()-15000, 100-30);
		test.addCount(System.currentTimeMillis()+5000, 100+10);
		test.addCount(System.currentTimeMillis()+10000, 100+20);
		test.addCount(System.currentTimeMillis()+15000, 100+30);
		statisticsDB.addStatistics(key, test);
		test = statisticsDB.getStatistics(key);
		System.out.println(test.getCountList());
		System.out.println(test.getTimestamp());
		System.out.println(test.getSumOfCount());
		statisticsDB.removeStatistics(key);
		//statisticsDB.clear();
	}
}
