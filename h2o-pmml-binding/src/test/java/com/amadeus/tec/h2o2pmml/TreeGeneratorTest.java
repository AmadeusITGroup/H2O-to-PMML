package com.amadeus.tec.h2o2pmml;

import java.io.File;

import org.junit.Test;

import com.amadeus.tec.h2o2pmml.binary.bind.H2OBinaryUnmarshallerTest;
import com.amadeus.tec.h2o2pmml.h2omodelini.pojo.H2OModelFactoryTest;

public class TreeGeneratorTest {
	
	private File getResourcesFile(String path) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(path).getFile());
	}
	
	@Test
	public void generateXMLString() throws Exception {
		TreeGenerator treeGen = new TreeGenerator(getResourcesFile(H2OModelFactoryTest.MODEL), getResourcesFile(H2OModelFactoryTest.DOMAIN_FOLDER));
		treeGen.generateXMLString(getResourcesFile(H2OBinaryUnmarshallerTest.T00_001));
	}

}
