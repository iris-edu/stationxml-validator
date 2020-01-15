package edu.iris.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.iris.validator.picocli.Convert;
import edu.iris.validator.picocli.HelpOption;
import edu.iris.validator.picocli.Rules;
import edu.iris.validator.picocli.Units;
import edu.iris.validator.picocli.Validate;
import edu.iris.validator.picocli.VersionOption;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.Spec;

@Command(name = "", versionProvider = Application.ManifestVersionProvider.class, subcommands = {
				Rules.class, Units.class, Validate.class, Convert.class })
public class Application implements Runnable {

	private static String[] banner = new String[] {
			"@|green  _____ _____   _____  _____   _____  __  __  _____    |@",
			"@|green |_   _|  __ \\|_   _|/ ____| |  __ \\|  \\/  |/ ____| |@",
			"@|green   | | | |__) | | | | (___   | |  | | \\  / | |        |@",
			"@|green   | | |  _  /  | |  \\___ \\  | |  | | |\\/| | |      |@",
			"@|green  _| |_| | \\ \\ _| |_ ____) | | |__| | |  | | |____   |@",
			"@|green |_____|_|  \\_\\_____|_____/  |_____/|_|  |_|\\_____| |@",
			"@|green xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx |@"};
	@Spec
	private CommandSpec spec; // injected by picocli

	@Mixin
	private VersionOption version;

	@Mixin
	private HelpOption usageHelpRequested;

	
	@Override
	public void run() {
		throw new ParameterException(spec.commandLine(), "Missing required command");
	}

	public static void main(String[] args) throws Exception {
		IExecutionExceptionHandler errorHandler = new IExecutionExceptionHandler() {
			public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) {
				commandLine.getErr().println(ex.getMessage());
				return commandLine.getCommandSpec().exitCodeOnExecutionException();
			}
		};
	    // or: String[] banner = this.spec.header();

	    for (String line : banner) {
	      System.out.println(CommandLine.Help.Ansi.AUTO.string(line));
	    }
	    System.out.println();
		System.exit(new CommandLine(new Application()).setExecutionExceptionHandler(errorHandler).execute(args));
	}

	static class ManifestVersionProvider implements picocli.CommandLine.IVersionProvider {
		public String[] getVersion() throws Exception {

			try (InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");) {
				Properties prop = new Properties();
				prop.load(resourceAsStream);

				prop.getProperty("groupId");
				prop.get("artifactId");
				prop.get("version");
				prop.get("build.date");

				return new String[] { prop.getProperty("groupId") + " >> " + prop.getProperty("version") + " ["
						+ prop.getProperty("build.date") + "]" };

			} catch (IOException ex) {
				return new String[] { "Unable to read from version: " + ex };
			}
		}
	}
}
