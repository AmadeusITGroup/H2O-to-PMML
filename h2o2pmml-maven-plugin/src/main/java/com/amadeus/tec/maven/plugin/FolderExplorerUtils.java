package com.amadeus.tec.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ahockkoon
 * Utility class to access the H2O files
 */
public class FolderExplorerUtils {
	
	public static String H20_DEFAULT_MODEL_INI_FILE_NAME = "model.ini";
	
	public static String H2O_DEFAULT_BINARY_TREE_FOLDER_NAME = "trees";
	
	public static String H2O_DEFAULT_BINARY_TREE_EXTENSION = ".bin";
	
	public static String H2O_DEFAULT_DOMAIN_FOLDER_NAME = "domains";
	
	protected static File getFile(File sourceFolder, String target) throws Exception {
		File file = new File(sourceFolder.getPath() + "/" + target);
		if(file.exists()) {
			return file;
		} else {
			throw new Exception("FILE NOT FOUND: " + file.getPath());
		}
	}

	public static File getH2OModelIniFile(File sourceFolder) throws Exception {
		return getFile(sourceFolder, H20_DEFAULT_MODEL_INI_FILE_NAME);
	}
	
	public static File getH2ODomainFolder(File sourceFolder) throws Exception {
		return getFile(sourceFolder, H2O_DEFAULT_DOMAIN_FOLDER_NAME);
	}
	
	public static File[] getH2OBinaryTreeFiles(File sourceFolder) throws Exception {
		return getFile(sourceFolder, H2O_DEFAULT_BINARY_TREE_FOLDER_NAME).listFiles((fileDirectory, fileStringName) -> {
			return fileStringName.toLowerCase().endsWith(H2O_DEFAULT_BINARY_TREE_EXTENSION);
		});
	}
	
	public static void generateXMLFile(String xmlContent, File destinationFolder, File binaryTree) throws IOException {
		if(!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		Path file = Paths.get(generatePathXMLFile(destinationFolder, binaryTree));
		Files.writeString(file, xmlContent);
	}
	
	public static String generatePathXMLFile(File destinationFolder, File binaryTree) {
		return destinationFolder.getPath() + "/" + binaryTree.getName() + ".xml";
	}
}
