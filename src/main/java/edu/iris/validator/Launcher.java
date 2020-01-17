package edu.iris.validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import edu.iris.converter.SeedToXmlDocumentConverter;
import edu.iris.seed.SeedVolume;
import edu.iris.seed.io.SeedFileUtils;
import edu.iris.station.io.StationFileUtils;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.validator.logger.MessageLogger;
import edu.iris.validator.logger.MessageLoggerFactory;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineService;

public class Launcher {

	private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

	private static List<File> list(File f) throws IOException {
		return Arrays.asList(f.listFiles());
	}

	public static void process(List<File> input, RuleEngineService ruleEngineService, MessageLogger messageLogger,
			boolean continueOnError) throws IOException, Exception {
		for (File f : input) {
			if (!f.exists()) {
				throw new IOException(String.format("File %s does not exist.  File is required!", f.getName()));
			}

			if (f.isDirectory()) {
				process(list(f), ruleEngineService, messageLogger, continueOnError);
				return;
			}

			String fileName = f.getName();
			if (logger.isInfoEnabled()) {
				logger.info("Processing {}", fileName);
			}

			messageLogger.header(fileName);
			FDSNStationXML document = null;
			try {
				if (fileName.toLowerCase().endsWith(".xml")) {
					document = StationFileUtils.stationXmlDocument(f);
				} else {
					SeedVolume volume = SeedFileUtils.toSeedVolume(f);
					document = SeedToXmlDocumentConverter.newInstance().convert(volume);
				}

				if (document == null) {
					continue;
				}

				for (Network n : document.getNetwork()) {
					if (logger.isDebugEnabled()) {
						logger.trace("Validating Network {}", n.getCode());
					}
					Map<Integer, Set<Message>> messages = ruleEngineService.executeAllRules(n);
					if (messages != null && !messages.isEmpty()) {
						List<Message> list = messages.values().stream().flatMap(Collection::stream)
								.collect(Collectors.toList());
						for (Message m : list) {
							messageLogger.log(m);
						}
					} else {
						//messageLogger.log("Success...");
						logger.info("Success...");
					}
				}
			} catch (Exception e) {
				StringBuilder messageBuilder = new StringBuilder("Exception parsing file: " + fileName + "\n");
				if (e.getCause() instanceof SAXParseException) {
					SAXParseException s = (SAXParseException) e.getCause();
					messageBuilder.append("Error when validating XML against FDSN-Station XSD schema\n" + "lineNumber: "
							+ s.getLineNumber() + ";\ncolumnNumber: " + s.getColumnNumber() + ";\n"
							+ s.getMessage().substring(s.getMessage().indexOf(":") + 2));
				} else {
					messageBuilder.append(e.getMessage());
				}

				if (continueOnError) {
					logger.error(messageBuilder.toString());
				} else {
					throw new StationxmlException(messageBuilder.toString(), e);
				}
			}
		}
	}

	public static void process(List<File> input, OutputStream outputStream, RuleEngineService ruleEngineService,
			String format, boolean continueOnError) throws IOException, Exception {

		try (Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));) {
			process(input, ruleEngineService, MessageLoggerFactory.create(outputStream, format), continueOnError);
		}

	}

}
