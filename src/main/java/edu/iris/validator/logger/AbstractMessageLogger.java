package edu.iris.validator.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public abstract class AbstractMessageLogger implements MessageLogger {
	//protected OutputStreamWriter writer;

	private PrintWriter w;

	AbstractMessageLogger(OutputStream outputstream) {
		//this.writer = new OutputStreamWriter(outputstream);
		this.w = new PrintWriter(outputstream);
	}

	public void newLine() {
		this.w.println();
	}

	public void header(String header) throws IOException {
		log(header);
		newLine();
	}

	public void log(String message) throws IOException {
		w.write(message);
	}
	
	@Override
	public void close() throws IOException {
		w.close();
	}

	@Override
	public void flush() throws IOException {
		if (w.checkError()) {
			throw new IOException("Unkown IO/Error");
		}
	}

}
