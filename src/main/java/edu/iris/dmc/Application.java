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
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.seed.Blockette;
import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.seed.Volume;
import edu.iris.dmc.seed.blockette.util.BlocketteItrator;
import edu.iris.dmc.seed.builder.BlocketteBuilder;
import edu.iris.dmc.seed.director.BlocketteDirector;
import edu.iris.dmc.Application;
import edu.iris.dmc.station.RuleEngineService;
import edu.iris.dmc.Application.ExtractorHandler;
import edu.iris.dmc.station.converter.SeedToXmlDocumentConverter;
import edu.iris.dmc.station.exceptions.StationxmlException;
import edu.iris.dmc.station.io.CsvPrintStream;
import edu.iris.dmc.station.io.HtmlPrintStream;
import edu.iris.dmc.station.io.ReportPrintStream;
import edu.iris.dmc.station.io.RuleResultPrintStream;
import edu.iris.dmc.station.io.XmlPrintStream;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.UnitTable;

public class Application {
	//private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
	private static Logger LOGGER = null;
	  static {
	      System.setProperty("java.util.logging.SimpleFormatter.format",
	    		  "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] [%4$-6s] %2$s:"
	    		  + " %5$s%6$s %n");
	      LOGGER = Logger.getLogger(Application.class.getName());
	  }
	private static CommandLine commandLine;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			LOGGER.setLevel(Level.WARNING);
			commandLine = CommandLine.parse(args);
		} catch (CommandLineParseException e) {
			StringBuilder message = createExceptionMessage(e);
			LOGGER.severe(message.toString());
			help();
			System.exit(1);
		}
		LOGGER.setLevel(Level.WARNING);
		LOGGER.setLevel(commandLine.getLogLevel());
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
			LOGGER.severe("File is required!");
			help();
			System.exit(1);
		}
		// Add configurable verboisty to this project. 
		// Update the logger to work similar to converter. 
		try {
			Application app = new Application();
			app.run();
		} catch (Exception e) {
			exitWithError(e);
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
				
				// This is where the output is printed. 
				print(ps, ruleEngineService.executeAllRules(document));
			}
		} catch (Exception e) {
			exitWithError(e);
			//e.printStackTrace();
		}

	}

	private FDSNStationXML read(Path path) throws Exception {
		File file = path.toFile();
		if (!file.exists()) {
			LOGGER.severe("File does not exist.  File is required!");
			throw new IOException(String.format("File %s does not exist.  File is required!", file.getAbsoluteFile()));
		}
		    InputStream is = new FileInputStream(file);

			// This is where stationxml vs seed is decided. 
			if(isStationXml(file)) {
				try {
				   LOGGER.info("Input file: " + path.toString());
				   return DocumentMarshaller.unmarshal(is);
				} catch (StationxmlException | IOException | RuntimeException e){
				    exitWithError(e);
					//StringBuilder message = createExceptionMessage(e);
					//LOGGER.severe(message.toString());
					return null;
				}
			} else {
				try {
				    LOGGER.info("Input file: " + path.toString());
				    Volume volume = IrisUtil.readSeed(file);
				return SeedToXmlDocumentConverter.getInstance().convert(volume);
			    }catch(RuntimeException e){
				    exitWithError(e);
				    //StringBuilder message = createExceptionMessage(e);		
				    //LOGGER.severe(message.toString());
				   return null;	
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
				ps.printMessage("\n");
			}
		} else {
			ps.printMessage("PASSED");
			ps.printMessage("\n");
			ps.printMessage("\n");

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
	
	
	private static void exitWithError(Exception e) {
		StringBuilder message = createExceptionMessage(e);
		if (commandLine.continueError()==true){
			LOGGER.severe(message.toString());
	        //null 
		}else {
			LOGGER.severe(message.toString());
		    System.exit(1);
	    }
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
		System.out.println("===============================================================");
		System.out.println("|" + center("StationXML Validation Rule List", 62, " ") + "|");
		System.out.println("===============================================================");
		//System.out.print("\n");
		System.out.print("Enforces FDSN StationXML Schema Version 1.1 Compliance\n");
		System.out.print("Level 100: Network\n");
		System.out.print("Level 200: Station\n");
		System.out.print("Level 300: Channel\n");
		System.out.print("Level 400: Response\n");
		System.out.print("Error: IF Error==False then document is invalid\n");
		System.out.print("Warning: IF Warning==TRUE then Return message and PASS else PASS\n");
		System.out.print("Epoch=startDate-endDate\n");
		System.out.print("Indices: (N AND M) > 1 AND (N > M)\n");
		//System.out.print("\n");
		System.out.println("===============================================================");
		System.out.println("|" + center("| Rule ID | Description | Type |", 62, " ") + "|");
		System.out.println("===============================================================");
		for (Rule rule : ruleslist) {
			System.out.printf("%-8s %s%n", rule.getId(), rule.getDescription());
		}
		System.out.println("===============================================================");

	}
	
	private static Comparator<Rule> comparator = new Comparator<Rule>() {
		public int compare(Rule c1, Rule c2) {

			int r = Integer.compare(c1.getId(), c2.getId());
			
			return r;
		}
	};

	private static void printUnits() {
		System.out.println("===============================================================");
		System.out.println("|" + center("Table of Acceptable Units", 62, " ") + "|");
		System.out.println("===============================================================");
		
		List<String> unitlist = UnitTable.units;
		int stride = (unitlist.size()/4);
		for (int row = 0; row < unitlist.size()/4; row++) {
		    System.out.println(String.format("%15s %15s %15s %15s", 
		    		unitlist.get(row), unitlist.get(row + stride), 
		    		unitlist.get(row + stride * 2), unitlist.get(row + stride * 3)));
		}
		
		System.out.println("===============================================================");

}

	private static void help() throws IOException {
		String version = "Version " + getVersion();
		version = center(version, 62, " ");

		System.out.println("===============================================================");
		System.out.println("|" + center("FDSN StationXml Validator", 62, " ") + "|");
		System.out.println("|" + version + "|");
		System.out.println("================================================================");
		System.out.println("Usage:");
		System.out.println("java -jar stationxml-validator <FILE> [OPTIONS]");
		System.out.println("OPTIONS:");
		System.out.println("   --file               : Full input file path");
		System.out.println("   --output             : where to output result, default is System.out");
		System.out.println("   --ignore-warnings    : don't show warnings");
		System.out.println("   --rules              : print a list of validation rules");
		System.out.println("   --units              : print a list of units used to validate");
		System.out.println("   --verbose            : Change the verobsity level to info");
		System.out.println("   --debug              : Change the verobsity level to debug");
		System.out.println("   --help               : print this message");
		System.out.println("   --continue-on-error, usage = Prints exceptions to stdout and processes next file");
		System.out.println("===============================================================");
		System.exit(0);
	}
	
	private static StringBuilder createExceptionMessage(Exception e) {
		StringBuilder message = new StringBuilder(
				"");
		if(e.getLocalizedMessage() != null) {
		    message.append(e.getLocalizedMessage());
		}
		for (StackTraceElement element : e.getStackTrace()){
			message.append(element.toString()).append("\n");
		}
		if (e.getCause() != null) {
			message.append(e.getCause().getLocalizedMessage());
			for (StackTraceElement element : e.getCause().getStackTrace()) {
				message.append(element.toString()).append("\n");
			}
		}
		return message;
	}
	
	
	private boolean isStationXml(File source) throws IOException {
		if (source == null) {
			throw new IOException("File not found");
		}
		ExtractorHandler handler = new ExtractorHandler();
		try (InputStream inputStream = new FileInputStream(source)) {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(inputStream, handler);
		} catch (

		Exception e) {
			// do nothing
		}
		QName qname = handler.rootElement;
		if (qname == null) {
			return false;
		}

		if ((new QName("http://www.fdsn.org/xml/station/1", "FDSNStationXML")).equals(qname)) {
			return true;
		}
		return false;
	}

	protected static class ExtractorHandler extends DefaultHandler {

		private QName rootElement = null;

		@Override
		public void startElement(String uri, String local, String name, Attributes attributes) throws SAXException {
			this.rootElement = new QName(uri, local);
			throw new SAXException("Aborting: root element received");
		}

		QName getRootElement() {
			return rootElement;
		}
	}
 }


