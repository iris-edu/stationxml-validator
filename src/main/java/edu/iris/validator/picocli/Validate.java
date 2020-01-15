package edu.iris.validator.picocli;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import edu.iris.validator.Application;
import edu.iris.validator.Launcher;
import edu.iris.validator.logger.MessageLogger;
import edu.iris.validator.logger.MessageLoggerFactory;
import edu.iris.validator.rules.RuleEngineService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

@Command(name = "validate", description = "Station metadata validation.")
public class Validate implements Runnable {

	
	private static final Logger logger = LoggerFactory.getLogger(Validate.class);
	@ParentCommand
	private Application parent;
	@Spec
	private CommandSpec spec; // injected by picocli

	@Mixin
	private VersionOption version;

	@Mixin
	private VerboseOption verbose;

	@Mixin
	private HelpOption usageHelpRequested;

	@Mixin
	private OutputOption output;

	@Option(names = { "r","--ignore-rules" }, description = "list of rules to ignore by the validator")
	private int[] ignoreRules;

	@Option(names = { "-w","--ignore-warnings" }, description = "do not show warnings, default false")
	private boolean ignoreWarnings;

	@Option(names = { "-c" }, description = "Continue execution when a target file fails to parse.")
	private boolean continueOnError;

	@Option(names = { "-f", "--format" }, description = "format of the generated report CSV|XML")
	private String format = "CSV";

	@Parameters(arity = "1..*", description = "Any number of input files SEED|XML")
	private List<Path> inputPaths = new ArrayList<>();

	@Override
	public void run() {
		try (OutputStream out = output.getOutputFile() == null ? System.out
				: new FileOutputStream(output.getOutputFile());
				MessageLogger formatter = MessageLoggerFactory.create(out, format)) {
			RuleEngineService ruleEngineService = new RuleEngineService(ignoreWarnings, ignoreRules);

			Level l = Level.toLevel(Integer.MAX_VALUE, Level.OFF);
			if (verbose != null) {
				switch (verbose.length()) {
				case 1:
					l = Level.ERROR;
					break;
				case 2:
					l = Level.WARN;
					break;
				case 3:
					l = Level.INFO;
					break;
				case 4:
					l = Level.DEBUG;
					break;
				default:
					l = Level.OFF;
				}
			}
			ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
					.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			root.setLevel(l);
			System.out.println(l+"   "+verbose);
			if (logger.isInfoEnabled()) {
				logger.info("Launching validaterX...");
			}
			Launcher.process(inputPaths, ruleEngineService, formatter, continueOnError);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
