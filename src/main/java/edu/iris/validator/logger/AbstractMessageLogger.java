package edu.iris.validator.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public abstract class AbstractMessageLogger implements MessageLogger {
	protected OutputStreamWriter writer;

	AbstractMessageLogger(OutputStream outputstream) {
		this.writer = new OutputStreamWriter(outputstream);
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

}
