package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public class FloatComparisonNode extends Node {

	private float split_value;
	
	public FloatComparisonNode(int colIdx, SPLIT_DIRECTION splitDir, float splitVal) {
		super(colIdx, splitDir);
		this.split_value = splitVal;
	}

	public float getSplit_value() {
		return split_value;
	}

	public void setSplit_value(float split_value) {
		this.split_value = split_value;
	}
}
