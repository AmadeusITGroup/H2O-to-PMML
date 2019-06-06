package com.amadeus.tec.h2o2pmml.binary.bind;

import java.io.File;
import java.io.IOException;

import com.amadeus.tec.h2o2pmml.binary.h2opojo.AbstractNode;
import com.amadeus.tec.h2o2pmml.binary.h2opojo.NodeFactory;
import com.amadeus.tec.h2o2pmml.file.FileUtils;

public class H2OBinaryUnmarshaller {
	
	public static AbstractNode unmarshal(File binaryTreeFile) throws IOException {
		return NodeFactory.createH2OTree(FileUtils.readAllBytes(binaryTreeFile), 0);
	}

}
