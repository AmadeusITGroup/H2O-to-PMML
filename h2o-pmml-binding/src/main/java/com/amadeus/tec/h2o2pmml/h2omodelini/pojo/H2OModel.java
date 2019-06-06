package com.amadeus.tec.h2o2pmml.h2omodelini.pojo;

import java.util.List;

public class H2OModel {
	
	private List<H2OAttribute> attribute_columns;

	public List<H2OAttribute> getAttribute_columns() {
		return attribute_columns;
	}

	public void setAttribute_columns(List<H2OAttribute> attribute_columns) {
		this.attribute_columns = attribute_columns;
	}
	
}
