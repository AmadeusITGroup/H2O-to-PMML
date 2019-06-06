# H2O2PMML MAven Plugin Specification

This folder contains the definition of the maven plugin which generates PMML 4.3 compliant .xml files from H2O 3.X binary trees. It reuses the [pmml-4-3-pojo](../pmml-4-3-pojo) and [h2o-pmml-binding](../h2o-pmml-binding) projects.

# Quick Start

From the root directory of this Maven project:

    maven clean install

The plugin will be available in your local mvn repository. Check [test-h2o2pmml-plugin](../test-h2o2pmml-plugin) for an example.