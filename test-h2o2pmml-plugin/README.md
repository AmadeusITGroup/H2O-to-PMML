# H2O2PMML Test project of the H2O2PMML Maven Plugin

This folder contains a simple Maven project using the H2O2PMML Maven Plugin to generate some .xml files from H2O binary trees.

# Quick Start

From the root directory of this generation Maven project:

    maven clean install

The H2O source files are available in the default src/main/resources folder and must respect the following hierarchy:

    resources folder
    - domains folder
    -- .txt domain files
    - trees folder
    -- .bin tree files
    - model.ini model file

The resulting xml files will be available at the default folder: src/main/resources/xmltrees.

Source and destination folders can be configured when using the maven plugin. E.g.:

    <configuration>
		<h2oSourceFolder>src/main/resources/custompath</h2oSourceFolder>
		<destinationFolder>src/main/resources/custompath/xmltrees</destinationFolder>
	</configuration>