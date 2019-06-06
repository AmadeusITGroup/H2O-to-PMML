package com.amadeus.tec.h2o2pmml.binary.h2opojo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NodeType {

	private int n_offset_bytes;
	
	private OPERATION operation;
	
	private boolean hasLeftChild = true;
	
	private boolean hasRightChild = true;
	
	private int attribute_tested_column_index;

	public NodeType(byte node_type, byte column_index1, byte column_index2) {
		this.n_offset_bytes = (node_type & (byte)3) + 1;
		byte operator = (byte)(node_type & (byte)12);
		if(operator == 0) {
			this.operation = OPERATION.FLOAT_GREATER_OR_EQUAL;
		} else if(operator == 8) {
			this.operation = OPERATION.INCLUSION_SMALL_SET;
		} else if(operator == 12) {
			this.operation = OPERATION.INCLUSION_BIG_SET;
		}
		this.hasLeftChild = (node_type & (byte)48) == 0;
		this.hasRightChild = (node_type & (byte)192) == 0;
		
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(column_index1);
		bb.put(column_index2);
		bb.put((byte)0);
		bb.put((byte)0);
		this.attribute_tested_column_index = bb.getInt(0);
	}
	
	public int getN_offset_bytes() {
		return n_offset_bytes;
	}

	public void setN_offset_bytes(int n_offset_bytes) {
		this.n_offset_bytes = n_offset_bytes;
	}

	public OPERATION getOperation() {
		return operation;
	}

	public void setOperation(OPERATION operation) {
		this.operation = operation;
	}

	public boolean isHasLeftChild() {
		return hasLeftChild;
	}

	public void setHasLeftChild(boolean hasLeftChild) {
		this.hasLeftChild = hasLeftChild;
	}

	public boolean isHasRightChild() {
		return hasRightChild;
	}

	public void setHasRightChild(boolean hasRightChild) {
		this.hasRightChild = hasRightChild;
	}

	public int getAttribute_tested_column_index() {
		return attribute_tested_column_index;
	}

	public void setAttribute_tested_column_index(int attribute_tested_column_index) {
		this.attribute_tested_column_index = attribute_tested_column_index;
	}
	
	public static SPLIT_DIRECTION getSplitDirection(byte splitDirection) {
		if(splitDirection == 1) {
			return SPLIT_DIRECTION.SPLIT_ON_NAN_TEST_NAN_RIGHT;
		} else if(splitDirection == 2 || splitDirection == 4) {
			return SPLIT_DIRECTION.SPLIT_ON_OPERATION_TEST_NAN_LEFT;
		} else {
			return SPLIT_DIRECTION.SPLIT_ON_OPERATION_TEST_NAN_RIGHT;
		}
	}
	
	public static float getFloatFromBytes(byte b, byte c, byte d, byte e) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b);
		bb.put(c);
		bb.put(d);
		bb.put(e);
		return bb.getFloat(0);
	}
	
	public static long getLongFromBytes(byte b, byte c, byte d, byte e) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b);
		bb.put(c);
		bb.put(d);
		bb.put(e);
		bb.put((byte)0);
		bb.put((byte)0);
		bb.put((byte)0);
		bb.put((byte)0);
		return bb.getLong(0);
	}
	
	public static int getIntFromBytes(byte b, byte c, byte d, byte e) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b);
		bb.put(c);
		bb.put(d);
		bb.put(e);
		return bb.getInt(0);
	}

}
