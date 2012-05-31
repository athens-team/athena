package com.eincs.athens.message;

import java.io.Serializable;

/**
 * @author Jung-Haeng Lee
 */
public class AnalyzeResult implements Serializable {

	private static final long serialVersionUID = 1420934476726186043L;

	public static AnalyzeResult create(AnalyzeResultType type) {
		return create(type, 0);
	}
	
	public static AnalyzeResult create(AnalyzeResultType type, int panalty) {
		AnalyzeResult result = new AnalyzeResult();
		result.setPanalty(panalty);
		result.setType(type);
		return result;
	}
	
	private AnalyzeResultType type;
	
	private int panalty;

	public AnalyzeResultType getType() {
		return type;
	}

	public void setType(AnalyzeResultType type) {
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
			if(result.getType() == AnalyzeResultType.PANALTY) {
				addPanalty(result.getPanalty());
			} else if(result.getType() == AnalyzeResultType.RELEASE) {
				setType(AnalyzeResultType.RELEASE);
				setPanalty(0);
			}
		}
		}
		return result;
	}
	
	public boolean needNotify() {
		return type == AnalyzeResultType.RELEASE || panalty > 0;
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