package com.eincs.athens.analyzer.message;

import java.io.Serializable;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeResult implements Serializable {

	private static final long serialVersionUID = 1420934476726186043L;

	public static AnalyzeResult create(ResultType type) {
		return create(type, 0);
	}
	
	public static AnalyzeResult create(ResultType type, int panalty) {
		AnalyzeResult result = new AnalyzeResult();
		result.setPanalty(panalty);
		result.setType(type);
		return result;
	}
	
	private ResultType type;
	
	private int panalty;

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public int getPanalty() {
		return panalty;
	}

	public void setPanalty(int panalty) {
		this.panalty = panalty;
	}
	
	public void addPanalty(int panalty) {
		this.panalty += panalty;
	}
	
	public AnalyzeResult merge(AnalyzeResult result) {
		switch (getType()) {
		// add panalty only when current type is PANALTY
		// otherwise, don't merge
		case PANALTY: {
			if(result.getType() == ResultType.PANALTY) {
				addPanalty(result.getPanalty());
			} else if(result.getType() == ResultType.RELEASE) {
				setType(ResultType.RELEASE);
				setPanalty(0);
			}
		}
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("seq:").append(type).append(", ");
		sb.append("panalty:").append(panalty).append(", ");
		sb.append("]");
		return sb.toString();
	}
}