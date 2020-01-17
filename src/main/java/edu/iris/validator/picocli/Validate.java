package edu.iris.validator.picocli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
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

	private static final Logger logger = (Logger) LoggerFactory.getLogger(Validate.class);
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

	@Option(names = { "-r", "--ignore-rules" }, description = "list of rules to ignore by the validator")
	private int[] ignoreRules;

	@Option(names = { "-w", "--ignore-warnings" }, description = "do not show warnings, default false")
	private boolean ignoreWarnings;

	@Option(names = { "-c" }, description = "Continue execution when a target file fails to parse.")
	private boolean continueOnError;

	@Option(names = { "-f", "--format" }, description = "format of the generated report CSV|XML")
	private String format = "CSV";

	@Parameters(arity = "1..*", description = "Any number of input files SEED|XML")
	private List<File> inputPaths = new ArrayList<>();

	@Override
	public void run() {
		try (OutputStream out = output.getOutputFile() == null ? System.out
				: new FileOutputStream(output.getOutputFile());
				MessageLogger formatter = MessageLoggerFactory.create(out, format)) {
			RuleEngineService ruleEngineService = new RuleEngineService(ignoreWarnings, ignoreRules);

			setLogLevel(verbose == null ? 0 : verbose.length());
			if (logger.isInfoEnabled()) {
				logger.info("Launching validaterX... verbose:{}", logger.getLevel());
				StringBuilder b = new StringBuilder();
				for (File f : inputPaths) {
					b.append(f.getName());
					if (f.isDirectory()) {
						b.append(" [").append(f.list().length).append("]");
					}
					b.append(",");
				}
				logger.info("root file(s): " + b.toString());
			}
			Launcher.process(inputPaths, ruleEngineService, formatter, continueOnError);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setLogLevel(int length) {
		Level l = Level.toLevel(Integer.MAX_VALUE, Level.OFF);
		switch (length) {
		case 0:
			l = Level.OFF;
			break;
		case 1:
			l = Level.INFO;
			break;
		case 2:
			l = Level.DEBUG;
			break;
		default:
			l = Level.TRACE;
		}
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (Logger logger : loggerContext.getLoggerList()) {
			logger.setLevel(l);
		}
	}
}