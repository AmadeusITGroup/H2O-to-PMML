package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public class Leaf extends AbstractNode {

	private float value;
	
	public Leaf(float val) {
		this.value = val;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
