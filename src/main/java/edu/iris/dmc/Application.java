package edu.iris.dmc;

/*
stationxml validator
Copyright (C) 2020  IRIS

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.seed.Blockette;
import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.seed.Volume;
import edu.iris.dmc.seed.blockette.util.BlocketteItrator;
import edu.iris.dmc.seed.builder.BlocketteBuilder;
import edu.iris.dmc.seed.director.BlocketteDirector;
import edu.iris.dmc.station.RuleEngineService;
import edu.iris.dmc.station.converter.SeedToXmlDocumentConverter;
import edu.iris.dmc.station.io.CsvPrintStream;
import edu.iris.dmc.station.io.HtmlPrintStream;
import edu.iris.dmc.station.io.ReportPrintStream;
import edu.iris.dmc.station.io.RuleResultPrintStream;
import edu.iris.dmc.station.io.XmlPrintStream;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.UnitTable;

public class Application {
	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

	private static CommandLine commandLine;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			commandLine = CommandLine.parse(args);
		} catch (CommandLineParseException e) {
			System.err.println(e.getMessage()+ "\n");
			help();
			System.exit(1);
		}
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		rootLogger.setLevel(commandLine.getLogLevel());
		for (Handler h : rootLogger.getHandlers()) {
			h.setLevel(commandLine.getLogLevel());
		}
		LOGGER.info("STATION VALIDATOR");

		if (commandLine.showVersion()) {
			LOGGER.info(Application.getVersion());
			System.exit(0);
		} else if (commandLine.showHelp()) {
			help();
			System.exit(0);
		} else if (commandLine.showRules()) {
			printRules();
			System.exit(0);
		} else if (commandLine.showUnits()) {
			printUnits();
			System.exit(0);
		} else if (commandLine.file() == null) {
			LOGGER.info("File is required!");
			help();
			System.exit(1);
		}

		try {
			Application app = new Application();
			app.run();
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage(), e);
			System.exit(1);
		}
	}

	public void run() throws Exception {
		Path path = commandLine.file();
		if (!path.toFile().exists()) {
			throw new IOException(String.format("File %s does not exist.  File is required!", path.toString()));
		}
		List<Path> input = new ArrayList<>();
		if (path.toFile().isDirectory()) {
			try (Stream<Path> paths = Files.walk(path)) {
				input = paths.filter(Files::isRegularFile).collect(Collectors.toList());
			}
		} else {
			input.add(path);
		}

		File outputFile = null;
		if (commandLine.output() != null) {
			outputFile = commandLine.output().toFile();
			if (!outputFile.exists()) {
				outputFile.createNewFile();
				//throw new IOException(String.format("File %s is not found!", commandLine.output().toString()));
			}
		
		    try (OutputStream outputStream =  new FileOutputStream(outputFile)) {
			    run(input, "csv", outputStream, commandLine.ignoreRules(), commandLine.ignoreWarnings());
		}
		}else {
			try (OutputStream outputStream = System.out;) {
				run(input, "csv", outputStream, commandLine.ignoreRules(), commandLine.ignoreWarnings());
			}
			
		}
		
	}
	
	private void run(List<Path> input, String format, OutputStream outputStream, int[] ignoreRules,
			boolean ignoreWarnings) throws Exception {
		RuleEngineService ruleEngineService = new RuleEngineService(ignoreWarnings, ignoreRules);
		try (final RuleResultPrintStream ps = getOutputStream(format, outputStream)) {
			for (Path p : input) {
				FDSNStationXML document = read(p);
				if (document == null) {
					continue;
				}
				print(ps, ruleEngineService.executeAllRules(document));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private FDSNStationXML read(Path path) throws Exception {
		File file = path.toFile();
		String xmlstring = "<FDSNStationXML";
		if (!file.exists()) {
			LOGGER.severe("File does not exist.  File is required!");
			throw new IOException(String.format("File %s does not exist.  File is required!", file.getAbsoluteFile()));
		}
		try (InputStream ts = new FileInputStream(file)) {
			 Reader r = new InputStreamReader(ts, "US-ASCII");
			  int i = 0;
			  String extentionString = "";
              int data = r.read();
              while(i < 100){
              char inputChar = (char) data;
              extentionString +=inputChar;
              data = r.read();
              i = i+1;
           } 
		    InputStream is = new FileInputStream(file);

			// This is where stationxml vs seed is decided. 
			if (extentionString.toLowerCase().contains(xmlstring.toLowerCase())) {
				return DocumentMarshaller.unmarshal(is);
			} else {
				Volume volume = IrisUtil.readSeed(file);
				return SeedToXmlDocumentConverter.getInstance().convert(volume);
			}
		}
	}

	private void print(RuleResultPrintStream ps, Map<Integer, Set<Message>> map) throws IOException {

		if (map != null && !map.isEmpty()) {
			SortedSet<Integer> keys = new TreeSet<>(map.keySet());
			for (Integer key : keys) {
				Set<Message> l = map.get(key);
				for (Message m : l) {
					ps.print(m);
					ps.flush();
				}
			}
		} else {
			ps.printMessage("PASSED\n");
		}

		ps.printFooter();
	}

	public Volume load(File file) throws SeedException, IOException {
		try (final FileInputStream inputStream = new FileInputStream(file)) {
			return load(inputStream);
		}
	}

	public Volume load(InputStream inputStream) throws SeedException, IOException {
		BlocketteDirector director = new BlocketteDirector();
		BlocketteItrator iterator = director.process(inputStream);

		Volume volume = new Volume();
		while (iterator.hasNext()) {
			Blockette blockette = iterator.next();
			volume.add(blockette);
		}
		return volume;
	}

	private RuleResultPrintStream getOutputStream(String format, OutputStream outputStream) throws IOException {
		if (format == null || format.isEmpty() || "html".equalsIgnoreCase(format)) {
			return new CsvPrintStream(outputStream);
		} else if ("csv".equalsIgnoreCase(format)) {
			return new CsvPrintStream(outputStream);
		} else if ("html".equalsIgnoreCase(format)) {
			return new HtmlPrintStream(outputStream);
		} else if ("xml".equalsIgnoreCase(format)) {
			return new XmlPrintStream(outputStream);
		} else if ("report".equalsIgnoreCase(format)) {
			return new ReportPrintStream(outputStream);
		} else {
			throw new IOException("Invalid format [" + format + "] requested");
		}
	}

	private static String getVersion() throws IOException {
		Properties prop = new Properties();
		InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(in);
		in.close();
		return prop.getProperty("application.version");
	}

	private static String center(String text, int length, String pad) {
		int width = length - text.length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < width / 2; i++) {
			builder.append(pad);
		}
		builder.append(text);
		int remainder = length - builder.length();
		for (int i = 0; i < remainder; i++) {
			builder.append(pad);
		}
		return builder.toString();
	}

	private static void printRules() {

		RuleEngineService ruleEngineService = new RuleEngineService(false, null);
		List<Rule> ruleslist = ruleEngineService.getRules();
		Collections.sort(ruleslist, comparator);
		System.out.println("Validator Rule Set:\n");
		System.out.println("--------------------------------------------------------------------------");
		for (Rule rule : ruleslist) {
			System.out.printf("%-8s %s%n", rule.getId(), rule.getDescription());
		}
		System.out.println("--------------------------------------------------------------------------");

	}
	
	private static Comparator<Rule> comparator = new Comparator<Rule>() {
		public int compare(Rule c1, Rule c2) {

			int r = Integer.compare(c1.getId(), c2.getId());
			
			return r;
		}
	};

	private static void printUnits() {
		System.out.println("Table of Acceptable Units:\n");
		
		System.out.println("--------------------------------------------------------------------------");
		
		List<String> unitlist = UnitTable.units;
		int stride = (unitlist.size()/4);
		for (int row = 0; row < unitlist.size()/4; row++) {
		    System.out.println(String.format("%15s %15s %15s %15s", 
		    		unitlist.get(row), unitlist.get(row + stride), 
		    		unitlist.get(row + stride * 2), unitlist.get(row + stride * 3)));
		}
		
		
		//for (String unit : UnitTable.units)  {
		//	 System.out.println();

	//}
	System.out.println("--------------------------------------------------------------------------");

}

	private static void help() throws IOException {
		String version = "Version " + getVersion();
		version = center(version, 62, " ");

		System.out.println("===============================================================");
		System.out.println("|" + center("FDSN StationXml validator", 62, " ") + "|");
		System.out.println("|" + version + "|");
		System.out.println("================================================================");
		System.out.println("Usage:");
		System.out.println("java -jar stationxml-validator <FILE> [OPTIONS]");
		System.out.println("OPTIONS");
		System.out.println("   --file               : Full input file path");
		System.out.println("   --output             : where to output result, default is System.out");
		System.out.println("   --ignore-warnings    : don't show warnings");
		System.out.println("   --rules              : print a list of validation rules");
		System.out.println("   --units              : print a list of units used to validate");
		System.out.println("   --debug              : Change the verobsity level to debug");
		System.out.println("   --help               : print this message");
		System.out.println("===============================================================");
		System.exit(0);
	}

}
