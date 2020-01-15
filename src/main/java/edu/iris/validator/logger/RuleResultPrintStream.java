package edu.iris.validator.logger;

import java.io.IOException;

import edu.iris.validator.rules.Message;

public interface RuleResultPrintStream extends AutoCloseable {
	public void println(String line);

	public void printHeader() throws IOException;

	public void printHeader(String text) throws IOException;

	public void printFooter() throws IOException;

	public void print(Message message) throws IOException;

	public void printRow(String text) throws IOException;

	public void printMessage(String text) throws IOException;

	public void flush() throws IOException;
}
