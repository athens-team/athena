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
package com.eincs.athens.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.conf.AnalyzersConf;
import com.eincs.athens.core.Analyzer;
import com.eincs.athens.db.StatisticsDB;
import com.eincs.athens.db.data.Statistics;
import com.eincs.athens.db.data.StatisticsKey;
import com.eincs.athens.message.AnalyzeResult;
import com.eincs.athens.message.AnalyzeResultType;
import com.eincs.athens.message.AthensRequest;
import com.google.inject.Inject;

/**
 * @author Jung-Haeng Lee
 */
public class RateLimitAnalyzer implements Analyzer {

	private static final Logger logger = LoggerFactory
			.getLogger(RateLimitAnalyzer.class);
	
	private final AnalyzersConf analyzerConf;
	private final StatisticsDB statisticDB;
	
	@Inject
	public RateLimitAnalyzer(AnalyzersConf analyzerConf,
			StatisticsDB statisticDB) {
		this.analyzerConf = analyzerConf;
		this.statisticDB = statisticDB;
	}
	
	@Override
	public AnalyzeResult analyze(AthensRequest request) throws Exception{
		StatisticsKey statKey = StatisticsKey.create(request.getTargetKey());
		Statistics statistics = statisticDB.getStatistics(statKey);
		if(statistics==null) {
			statistics = new Statistics();
		}
		statistics.addCount(System.currentTimeMillis(), 1);
		statisticDB.putStatistics(statKey, statistics);
		
		logger.info("statistics: {}", statistics);
		
		int requestCnt = statistics.getSumOfCount();
		if(requestCnt > 10) {
			return AnalyzeResult.create(AnalyzeResultType.PANALTY, 60000);	
		}
		return AnalyzeResult.create(AnalyzeResultType.PANALTY, 0);
	}

}
