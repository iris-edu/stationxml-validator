package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Response;
import edu.iris.validator.rules.Message;

public abstract class AbstractCondition implements Condition {

	protected boolean required;
	protected String description;

	public AbstractCondition(boolean required, String description) {
		this.required = required;
		this.description = description;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Message evaluate(FDSNStationXML document) {
		throw new IllegalArgumentException("method not supported");
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		throw new IllegalArgumentException("method not supported");
	}

}
