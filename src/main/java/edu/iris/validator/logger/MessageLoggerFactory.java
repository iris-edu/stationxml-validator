package edu.iris.validator.logger;

import java.io.OutputStream;

public class MessageLoggerFactory {

	public static MessageLogger create(OutputStream outputStream, String format) {
		if ("csv".equalsIgnoreCase(format)) {
			return new CsvMessageLogger(outputStream);
		} else {
			throw new IllegalArgumentException("Error: invalid format value ["+format+"]");
		}
	}
}
