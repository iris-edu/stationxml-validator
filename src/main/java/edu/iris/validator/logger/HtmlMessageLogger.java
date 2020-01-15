package edu.iris.validator.logger;

import java.io.OutputStream;

import edu.iris.validator.rules.Message;

public class HtmlMessageLogger extends AbstractMessageLogger {

	public HtmlMessageLogger(OutputStream outputstream) {
		super(outputstream);
	}

	@Override
	public void log(Message message) {
		message.getNetwork();
		message.getStation();
		message.getChannel();
		message.getRule();
		message.getDescription();
		message.getSource();
	}

	@Override
	public void log(String message) {
		// TODO Auto-generated method stub

	}
}
