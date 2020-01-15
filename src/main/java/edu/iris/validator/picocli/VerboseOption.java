package edu.iris.validator.picocli;

import picocli.CommandLine.Option;

public class VerboseOption {
	@Option(names = { "-v", "--verbose" }, description = { "Specify multiple -v options to increase verbosity.",
	"For example, `-v -v -v` or `-vvv`" })
protected boolean[] verbosity = new boolean[0];
	
	public boolean[] value() {
		return verbosity;
	}
	
	public int length() {
		return verbosity.length;
	}
}
