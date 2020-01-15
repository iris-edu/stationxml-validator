package edu.iris.validator.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class XmlPrintStream extends PrintStream implements RuleResultPrintStream {

	public XmlPrintStream(OutputStream out) {
		super(out);
	}

	public void println(Result result) {

	}

	@Override
	public void printHeader() {
		// TODO Auto-generated method stub

	}

	public void printHeader(String text) throws IOException {
		printHeader();
	}
	@Override
	public void printFooter() {
		// TODO Auto-generated method stub

	}

	public void print(Message result) {

	}


	@Override
	public void printRow(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printMessage(String text) throws IOException {
		// TODO Auto-generated method stub

	}

}
