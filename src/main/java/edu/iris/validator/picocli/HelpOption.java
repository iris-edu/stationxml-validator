package edu.iris.validator.picocli;

import picocli.CommandLine.Option;

public class HelpOption {
	@Option(names = {  "-h", "--help" }, usageHelp = true, description = "display this help message")
	private boolean usageHelpRequested;
}
