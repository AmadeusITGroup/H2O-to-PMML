package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public class Node extends AbstractNode {
	
	private int attribute_tested_column_index;

	private SPLIT_DIRECTION split_direction;
	
	private AbstractNode leftNode = null;
	
	private AbstractNode rightNode = null;
	
	public Node(int colIdx, SPLIT_DIRECTION splitDir) {
		this.attribute_tested_column_index = colIdx;
		this.split_direction = splitDir;
	}
	
	public int getAttribute_tested_column_index() {
		return attribute_tested_column_index;
	}

	public void setAttribute_tested_column_index(int attribute_tested_column_index) {
		this.attribute_tested_column_index = attribute_tested_column_index;
	}

	public SPLIT_DIRECTION getSplit_direction() {
		return split_direction;
	}

	public void setSplit_direction(SPLIT_DIRECTION split_direction) {
		this.split_direction = split_direction;
	}

	public AbstractNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(AbstractNode leftNode) {
		this.leftNode = leftNode;
	}

	public AbstractNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(AbstractNode rightNode) {
		this.rightNode = rightNode;
	}

}
