package com.amadeus.tec.h2o2pmml.binary.h2opojo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.amadeus.tec.h2o2pmml.binary.h2opojo.AbstractNode;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.Leaf;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.NodeFactory;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.NodeType;

public class NodeFactoryTest {
	
	@Test
	public void createH2OTreeLeafNode() {
		// byte(255) + (byte)255 => 65535 
		byte[] binaryTreeArray = {(byte)8, (byte)255, (byte)255, (byte)0, (byte)0, (byte)0, (byte)0};
		AbstractNode result = NodeFactory.createH2OTree(binaryTreeArray, 0);
		Leaf leaf = (Leaf)result;
		assertTrue(leaf.getValue() == 0);
	}
	
	@Test
	public void getRightNodeByteStartIndex() {
		byte[] binaryTreeArray = {(byte)1, (byte)1, (byte)1, (byte)1};
		int leftNodeByteStartIndex = 0;
		NodeType nodeType = new NodeType((byte)13, (byte)19, (byte)0);
		// 100000001 => 255 + 2 (offset) = 259 
		assertTrue(NodeFactory.getRightNodeByteStartIndex(binaryTreeArray, leftNodeByteStartIndex, nodeType) == 259);
		nodeType.setN_offset_bytes(1);
		// 1 => 1 + 1 (offset) = 2 
		assertTrue(NodeFactory.getRightNodeByteStartIndex(binaryTreeArray, leftNodeByteStartIndex, nodeType) == 2);
		nodeType.setN_offset_bytes(3);
		// 10000000100000001 => 65793 + 3 (offset) = 65796 
		assertTrue(NodeFactory.getRightNodeByteStartIndex(binaryTreeArray, leftNodeByteStartIndex, nodeType) == 65796);
		nodeType.setN_offset_bytes(4);
		// 1000000010000000100000001 => 16843009 + 4 (offset) = 16843013 
		assertTrue(NodeFactory.getRightNodeByteStartIndex(binaryTreeArray, leftNodeByteStartIndex, nodeType) == 16843013);
	}

}
