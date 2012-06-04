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

import com.eincs.athens.db.data.Statistics;
import com.eincs.athens.db.data.StatisticsKey;
import com.eincs.athens.db.leveldb.AthensDBFactory;
import com.eincs.athens.db.leveldb.AthensDBFactory.AthensDB;
import com.eincs.athens.db.leveldb.LevelDBStatisticsDB;

/**
 * @author Junseong So
 */
public class StatisticsDBTest {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		AthensDBFactory factory = new AthensDBFactory();
		AthensDB athensDB = factory.open("./database/statistics/statistics.db");
		LevelDBStatisticsDB statisticsDB = new LevelDBStatisticsDB(athensDB);

		Statistics statistics = Statistics.creatStatisticsByCount(
				System.currentTimeMillis(), 100);
		StatisticsKey key = StatisticsKey.createKeyByUserId(
				"UserID".getBytes(), "GET", "path");
		statistics.addCount(System.currentTimeMillis()-5000, 100-10);
		statistics.addCount(System.currentTimeMillis()-10000, 100-20);
		statistics.addCount(System.currentTimeMillis()-15000, 100-30);
		statistics.addCount(System.currentTimeMillis()+5000, 100+10);
		statistics.addCount(System.currentTimeMillis()+10000, 100+20);
		statistics.addCount(System.currentTimeMillis()+15000, 100+30);
		System.out.println(statistics);
		statistics.addCount(System.currentTimeMillis(), 100+30);
		statistics.addCount(System.currentTimeMillis(), 100+30);
		System.out.println(statistics);
		statistics.addCount(System.currentTimeMillis()+90000, 100+30);
		statistics.addCount(System.currentTimeMillis()+90000, 100+30);
		statistics.addCount(System.currentTimeMillis()+90000, 100+30);
		statistics.addCount(System.currentTimeMillis()+60000, 100+30);
		System.out.println(statistics);
		
		statisticsDB.putStatistics(key, statistics);
		statistics = statisticsDB.getStatistics(key);
		System.out.println(statistics);
		statisticsDB.removeStatistics(key);
		statistics = statisticsDB.getStatistics(key);
		System.out.println(statistics);
		
		//statisticsDB.clear();
		
		
	}
}
