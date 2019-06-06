package com.amadeus.tec.h2o2pmml.binary.h2opojo;

import java.util.Arrays;

/**
 * @author ahockkoon
 * Utility class to instantiate a POJO from a byte Array representing an H2O binary tree.
 * {@link AbstractNode} has currently 2 subclasses:
 * - {@link Node}: node structure with possible left and right children. It has 2 subclasses defining the conditions to go left or right:
 * 		- {@link FloatComparisonNode}: great or equal float comparison operation;
 * 		- {@link SetInclusionNode}: inclusion check operation for categorical feature in a given set.
 * - {@link Leaf}: node structure without child containing the resulting float value.
 *
 */
public class NodeFactory {
	
	public static int H2O_COLUMN_INDEX_LEAF_MARKER = 65535;

	/**
	 * Recursive method creating each node of the tree starting from the root node.
	 * @param binaryTreeArray byte Array containing the set of bits coming from an H2O binary tree.
	 * @param offset
	 * @return
	 */
	public static AbstractNode createH2OTree(byte[] binaryTreeArray, int offset) {
		NodeType nodeType = new NodeType(binaryTreeArray[offset], binaryTreeArray[offset + 1], binaryTreeArray[offset + 2]);
		int leftNodeByteStartIndex = 0, rightNodeByteStartIndex = 0;
		if(nodeType.getAttribute_tested_column_index() == H2O_COLUMN_INDEX_LEAF_MARKER) {
			return new Leaf(NodeType.getFloatFromBytes(binaryTreeArray[offset + 3], binaryTreeArray[offset + 4], binaryTreeArray[offset + 5], binaryTreeArray[offset + 6]));
		} else {
			SPLIT_DIRECTION splitDir = NodeType.getSplitDirection(binaryTreeArray[offset + 3]);
			Node currentNode = null;
			if(splitDir != SPLIT_DIRECTION.SPLIT_ON_NAN_TEST_NAN_RIGHT) {
				if(nodeType.getOperation() == OPERATION.FLOAT_GREATER_OR_EQUAL) {
					currentNode = new FloatComparisonNode(nodeType.getAttribute_tested_column_index(), splitDir, NodeType.getFloatFromBytes(binaryTreeArray[offset + 4], binaryTreeArray[offset + 5], binaryTreeArray[offset + 6], binaryTreeArray[offset + 7]));
					leftNodeByteStartIndex = offset + 8;
				} else if(nodeType.getOperation() == OPERATION.INCLUSION_SMALL_SET) {
					currentNode = new SetInclusionNode(nodeType.getAttribute_tested_column_index(), splitDir, Arrays.copyOfRange(binaryTreeArray, offset + 4, offset + 7));
					leftNodeByteStartIndex = offset + 8;
				} else if(nodeType.getOperation() == OPERATION.INCLUSION_BIG_SET) {
					int bitset_test_offset = NodeType.getIntFromBytes(binaryTreeArray[offset + 4], binaryTreeArray[offset + 5], (byte)0, (byte)0);
					long biteSetLength = NodeType.getLongFromBytes(binaryTreeArray[offset + 6], binaryTreeArray[offset + 7], binaryTreeArray[offset + 8], binaryTreeArray[offset + 9]);
					int byteSetLength = Math.floorDiv((int)biteSetLength + 7, 8);
					// long to int cast could be an issue, since byteSetLength is a uint, which does not exist in Java
					currentNode = new SetInclusionNode(nodeType.getAttribute_tested_column_index(), splitDir, Arrays.copyOfRange(binaryTreeArray, offset + 10, offset + 10 + byteSetLength - 1), bitset_test_offset);
					leftNodeByteStartIndex = offset + 10 + byteSetLength;
				}
			} else {
				leftNodeByteStartIndex = offset + 4;
			}
			if(!nodeType.isHasLeftChild()) {
				currentNode.setLeftNode(new Leaf(NodeType.getFloatFromBytes(binaryTreeArray[leftNodeByteStartIndex], binaryTreeArray[leftNodeByteStartIndex + 1], binaryTreeArray[leftNodeByteStartIndex + 2], binaryTreeArray[leftNodeByteStartIndex + 3])));
				if(nodeType.isHasRightChild()) {
					currentNode.setRightNode(createH2OTree(binaryTreeArray, leftNodeByteStartIndex + 4));
				} else {
					currentNode.setRightNode(new Leaf(NodeType.getFloatFromBytes(binaryTreeArray[leftNodeByteStartIndex + 4], binaryTreeArray[leftNodeByteStartIndex + 5], binaryTreeArray[leftNodeByteStartIndex + 6], binaryTreeArray[leftNodeByteStartIndex + 7])));
				}
			} else {
				rightNodeByteStartIndex = getRightNodeByteStartIndex(binaryTreeArray, leftNodeByteStartIndex, nodeType);
				currentNode.setLeftNode(createH2OTree(binaryTreeArray, leftNodeByteStartIndex + nodeType.getN_offset_bytes()));
				if(nodeType.isHasRightChild()) {
					currentNode.setRightNode(createH2OTree(binaryTreeArray, rightNodeByteStartIndex));
				} else {
					currentNode.setRightNode(new Leaf(NodeType.getFloatFromBytes(binaryTreeArray[rightNodeByteStartIndex], binaryTreeArray[rightNodeByteStartIndex + 1], binaryTreeArray[rightNodeByteStartIndex + 2], binaryTreeArray[rightNodeByteStartIndex + 3])));
				}
			}
			return currentNode;
		}
	}
	
	protected static int getRightNodeByteStartIndex(byte[] binaryTreeArray, int leftNodeByteStartIndex, NodeType nodeType) {
		int n_offset = nodeType.getN_offset_bytes();
		int rightIndex = leftNodeByteStartIndex + n_offset;
		if(n_offset == 4) {
			rightIndex += NodeType.getIntFromBytes(binaryTreeArray[leftNodeByteStartIndex], binaryTreeArray[leftNodeByteStartIndex + 1], binaryTreeArray[leftNodeByteStartIndex + 2], binaryTreeArray[leftNodeByteStartIndex + 3]);
		} else if (n_offset == 3) {
			rightIndex += NodeType.getIntFromBytes(binaryTreeArray[leftNodeByteStartIndex], binaryTreeArray[leftNodeByteStartIndex + 1], binaryTreeArray[leftNodeByteStartIndex + 2], (byte)0);
		} else if (n_offset == 2) {
			rightIndex += NodeType.getIntFromBytes(binaryTreeArray[leftNodeByteStartIndex], binaryTreeArray[leftNodeByteStartIndex + 1], (byte)0, (byte)0);
		} else {
			rightIndex += NodeType.getIntFromBytes(binaryTreeArray[leftNodeByteStartIndex], (byte)0, (byte)0, (byte)0);
		}
		return rightIndex;
	}
}
