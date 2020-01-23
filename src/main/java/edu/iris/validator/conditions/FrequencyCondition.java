package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.SampleRate;
import edu.iris.station.model.Sensitivity;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class FrequencyCondition extends ChannelRestrictedCondition {

	public FrequencyCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
	}
	
	@Override
	public String result() {
		return "Warning";
	}


	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		return evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}

		Sensitivity sensitivity = channel.getResponse().getInstrumentSensitivity();
		SampleRate sampleRate = channel.getSampleRate();
		if (sensitivity != null && sensitivity.getFrequency() != null && sampleRate != null) {
			if (sensitivity.getFrequency() > (sampleRate.getValue() / 2)) {
				return Result.warning(sensitivity.getFrequency() + ">" + sampleRate.getValue() + "/2");
			}
		}
		return Result.success();
	}

}
