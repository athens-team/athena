package net.rothlee.athens.analyzer;

import net.rothlee.athens.analyzer.message.AnalyzeRqeuest;

/**
 * @author Jung-Haeng Lee
 */
public interface Analyzer {

	public void analyze(AnalyzeRqeuest request);
}