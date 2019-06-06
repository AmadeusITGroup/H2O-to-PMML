package com.amadeus.tec.h2o2pmml.binary.h2opojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.amadeus.tec.h2o2pmml.binary.h2opojo.NodeType;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.OPERATION;

public class NodeTypeTest {
	
	@Test
	public void nodeType() {
		NodeType nodeType = new NodeType((byte)13, (byte)19, (byte)0);
		assertEquals(OPERATION.INCLUSION_BIG_SET, nodeType.getOperation());
		assertTrue(nodeType.isHasLeftChild());
		assertTrue(nodeType.isHasRightChild());
		assertTrue(nodeType.getAttribute_tested_column_index() == 19);
		assertTrue(nodeType.getN_offset_bytes() == 2);
	}
	
	@Test
	public void getIntFromBytes() {
		assertTrue(NodeType.getIntFromBytes((byte)73, (byte)1, (byte)0, (byte)0) == 329);
	}
	
	@Test
	public void getLongFromBytes() {
		assertTrue(NodeType.getLongFromBytes((byte)73, (byte)1, (byte)0, (byte)0) == 329);
	}
	
	@Test
	public void getFloatFromBytes() {
		assertTrue(NodeType.getFloatFromBytes((byte)0, (byte)0, (byte)32, (byte)64) == 2.5);
	}

}
