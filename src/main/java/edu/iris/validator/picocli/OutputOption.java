package edu.iris.validator.picocli;

import java.io.File;
import java.io.IOException;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

public class OutputOption {
	
	@Spec
	private CommandSpec spec; // injected by picocli
	
	@Option(names = { "-o", "--output" }, description = "where to output result, default is System.out")
	private File outputFile;
	
	public File getOutputFile() {
		return outputFile;
	}
	
	public void setOutputFile(File outputFile) {
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
        }
        catch (IOException exc) {
            throw new ParameterException(spec.commandLine(),
                    "A problem occurred while creating the output file. Details: " + exc.toString());
        }
        if(!outputFile.canRead()) {
            throw new ParameterException(spec.commandLine(), "Output file must be writable");
        }
        this.outputFile = outputFile;
    }
}
