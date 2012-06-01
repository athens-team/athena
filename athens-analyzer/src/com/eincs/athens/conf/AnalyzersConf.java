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
package com.eincs.athens.conf;

import java.util.List;
import java.util.Map;

import org.testng.collections.Maps;

import com.beust.jcommander.internal.Lists;
import com.eincs.athens.analyzer.RateLimitAnalyzer;
import com.eincs.pantheon.utils.config.AbstractXStreamConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Jung-Haeng Lee
 */
@XStreamAlias("analyzerConf")
public class AnalyzersConf extends AbstractXStreamConfig {

	private static final String CONF_NAME = "./conf/athens-analyzer.conf";
	
	public AnalyzersConf() {
		super(CONF_NAME);
	}

	@XStreamAlias("analyzers")
	private List<Analyzer> analyzerInfos;

	public List<Analyzer> getAnalyzerInfos() {
		return analyzerInfos;
	}

	public void setAnalyzerInfos(List<Analyzer> analyzerInfos) {
		this.analyzerInfos = analyzerInfos;
	}
	
	public static void main(String[] args) {
		List<Analyzer> analyzerInfos = Lists.newArrayList();
		Analyzer info;
		Map<String, Object> options;
		
		options = Maps.newHashMap();
		options.put("Stream", 1);
		options.put("Value2", "asdf");
		
		info = new Analyzer();
		info.setClassName(RateLimitAnalyzer.class);
		info.setOptions(options);
		analyzerInfos.add(info);
		
		options = Maps.newHashMap();
		options.put("Stream", 1);
		options.put("Value2", "asdf");
		
		info = new Analyzer();
		info.setClassName(AnalyzersConf.class);
		info.setOptions(options);
		analyzerInfos.add(info);
		
		AnalyzersConf conf = new AnalyzersConf();
		conf.setAnalyzerInfos(analyzerInfos);
		conf.save();
		
//		AnalyzersConf conf = new AnalyzersConf();
//		conf.load();
//		System.out.println(conf);
	}
}
