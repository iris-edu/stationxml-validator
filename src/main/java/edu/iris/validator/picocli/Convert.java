package edu.iris.validator.picocli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iris.validator.Application;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

@Command(name = "convert", description = "Station metadata validation.")
public class Convert implements  Runnable {

	
	private static final Logger logger = LoggerFactory.getLogger(Convert.class);
	@ParentCommand
	private Application parent;
	@Spec
	private CommandSpec spec; // injected by picocli

	@Mixin
	private VersionOption version;


	@Mixin
	private HelpOption usageHelpRequested;

	;

	
	@Override
	public void run(){
		//throw new Exception("Hello");
		throw new RuntimeException("Hello");

		//return 3;
	}
	
}
