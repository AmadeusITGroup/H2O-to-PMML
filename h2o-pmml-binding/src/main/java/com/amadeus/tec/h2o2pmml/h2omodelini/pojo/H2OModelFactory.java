package com.amadeus.tec.h2o2pmml.h2omodelini.pojo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amadeus.tec.h2o2pmml.file.FileUtils;
import com.amadeus.tec.h2o2pmml.h2omodelini.bind.H2ODomainUnmarshaller;

/**
 * @author ahockkoon
 * Utility class to create H2OModel POJO representing the model.ini file
 */
public class H2OModelFactory {
	
	public static String COLUMNS_MARKER = "[columns]";
	
	public static String N_COLUMNS_REGEX = "n_columns = (\\d+)";
	
	public static String DOMAINS_MARKER = "[domains]";
	
	public static String DOMAINS_REGEX = "(\\d+): (\\d+) (d\\d+\\.txt)";

	/**
	 * @param lines List of String for each lines of the model.ini file
	 * @param domainFolder
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static H2OModel createH2OModel(List<String> lines, File domainFolder) throws NumberFormatException, IOException {
		List<H2OAttribute> attributes = new ArrayList<H2OAttribute>();
		int start = lines.indexOf(COLUMNS_MARKER) + 1;
		int stop = start + getNbColumns(lines);
		for(int i = start; i < stop; i++) {
			attributes.add(new H2OAttribute(lines.get(i), H2OVARIABLE.CONTINUOUS_VARIABLE));
		}
		start = lines.indexOf(DOMAINS_MARKER) + 1;
		Pattern pattern = Pattern.compile(DOMAINS_REGEX);
		Matcher matcher;
		boolean continueLoop = true;
		while(continueLoop && start < lines.size()) {
			matcher = pattern.matcher(lines.get(start));
			if(matcher.find()) {	
				attributes.get(Integer.parseInt(matcher.group(1))).withH2ovariable(H2OVARIABLE.CATEGORICAL_VARIABLE).setDomainValues(getDomains(domainFolder, matcher.group(3)));
			} else {
				continueLoop = false;
			}
			start++;
		}
		H2OModel model = new H2OModel();
		model.setAttribute_columns(attributes);
		return model;
	}
	
	protected static int getNbColumns(List<String> lines) {
		Pattern pattern = Pattern.compile(N_COLUMNS_REGEX);
		Matcher matcher;
		for(int i = 0; i < lines.size(); i++) {
			matcher = pattern.matcher(lines.get(i));
			if(matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			}
		}
		return 0;
	}
	
	protected static List<String> getDomains(File domainFolder, String domainFileName) throws IOException {
		return H2ODomainUnmarshaller.loadDomainValuesFromFile(FileUtils.createDomainFile(domainFolder, domainFileName), StandardCharsets.UTF_8);
	}

}
