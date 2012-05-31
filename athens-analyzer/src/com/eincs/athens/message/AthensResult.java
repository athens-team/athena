package com.eincs.athens.message;

import java.io.Serializable;

/**
 * @author Jung-Haeng Lee
 */
public class AthensResult implements Serializable {

	private static final long serialVersionUID = 1420934476726186043L;

	public static AthensResult create(ResultType type) {
		return create(type, 0);
	}
	
	public static AthensResult create(ResultType type, int panalty) {
		AthensResult result = new AthensResult();
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
	
	public AthensResult merge(AthensResult result) {
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
	
	public boolean needNotify() {
		return type == ResultType.RELEASE || panalty > 0;
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