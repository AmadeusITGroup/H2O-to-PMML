package com.amadeus.tec.h2o2pmml.h2omodelini.pojo;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;

import com.amadeus.tec.h2o2pmml.file.FileUtils;

public class H2OModelFactoryTest {
	
	public static final String MODEL = "h2oBinaryTrees/model.ini";
	
	public static final String DOMAIN_FOLDER = "h2oBinaryTrees/domains";
	
	public static final String DOMAIN_FILE_NAME = "d000.txt";
	
	private File getResourcesFile(String path) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(path).getFile());
	}
	
	@Test
	public void getNbColumns() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(MODEL), StandardCharsets.UTF_8);
		assertTrue(H2OModelFactory.getNbColumns(lines) == 25);
	}
	
	@Test
	public void getDomainsFromIniFile() throws IOException {
		List<String> domain = H2OModelFactory.getDomains(getResourcesFile(DOMAIN_FOLDER), DOMAIN_FILE_NAME);
		assertTrue(domain.size() == 1606);
	}
	
	@Test
	public void createH2OModel() throws IOException {
		List<String> lines = FileUtils.readAllLines(getResourcesFile(MODEL), StandardCharsets.UTF_8);
		H2OModel model = H2OModelFactory.createH2OModel(lines, getResourcesFile(DOMAIN_FOLDER));
		List<H2OAttribute> attributes = model.getAttribute_columns();
		assertTrue(attributes.size() == 25);
		assertTrue(attributes.get(0).getColumn_name().equals("col_0"));
		assertTrue(attributes.get(0).getH2ovariable() == H2OVARIABLE.CATEGORICAL_VARIABLE);
		assertTrue(attributes.get(0).getDomainValues().size() == 1606);
		assertTrue(attributes.get(24).getColumn_name().equals("col_24"));
		assertTrue(attributes.get(24).getH2ovariable() == H2OVARIABLE.CATEGORICAL_VARIABLE);
		assertTrue(attributes.get(24).getDomainValues().size() == 2);
	}

}
