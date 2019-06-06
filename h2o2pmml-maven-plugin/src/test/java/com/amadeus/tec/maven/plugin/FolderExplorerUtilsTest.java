package com.amadeus.tec.maven.plugin;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FolderExplorerUtilsTest {
	
	public static final String SOURCE_FOLDER = "h2oBinaryTrees";
	
	public static final String NOT_EXISTING_FILE = "badFileName.txt";
	
	private File getResourcesFile(String path) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(path).getFile());
	}
	
	@Test
	public void getFile() {
		Boolean result = false;
		try {
			FolderExplorerUtils.getFile(getResourcesFile(SOURCE_FOLDER), NOT_EXISTING_FILE);
		} catch (Exception e) {
			result = true;
		}
		assertTrue(result);
	}
	
	@Test
	public void getH2OModelIniFile() throws Exception {
		FolderExplorerUtils.getH2OModelIniFile(getResourcesFile(SOURCE_FOLDER));
	}
	
	@Test
	public void getH2ODomainFolder() throws Exception {
		FolderExplorerUtils.getH2ODomainFolder(getResourcesFile(SOURCE_FOLDER));
	}
	
	@Test
	public void getH2OBinaryTreeFiles() throws Exception {
		FolderExplorerUtils.getH2OBinaryTreeFiles(getResourcesFile(SOURCE_FOLDER));
	}
	
	public static final String FILE_CONTENT = "my content";
	
	public static final String DEST_FOLDER = "target";
	
	public static final String BINARY_FILE = "h2oBinaryTrees/trees/t00_001.bin";
	
	public static final String RESULTING_XML_FILE = "/t00_001.bin.xml";
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Test
	public void generateXMLFile() throws IOException {
		File destFolder = testFolder.newFolder(DEST_FOLDER);
		FolderExplorerUtils.generateXMLFile(FILE_CONTENT, destFolder, getResourcesFile(BINARY_FILE));
		File resultingFile = new File(destFolder.getAbsolutePath() + RESULTING_XML_FILE);
		assertTrue(resultingFile.exists());
	}

}
