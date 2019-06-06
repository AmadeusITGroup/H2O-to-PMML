package com.amadeus.tec.h2o2pmml.xml.pmmlpojo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dmg.pmml_4_3.CompoundPredicate;
import org.dmg.pmml_4_3.DATATYPE;
import org.dmg.pmml_4_3.DataDictionary;
import org.dmg.pmml_4_3.DataField;
import org.dmg.pmml_4_3.Header;
import org.dmg.pmml_4_3.MININGFUNCTION;
import org.dmg.pmml_4_3.MiningField;
import org.dmg.pmml_4_3.MiningSchema;
import org.dmg.pmml_4_3.Node;
import org.dmg.pmml_4_3.OPTYPE;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.SimplePredicate;
import org.dmg.pmml_4_3.TreeModel;
import org.dmg.pmml_4_3.True;
import org.dmg.pmml_4_3.Value;

import com.amadeus.tec.h2o2pmml.binary.h2opojo.AbstractNode;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.FloatComparisonNode;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.Leaf;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.SetInclusionNode;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OAttribute;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OModel;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OVARIABLE;

/**
 * @author ahockkoon
 * Utility factory class to create PMML 4.3 POJOs from H2O POJOs.
 */
public class H2O2PMMLFactory {
	
	public static String PMML_VERSION = "4.3";
	
	public static String PMML_COPYRIGHT = "h2o2pmml";
	
	public static String H2O2PMML_DESCRIPTION = "PMML tree generated from H2O binary tree with H2O2PMML maven plugin";
	
	public static String PMML_OPERATOR_EQUAL = "equal";
	
	public static String PMML_OPERATOR_GREATER_OR_EQUAL = "greaterOrEqual";
	
	public static String PMML_BOOLEAN_OPERATOR = "or";
	
	public static String ROOT_NODE_ID = "0";
	
	public static String LEFT_NODE_ID_MARKER = "1";
	
	public static String RIGHT_NODE_ID_MARKER = "2";
	
	/**
	 * Creates a PMML POJO from H2O POJO representing a binary tree.
	 * @param tree H2O POJO representing the binary tree
	 * @param model H2O POJO representing the metadata to interpret the binary tree
	 * @return
	 */
	public static PMML createPMMLfromH2OTree(AbstractNode tree, H2OModel model) {
		return new PMML().withVersion(PMML_VERSION)
				.withHeader(new Header().withCopyright(PMML_COPYRIGHT).withDescription(H2O2PMML_DESCRIPTION))
				.withDataDictionary(createDataDictionary(model))
				.withAssociationModelOrBayesianNetworkModelOrBaselineModel(createTreeModel(tree, model));
	}

	protected static DataDictionary createDataDictionary(H2OModel model) {
		List<H2OAttribute> attributes = model.getAttribute_columns();
		DataDictionary dataDictionary = new DataDictionary().withNumberOfFields(BigInteger.valueOf((attributes.size())));
		Collection<DataField> dfs = new ArrayList<DataField>();
		for(int i = 0; i < attributes.size(); i++) {
			H2OAttribute attribute = attributes.get(i);
			DataField df = new DataField().withName(attribute.getColumn_name());
			if(attribute.getH2ovariable() == H2OVARIABLE.CATEGORICAL_VARIABLE) {
				df.withOptype(OPTYPE.CATEGORICAL).withDataType(DATATYPE.STRING);
				List<String> domainValues = attribute.getDomainValues();
				Collection<Value> values = new ArrayList<Value>();
				for(int j = 0; j < domainValues.size(); j++) {
					values.add(new Value().withValue(domainValues.get(j)));
				}
				df.withValue(values);
			} else {
				df.withOptype(OPTYPE.CONTINUOUS).withDataType(DATATYPE.FLOAT);
			}
			dfs.add(df);
		}
		dataDictionary.withDataField(dfs);
		return dataDictionary;
	}
	
	protected static TreeModel createTreeModel(AbstractNode tree, H2OModel model) {
		TreeModel treeModel = new TreeModel().withModelName(PMML_COPYRIGHT)
				.withFunctionName(MININGFUNCTION.CLASSIFICATION);
		Collection<Object> contents = new ArrayList<Object>();
		contents.add(createMiningSchema(model));
		contents.add(createNodes(tree, model));
		treeModel.withContent(contents);
		return treeModel;
	}
	
	protected static MiningSchema createMiningSchema(H2OModel model) {
		List<H2OAttribute> attributes = model.getAttribute_columns();
		Collection<MiningField> fields = new ArrayList<MiningField>();
		for(int i = 0; i < attributes.size(); i++) {
			fields.add(new MiningField().withName(attributes.get(i).getColumn_name()));
		}
		return new MiningSchema().withMiningField(fields);
	}
	
	protected static Node createNodes(AbstractNode tree, H2OModel model) {
		Node rootNode = new Node().withId(ROOT_NODE_ID);
		Collection<Object> content = new ArrayList<Object>();
		content.add(new True());
		content.addAll(createLeftRightNodes(tree, model, rootNode));
		rootNode.withContent(content);
		return rootNode;
	}
	
