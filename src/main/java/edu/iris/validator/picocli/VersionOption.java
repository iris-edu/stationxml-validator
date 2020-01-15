package edu.iris.validator.picocli;

import picocli.CommandLine.Option;

public class VersionOption {
	@Option(names = { "-V", "--version" }, versionHelp = true, description = "display version info")
	private boolean versionInfoRequested;
}
