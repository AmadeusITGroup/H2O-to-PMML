# H2O2PMML Project

This project generates from .bin files representing H2O GBM MOJO trees their equivalent as .xml files respecting the PMML standard.

Versions supported: H2O 3.X - PMML 4.3.

Supported Java Version: 11.

Java 11 is mandatory due to the use of new static methods such as java.nio.file.Files.writeString().

# Quick Start

The H2O2PMML project is composed of 4 dependent maven projects. They need to be built in the following order:

- [pmml-4-3-pojo](pmml-4-3-pojo): the POJOs generation project from PMML 4.3 xsd;
- [h2o-pmml-binding](h2o-pmml-binding): the binding project between H2O binary tree POJOs and PMML POJOs;
- [h2o2pmml-maven-plugin](h2o2pmml-maven-plugin): the definition of the maven plugin for xml file generation from H2O binary trees;
- [test-h2o2pmml-plugin](test-h2o2pmml-plugin): (OPTIONAL) a simple maven project example reusing the plugin to generate XML tree files.

test-h2o2pmml-plugin is a test project reusing the maven plugin. It is an example on how to use the plugin. During its build, it will parse the H2O files stored in [src/main/resources](test-h2o2pmml-plugin/src/main/resources) and generate .xml tree files at src/main/resources/xmltrees.