	protected static Collection<Object> createLeftRightNodes(AbstractNode tree, H2OModel model, Node parentNode) {
		Collection<Object> result = new ArrayList<Object>();
		
		String leftNodeId = parentNode.getId() + LEFT_NODE_ID_MARKER;
		Node leftNode = new Node().withId(leftNodeId);
		Collection<Object> leftNodeContent = new ArrayList<Object>();
		if(tree instanceof FloatComparisonNode) {
			FloatComparisonNode floatNode = (FloatComparisonNode) tree;
			leftNodeContent.add(new SimplePredicate().withField(getAttributeTestedName(floatNode, model)).withOperator(PMML_OPERATOR_GREATER_OR_EQUAL).withValue(String.valueOf(floatNode.getSplit_value())));
			if(floatNode.getLeftNode() != null) {
				if(floatNode.getLeftNode() instanceof Leaf) {
					leftNode.setScore(String.valueOf(((Leaf)floatNode.getLeftNode()).getValue()));
				} else {
					leftNodeContent.addAll(createLeftRightNodes(floatNode.getLeftNode(), model, leftNode));
				}
			}
		} else if (tree instanceof SetInclusionNode) {
			SetInclusionNode setNode = (SetInclusionNode) tree;
			leftNodeContent.add(new CompoundPredicate().withBooleanOperator(PMML_BOOLEAN_OPERATOR).withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(createSetInclusionSimplePredicate(setNode, model)));
			if(setNode.getLeftNode() != null) {
				if(setNode.getLeftNode() instanceof Leaf) {
					leftNode.setScore(String.valueOf(((Leaf)setNode.getLeftNode()).getValue()));
				} else {
					leftNodeContent.addAll(createLeftRightNodes(setNode.getLeftNode(), model, leftNode));
				}
			}
		}
		leftNode.withContent(leftNodeContent);
		result.add(leftNode);
		
		String rightNodeId = parentNode.getId() + RIGHT_NODE_ID_MARKER;
		Node rightNode = new Node().withId(rightNodeId);
		Collection<Object> rightNodeContent = new ArrayList<Object>();
		rightNodeContent.add(new True());
		if(tree instanceof com.amadeus.tec.h2o2pmml.binary.h2opojo.Node) {
			com.amadeus.tec.h2o2pmml.binary.h2opojo.Node node = (com.amadeus.tec.h2o2pmml.binary.h2opojo.Node) tree;
			if(node.getRightNode() != null) {
				if(node.getRightNode() instanceof Leaf) {
					rightNode.setScore(String.valueOf(((Leaf)node.getRightNode()).getValue()));
				} else {
					rightNodeContent.addAll(createLeftRightNodes(node.getRightNode(), model, rightNode));
				}
			}
			setDefaultChild(node, parentNode, leftNodeId, rightNodeId);
		}
		rightNode.withContent(rightNodeContent);
		result.add(rightNode);
		
		return result;
	}
	
	protected static void setDefaultChild(com.amadeus.tec.h2o2pmml.binary.h2opojo.Node currentNode, Node parentNode, String leftId, String rightId) {
		switch(currentNode.getSplit_direction()) {
			case SPLIT_ON_NAN_TEST_NAN_RIGHT: parentNode.setDefaultChild(rightId);
				break;
			case SPLIT_ON_OPERATION_TEST_NAN_LEFT: parentNode.setDefaultChild(leftId);
				break;
			case SPLIT_ON_OPERATION_TEST_NAN_RIGHT: parentNode.setDefaultChild(rightId);
				break;
			default: parentNode.setDefaultChild(leftId);
				break;
		}
	}
	
	protected static String getAttributeTestedName(com.amadeus.tec.h2o2pmml.binary.h2opojo.Node node, H2OModel model) {
		return model.getAttribute_columns().get((node).getAttribute_tested_column_index()).getColumn_name();
	}
	
	protected static String getDomainValueTested(SetInclusionNode setNode,  H2OModel model, int index) {
		return model.getAttribute_columns().get(setNode.getAttribute_tested_column_index()).getDomainValues().get(index);
	}
	
	protected static Collection<Object> createSetInclusionSimplePredicate(SetInclusionNode setNode, H2OModel model) {
		Collection<Object> setSimplePredicate = new ArrayList<Object>();
		String testedAttribute = getAttributeTestedName(setNode, model);
		byte[] splitSet = setNode.getSplit_set();
		for(int i = 0; i < splitSet.length; i++) {
			if((splitSet[i] & (byte)1) == 1) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8)));
			}
			if((splitSet[i] & (byte)2) == 2) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 1)));
			}
			if((splitSet[i] & (byte)4) == 4) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 2)));
			}
			if((splitSet[i] & (byte)8) == 8) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 3)));
			}
			if((splitSet[i] & (byte)16) == 16) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 4)));
			}
			if((splitSet[i] & (byte)32) == 32) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 5)));
			}
			if((splitSet[i] & (byte)64) == 64) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 6)));
			}
			if((splitSet[i] & (byte)128) == 128) {
				setSimplePredicate.add(new SimplePredicate().withField(testedAttribute).withOperator(PMML_OPERATOR_EQUAL).withValue(getDomainValueTested(setNode, model, i*8 + 7)));
			}
		}
		return setSimplePredicate;
	}


}
