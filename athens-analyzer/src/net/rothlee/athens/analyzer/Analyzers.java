package net.rothlee.athens.analyzer;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jung-Haeng Lee
 */
public class Analyzers {

	private static final Analyzers instance = new Analyzers();
	
	public static Analyzers getInstance() {
		return instance;
	}
	
	private List<Analyzer> analyzers = Lists.newArrayList();
	
	
}
