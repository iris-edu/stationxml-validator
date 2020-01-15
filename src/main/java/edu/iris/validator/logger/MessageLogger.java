package edu.iris.validator.logger;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

import edu.iris.validator.rules.Message;

public interface MessageLogger extends Closeable, Flushable, AutoCloseable {

	public void log(Message message)throws IOException;

	public void log(String message)throws IOException;
}
