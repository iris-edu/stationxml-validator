package edu.iris.validator.logger;

import java.io.IOException;
import java.io.OutputStream;

import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.XmlUtil;

public class CsvMessageLogger extends AbstractMessageLogger {

	public CsvMessageLogger(OutputStream outputstream) {
		super(outputstream);
	}

	
	@Override
	public void log(Message message) throws IOException {
		StringBuilder b = new StringBuilder();
		b.append(message.getRule() == null ? "" : message.getRule().getId()).append(",");
		b.append(message.getNetwork() == null ? "" : message.getNetwork().getCode()).append(",");
		b.append(message.getStation() == null ? "" : message.getStation().getCode()).append(",");

		b.append(message.getChannel() == null ? "" : message.getChannel().getCode()).append(",");
		b.append(message.getChannel() == null ? "" : message.getChannel().getLocationCode()).append(",");

		if (message.getChannel() != null) {
			b.append(message.getChannel() == null ? null : XmlUtil.toText(message.getChannel().getStartDate()))
					.append(",");
			b.append(message.getChannel() == null ? null : XmlUtil.toText(message.getChannel().getEndDate()))
					.append(",");
		} else if (message.getStation() != null) {
			b.append(message.getStation() == null ? null : XmlUtil.toText(message.getStation().getStartDate()))
					.append(",");
			b.append(message.getStation() == null ? null : XmlUtil.toText(message.getStation().getEndDate()))
					.append(",");
		} else if (message.getNetwork() != null) {
			b.append(message.getNetwork() == null ? null : XmlUtil.toText(message.getNetwork().getStartDate()))
					.append(",");
			b.append(message.getNetwork() == null ? null : XmlUtil.toText(message.getNetwork().getEndDate()))
					.append(",");
		}

		b.append(message.getDescription() == null ? "" : message.getDescription()).append(",");
		b.append(message.getSource() == null ? "" : message.getSource()).append(System.lineSeparator());

		log(b.toString());

	}

	@Override
	public void log(String message) throws IOException {
		writer.write(message);
	}

}
