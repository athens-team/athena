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
import com.eincs.athens.message.AnalyzeResultType;
import com.eincs.athens.message.AthensReport;
import com.eincs.athens.message.AthensRequest;
import com.google.common.collect.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class Analyzers {

	private static final Logger logger = LoggerFactory
			.getLogger(Analyzers.class);
	
	private static final Analyzers instance = new Analyzers();

	public static Analyzers getInstance() {
		return instance;
	}

	private List<AnalyzerHodler> analyzerHolders = Lists.newArrayList();

	public void addAnalyzer(Analyzer analzyer) {
		AnalyzerHodler holder = new AnalyzerHodler(analzyer);
		analyzerHolders.add(holder);
	}

	/**
	 * travel all analyzers and invoke analyze method
	 * @param request request information of analyze
	 * @return report of analyze
	 */
	public AthensReport invokeAnalyzers(AthensRequest request) {
		// invoke analyzers
		AnalyzeResult result = AnalyzeResult.create(AnalyzeResultType.PANALTY);
		for(AnalyzerHodler holder : analyzerHolders) {
			try {
				AnalyzeResult newResult = holder.analyzer.analyze(request);
				result.merge(newResult);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
		// create report with result and request
		AthensReport report = new AthensReport();
		report.setRequestSeq(request.getRequestSeq());
		report.setTargetKey(request.getTargetKey());
		report.setResult(result);
		report.setTags(request.getTags());
		return report;
	}
	
	/**
	 * @author Jung-Haeng Lee
	 */
	private class AnalyzerHodler {
		final Analyzer analyzer;

		AnalyzerHodler(Analyzer analyzer) {
			this.analyzer = analyzer;
		}
	}
}
