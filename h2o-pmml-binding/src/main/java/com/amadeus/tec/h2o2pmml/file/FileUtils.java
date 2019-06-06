package com.amadeus.tec.h2o2pmml.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * @author ahockkoon
 * Utility class to read files, mainly to read the H2O files (model .ini, domains .txt, binary tree .bin)
 */
public class FileUtils {

	public static byte[] readAllBytes(File binaryTreefile) throws IOException {
		return Files.readAllBytes(binaryTreefile.toPath());
	}
	
	public static List<String> readAllLines(File modelFile, Charset encoding) throws IOException {
		return Files.readAllLines(modelFile.toPath(), encoding);
	}
	
	public static File createDomainFile(File domainFolder, String domainFileName) {
		return new File(domainFolder.getPath() + "/" + domainFileName);
	}
}
