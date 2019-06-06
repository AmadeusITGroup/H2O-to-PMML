package com.amadeus.tec.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amadeus.tec.h2o2pmml.TreeGenerator;
 
/**
 * @author ahockkoon
 * Maven plugin definition to generate .xml files from H2O binary trees.
 * the H2O source folder containing the H2O binary trees .bin, model .ini, and domains .txt should have the following structure:
 * 
 * h2oSourceFolder
 * - domains folder
 * -- .txt domains files
 * - trees folder
 * -- .bin binary tree files
 * - model.ini h2o model file
 *
 */
@Mojo( name = "generatePMML")
public class H2O2PMMLMojo extends AbstractMojo {
	
	@Parameter(property = "generatePMML.h2oSourceFolder", defaultValue = "src/main/resources")
	private File h2oSourceFolder;
	
	@Parameter(property = "generatePMML.destinationFolder", defaultValue = "src/main/resources/xmltrees")
	private File destinationFolder;
	
	public void execute() throws MojoExecutionException
    {
        getLog().info( "generatePMML START" );
        getLog().info( "h2oSourceFolder: " + h2oSourceFolder.getPath());
        getLog().info( "destinationFolder: " + destinationFolder.getPath());
    
		try {
			File model = FolderExplorerUtils.getH2OModelIniFile(h2oSourceFolder);
			File domainFolder = FolderExplorerUtils.getH2ODomainFolder(h2oSourceFolder);
			
			TreeGenerator treeGen = new TreeGenerator(model, domainFolder);
			
			File[] trees = FolderExplorerUtils.getH2OBinaryTreeFiles(h2oSourceFolder);
			getLog().info("Found " + trees.length + " binary trees");
			
	        for(int i = 0; i < trees.length; i++) {
	        	getLog().info( "Generating tree: " + trees[i].getName() + " -> " + FolderExplorerUtils.generatePathXMLFile(destinationFolder, trees[i]));
	        	FolderExplorerUtils.generateXMLFile(treeGen.generateXMLString(trees[i]), destinationFolder, trees[i]);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
