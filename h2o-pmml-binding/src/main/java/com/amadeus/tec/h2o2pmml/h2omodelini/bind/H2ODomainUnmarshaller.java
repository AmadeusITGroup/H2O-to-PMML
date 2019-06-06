package com.amadeus.tec.h2o2pmml.h2omodelini.bind;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.amadeus.tec.h2o2pmml.file.FileUtils;

/**
 * @author ahockkoon
 * Unmarshaller class to extract the list of values for the categorical features from the domains .txt files
 *
 */
public class H2ODomainUnmarshaller {
	
	/**
	 * @param domainTxtFile domain .txt File reference
	 * @param encoding
	 * @return Each string of the List is a possible value of the categorical feature associated with the domain .txt file
	 * @throws IOException
	 */
	public static List<String> loadDomainValuesFromFile(File domainTxtFile, Charset encoding) throws IOException{
		return FileUtils.readAllLines(domainTxtFile, encoding);
	}

}
