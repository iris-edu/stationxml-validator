package edu.iris.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import edu.iris.validator.rules.Rule;
import edu.iris.validator.rules.RuleEngineService;
import edu.iris.validator.rules.UnitTable;

public class ResourceUtil {

	public static String getVersion() throws IOException {
		Properties prop = new Properties();
		try (InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties");) {
			prop.load(in);
			in.close();
			return prop.getProperty("application.version");
		}
	}

	public static String fileToString(String file) throws IOException {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(Application.class.getClassLoader().getResourceAsStream(file)))) {
			StringBuilder out = new StringBuilder();
			String line = null;
			int i=0;
			while ((line = reader.readLine()) != null) {
				if(i>0) {
					out.append(System.lineSeparator());
				}
				out.append(line.trim());
				i++;
			}
			return out.toString();
		}
	}

	public static String getRules() {
		RuleEngineService ruleEngineService = new RuleEngineService(false, null);
		StringBuilder b = new StringBuilder();
		for (Rule rule : ruleEngineService.getRules()) {
			b.append(String.format("%-8s %s%n", rule.getId(), rule.getDescription()));
			// System.out.printf("%-8s %s%n", rule.getId(), rule.getDescription());
		}
		return b.toString();
	}

	public static String getUnits() {
		StringBuilder b = new StringBuilder("UNIT TABLE:").append(System.lineSeparator());
		b.append("-------------------------------------").append(System.lineSeparator());
		for (String unit : UnitTable.units) {
			b.append(unit).append(System.lineSeparator());
		}
		return b.toString();
	}
}
