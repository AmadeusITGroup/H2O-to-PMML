package com.amadeus.tec.h2o2pmml.h2omodelini.pojo;

import java.util.List;

public class H2OAttribute {
	
	private H2OVARIABLE h2ovariable;
	
	private String column_name;
	
	private List<String> domainValues;
	
	H2OAttribute(String colName, H2OVARIABLE variable) {
		this.column_name = colName;
		this.h2ovariable = variable;
	}

	public H2OVARIABLE getH2ovariable() {
		return h2ovariable;
	}

	public void setH2ovariable(H2OVARIABLE h2ovariable) {
		this.h2ovariable = h2ovariable;
	}
	
	public H2OAttribute withH2ovariable(H2OVARIABLE h2ovariable) {
		setH2ovariable(h2ovariable);
		return this;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public List<String> getDomainValues() {
		return domainValues;
	}

	public void setDomainValues(List<String> domainValues) {
		this.domainValues = domainValues;
	}

}
