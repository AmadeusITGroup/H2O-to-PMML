package com.amadeus.tec.h2o2pmml.binary.bind;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class H2OBinaryUnmarshallerTest {
	
	public static final String T00_001 = "h2oBinaryTrees/trees/t00_001.bin";
	
	private File getResourcesFile(String path) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(path).getFile());
	}
	
	@Test
	public void unmarshal() throws IOException {
		H2OBinaryUnmarshaller.unmarshal(getResourcesFile(T00_001));
	}

}
