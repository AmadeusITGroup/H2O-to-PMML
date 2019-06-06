package com.amadeus.tec.h2o2pmml.binary.h2opojo;

public class SetInclusionNode extends Node{
	
	private byte[] split_set;
	
	private int bitset_offset = 0;
	
	public SetInclusionNode(int colIdx, SPLIT_DIRECTION splitDir, byte[] splitSet) {
		super(colIdx, splitDir);
		this.split_set = splitSet;
	}
	
	public SetInclusionNode(int colIdx, SPLIT_DIRECTION splitDir, byte[] splitSet, int bitsetOffset) {
		super(colIdx, splitDir);
		this.split_set = splitSet;
		this.bitset_offset = bitsetOffset;
	}

	public byte[] getSplit_set() {
		return split_set;
	}

	public void setSplit_set(byte[] split_set) {
		this.split_set = split_set;
	}

	public int getBitset_offset() {
		return bitset_offset;
	}

	public void setBitset_offset(int bitset_offset) {
		this.bitset_offset = bitset_offset;
	}

}
