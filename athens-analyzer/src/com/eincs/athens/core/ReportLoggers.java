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
package com.eincs.athens.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.message.AnalyzeResult;
import com.google.common.collect.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class ReportLoggers {

	private static final Logger logger = LoggerFactory
			.getLogger(ReportLoggers.class);
	
	private static final ReportLoggers instance = new ReportLoggers();

	public static ReportLoggers getInstance() {
		return instance;
	}

	private List<ReportLogger> reportLoggers = Lists.newArrayList();

	public void addAllReportLoggers(List<ReportLogger> reportLoggers) {
		synchronized (this) {
			reportLoggers.addAll(reportLoggers);
		}
	}
	
	public void addReportLogger(ReportLogger reportLogger) {
		synchronized (this) {
			reportLoggers.add(reportLogger);
		}
	}

	public void clearReportLoggers() {
		synchronized (this) {
			reportLoggers.clear();
		}
	}
	
	/**
	 * travel all analyzers and invoke analyze method
	 * @param request request information of analyze
	 * @return report of analyze
	 */
	public void invokeReportLoggers(AnalyzeResult result) {
		// invoke analyzers
		synchronized (this) {
			for(ReportLogger reportLogger: reportLoggers) {
				try {
					reportLogger.handleResult(result);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				
			}
		}
	}
}