package com.amadeus.tec.h2o2pmml.xml.pmmlpojo;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.dmg.pmml_4_3.CompoundPredicate;
import org.dmg.pmml_4_3.DataDictionary;
import org.dmg.pmml_4_3.MiningSchema;
import org.dmg.pmml_4_3.Node;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.SimplePredicate;
import org.dmg.pmml_4_3.TreeModel;
import org.dmg.pmml_4_3.True;
import org.junit.Test;

import com.amadeus.tec.h2o2pmml.binary.bind.H2OBinaryUnmarshaller;
import com.amadeus.tec.h2o2pmml.binary.bind.H2OBinaryUnmarshallerTest;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.AbstractNode;
import com.amadeus.tec.h2o2pmml.file.FileUtils;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OModel;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OModelFactory;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OModelFactoryTest;
import com.amadeus.tec.h2o2pmml.xml.bind.JAXBMarshaller;

public class H2O2PMMLFactoryTest {
	
	private File getResourcesFile(String path) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(path).getFile());
	}
	
	@Test
	public void createDataDictionary() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		DataDictionary dd = H2O2PMMLFactory.createDataDictionary(model);
		assertTrue(dd.getNumberOfFields().intValue() == 25);
		assertTrue(dd.getDataField().get(0).getName().equals("col_0"));
		assertTrue(dd.getDataField().get(0).getValue().size() == 1606);
		assertTrue(dd.getDataField().get(24).getName().equals("col_24"));
		assertTrue(dd.getDataField().get(24).getValue().size() == 2);
	}
	
	@Test
	public void createMiningSchema() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		MiningSchema schema = H2O2PMMLFactory.createMiningSchema(model);
		assertTrue(schema.getMiningField().size() == 25);
		assertTrue(schema.getMiningField().get(0).getName().equals("col_0"));
		assertTrue(schema.getMiningField().get(24).getName().equals("col_24"));
	}
	
	@Test
	public void createNodes() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		AbstractNode tree = H2OBinaryUnmarshaller.unmarshal(getResourcesFile(H2OBinaryUnmarshallerTest.T00_001));
		Node rootNode = H2O2PMMLFactory.createNodes(tree, model);
		assertTrue(rootNode.getContent().size() == 3);
		assertTrue(rootNode.getContent().get(0) instanceof True);
		assertTrue(rootNode.getContent().get(1) instanceof Node);
		assertTrue(rootNode.getContent().get(2) instanceof Node);
		assertTrue(rootNode.getId().equals("0"));
		assertTrue(rootNode.getDefaultChild().equals("01"));
	}
	
	@Test
	public void createLeftRightNodes() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		AbstractNode tree = H2OBinaryUnmarshaller.unmarshal(getResourcesFile(H2OBinaryUnmarshallerTest.T00_001));
		Node parentNode = new Node().withId("0");
		Collection<Object> content = H2O2PMMLFactory.createLeftRightNodes(tree, model, parentNode);
		
		assertTrue(parentNode.getDefaultChild().equals("01"));
		
		assertTrue(content.size() == 2);
		
		Node leftNode = (Node)content.toArray()[0];
		assertTrue(leftNode.getContent().size() == 3);
		assertTrue(leftNode.getContent().get(0) instanceof CompoundPredicate);
		assertTrue(leftNode.getContent().get(1) instanceof Node);
		assertTrue(leftNode.getContent().get(2) instanceof Node);
		assertTrue(leftNode.getId().equals("01"));
		assertTrue(leftNode.getDefaultChild().equals("012"));
		
		CompoundPredicate leftPredicate = (CompoundPredicate)leftNode.getContent().get(0);
		assertTrue(((SimplePredicate)leftPredicate.getSimplePredicateOrCompoundPredicateOrSimpleSetPredicate().get(0)).getField().equals("col_19"));
		
		Node subLeftLeftNode = (Node)leftNode.getContent().get(1);
		assertTrue(subLeftLeftNode.getContent().size() == 3);
		assertTrue(subLeftLeftNode.getContent().get(0) instanceof SimplePredicate);
		assertTrue(subLeftLeftNode.getContent().get(1) instanceof Node);
		assertTrue(subLeftLeftNode.getContent().get(2) instanceof Node);
		assertTrue(subLeftLeftNode.getId().equals("011"));
		
		SimplePredicate leftLeftPredicate = (SimplePredicate)subLeftLeftNode.getContent().get(0);
		assertTrue(leftLeftPredicate.getValue().equals("26.5"));
		assertTrue(leftLeftPredicate.getField().equals("col_17"));
		assertTrue(leftLeftPredicate.getOperator().equals("greaterOrEqual"));
		
		
		Node rightNode = (Node)content.toArray()[1];
		assertTrue(rightNode.getContent().size() == 3);
		assertTrue(rightNode.getContent().get(0) instanceof True);
		assertTrue(rightNode.getContent().get(1) instanceof Node);
		assertTrue(rightNode.getContent().get(2) instanceof Node);
		assertTrue(rightNode.getId().equals("02"));
		assertTrue(rightNode.getDefaultChild().equals("021"));
		
		Node subRightLeftNode = (Node)rightNode.getContent().get(1);
		assertTrue(subRightLeftNode.getContent().size() == 3);
		assertTrue(subRightLeftNode.getContent().get(0) instanceof CompoundPredicate);
		assertTrue(subRightLeftNode.getContent().get(1) instanceof Node);
		assertTrue(subRightLeftNode.getContent().get(2) instanceof Node);
		assertTrue(subRightLeftNode.getId().equals("021"));
		assertTrue(subRightLeftNode.getDefaultChild().equals("0211"));
		
		CompoundPredicate subRightLeftPredicate = (CompoundPredicate)subRightLeftNode.getContent().get(0);
		assertTrue(((SimplePredicate)subRightLeftPredicate.getSimplePredicateOrCompoundPredicateOrSimpleSetPredicate().get(0)).getField().equals("col_9"));
	}
	
	@Test
	public void createTreeModel() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		AbstractNode tree = H2OBinaryUnmarshaller.unmarshal(getResourcesFile(H2OBinaryUnmarshallerTest.T00_001));
		TreeModel treeModel = H2O2PMMLFactory.createTreeModel(tree, model);
		assertTrue(treeModel.getContent().get(0) instanceof MiningSchema);
		assertTrue(treeModel.getContent().get(1) instanceof Node);
	}
	
	@Test
	public void createPMMLfromH2OTree() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		AbstractNode tree = H2OBinaryUnmarshaller.unmarshal(getResourcesFile(H2OBinaryUnmarshallerTest.T00_001));
		PMML pmml = H2O2PMMLFactory.createPMMLfromH2OTree(tree, model);
		StringWriter stringWriter = new StringWriter();
		JAXBMarshaller.marshal(pmml, stringWriter);
		Path file = Paths.get("src/test/resources/h2oBinaryTrees/trees/pmml.xml");
		Files.writeString(file, stringWriter.toString());
	}
	
	public static final String T00_002 = "h2oBinaryTrees/trees/t00_002.bin";
	
	@Test
	public void createPMMLfromH2OTreeT002() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(H2OModelFactoryTest.MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		AbstractNode tree = H2OBinaryUnmarshaller.unmarshal(getResourcesFile(T00_002));
		PMML pmml = H2O2PMMLFactory.createPMMLfromH2OTree(tree, model);
		StringWriter stringWriter = new StringWriter();
		JAXBMarshaller.marshal(pmml, stringWriter);
		Path file = Paths.get("src/test/resources/h2oBinaryTrees/trees/pmml_t002.xml");
		Files.writeString(file, stringWriter.toString());
	}

}
