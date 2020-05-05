package edu.iris.dmc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

import edu.iris.dmc.CommandLine;
import edu.iris.dmc.CommandLineParseException;

public class CommandLine {

	private Path file;
	private Path input;
	private Path output;
	private boolean ignoreWarnings;
	private int[] ignoreRules;
	private boolean showRules;
	private boolean showUnits;
	private String format;
	private boolean summary;
	private boolean showHelp;
	private boolean continueError;
	private boolean showVersion;

	private Level logLevel = Level.WARNING;

	private Map<String, String> map = new HashMap<>();

	public Path input() {
		return input;
	}

	public CommandLine setInput(Path input)	{
		this.input = input;
		return this;
	}

	public Path output() {
		return output;
	}

	public CommandLine setOutput(Path output) {
		this.output = output;
		return this;
	}

	public boolean showHelp() {
		return showHelp;
	}

	public boolean showVersion() {
		return showVersion;
	}

	public boolean ignoreWarnings() {
		return ignoreWarnings;
	}

	public CommandLine setIgnoreWarnings(boolean b) {
		this.ignoreWarnings = b;
		return this;
	}

	public int[] ignoreRules() {
		return ignoreRules;
	}

	public CommandLine setIgnoreRules(int[] rules) {
		this.ignoreRules = rules;
		return this;
	}

	public boolean showRules() {
		return showRules;
	}

	public CommandLine setShowRules(boolean b) {
		this.showRules = b;
		return this;
	}

	public boolean continueError() {
		return continueError;
	}

	public CommandLine setContinueError(boolean b) {
		this.continueError = b;
		return this;
	}

	public boolean showUnits() {
		return showUnits;
	}

	public CommandLine setShowUnits(boolean b) {
		this.showUnits = b;
		return this;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public CommandLine setLogLevel(Level level) {
		this.logLevel = logLevel;
		return this;
	}

	public static CommandLine parse(String[] args) throws CommandLineParseException {
		CommandLine commandLine = new CommandLine();

		if (args == null || args.length == 0) {
			throw new CommandLineParseException("Application arguments cannot be empty or null!");

		}
		// look for showHelp or showVersion flags
		if (args.length == 1) {
			if ("--help".equalsIgnoreCase(args[0]) || "--showhelp".equalsIgnoreCase(args[0])
					|| "-h".equalsIgnoreCase(args[0])) {
				commandLine.showHelp = true;
				return commandLine;
			} else if ("--version".equalsIgnoreCase(args[0]) || "-v".equalsIgnoreCase(args[0])) {
				commandLine.showVersion = true;
				return commandLine;
			} else if ("--units".equalsIgnoreCase(args[0]) || "-u".equalsIgnoreCase(args[0])) {
				commandLine.showUnits = true;
				return commandLine;
			} else if ("--rules".equalsIgnoreCase(args[0]) || "-r".equalsIgnoreCase(args[0])) {
				commandLine.showRules = true;
				return commandLine;
			} else {
				String path = args[0];
				commandLine.input = Paths.get(path);
				if (!commandLine.input.toFile().exists()) {
					commandLine.showHelp = true;
					System.out.println(String.format("File %s does not exist!", path));
					return commandLine;

				}
			}

		}
		if (args.length < 1) {
			throw new CommandLineParseException(
					"Invalid number of arguments, expected 1 but was " + args.length + "!");
		}



		// look for logLevel
		if (args.length >= 2) {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				if ("--help".equalsIgnoreCase(arg) || "--showhelp".equalsIgnoreCase(arg)
						|| "-h".equalsIgnoreCase(arg)) {
					commandLine.showHelp = true;
				} else if ("--version".equalsIgnoreCase(arg) || "-v".equalsIgnoreCase(arg)) {
					commandLine.showVersion = true;
				} else if ("--verbose".equalsIgnoreCase(arg)) {
					commandLine.logLevel = Level.INFO;
				} else if ("--debug".equalsIgnoreCase(arg)) {
					commandLine.logLevel = Level.FINE;
				} else if ("--ignore-warnings".equalsIgnoreCase(arg)) {
					commandLine.ignoreWarnings = true;
				} else if ("--ignore-rules".equalsIgnoreCase(arg)) {
					if(args.length < (i+2)) {
						throw new CommandLineParseException(String.format("Please provide rules to ignore."));
					}else {
					    String rules = args[i + 1];
					    commandLine.ignoreRules = Stream.of(rules.split("\\s*,\\s*")).map(String::trim)
							    .map(Integer::parseInt).mapToInt(item -> item).toArray();
					    i = i + 1;
				    }
				} else if ("--show-rules".equalsIgnoreCase(arg)) {
					commandLine.showRules = true;
				} else if ("--show-units".equalsIgnoreCase(arg)) {
					commandLine.showUnits = true;
				} else if ("--continue-on-error".equalsIgnoreCase(arg)) {
					commandLine.continueError = true;
				}  else if ("--output".equalsIgnoreCase(arg) || "-o".equalsIgnoreCase(arg)) {
					if(args.length < (i+2)) {
						throw new CommandLineParseException(String.format("Please provide an argument for --output."));
					}else {
					    commandLine.output = Paths.get(args[i + 1]);
					i = i + 1;
					}
				}else if ("--input".equalsIgnoreCase(arg) || "-i".equalsIgnoreCase(arg)) {
					if(args.length < (i+2)) {
						throw new CommandLineParseException(String.format("Please provide an argument for --file."));
					}else {
					    String path = args[i+1];
					    commandLine.input = Paths.get(path);
					    i = i + 1;
					    if (!commandLine.input.toFile().exists()) {
						throw new CommandLineParseException(String.format("File %s does not exist!", path));
					   }
					}
				}else {
					String path = args[i];
					commandLine.input = Paths.get(path);
					if (!commandLine.input.toFile().exists()) {
						throw new CommandLineParseException(String.format("File %s does not exist!", path));
					}
				}

			}
		}

		return commandLine;
	}

}
