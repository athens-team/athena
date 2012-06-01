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
package com.eincs.athens;

import com.eincs.athens.conf.AnalyzersConf;
import com.eincs.athens.db.BlockDB;
import com.eincs.athens.db.StatisticsDB;
import com.eincs.athens.db.leveldb.AthensDBFactory;
import com.eincs.athens.db.leveldb.LevelDBBlockDB;
import com.eincs.athens.db.leveldb.LevelDBStatisticsDB;
import com.google.inject.AbstractModule;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzerModule extends AbstractModule {

	private static final String DB_NAME_STATISTICS = "./database/statistics.db";
	private static final String DB_NAME_BLOCK= "./database/block.db";
	
	private StatisticsDB statisticsDB;
	private BlockDB blockDB;
	
	public AnalyzerModule() {
		try {
			AthensDBFactory factory = new AthensDBFactory();
			
			statisticsDB = new LevelDBStatisticsDB(factory
					.open(DB_NAME_STATISTICS));
			blockDB = new LevelDBBlockDB(factory.open(DB_NAME_BLOCK));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void configure() {
		bind(StatisticsDB.class).toInstance(statisticsDB);
		bind(BlockDB.class).toInstance(blockDB);
		bind(AnalyzersConf.class).toInstance(new AnalyzersConf());
	}

}
