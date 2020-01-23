package edu.iris.validator.conditions;

import java.util.List;
import java.util.logging.Logger;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class StageSequenceCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(StageSequenceCondition.class.getName());

	public StageSequenceCondition(boolean required, String description, Restriction[] restrictions) {
		super(required, description, restrictions);
	}
	
	@Override
	public String result() {
		return "Error";
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
		if (channel == null) {
			return Result.success();
		}
		return evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			List<ResponseStage> stages = response.getStage();
			ResponseStage stage = stages.get(stages.size() - 1);
			if (stage.getNumber().intValue() == stages.size() - 1) {
				return Result.error("invalid stage sequence number " + stage.getNumber().intValue());
			} else {
				int i = 1;
				for (ResponseStage s : stages) {
					if (s.getNumber().intValue() != i) {
						return Result.error("invalid stage sequence number " + s.getNumber().intValue() + " expected: " + i);
					}
					i++;
				}
			}
		}
		return Result.success();
	}
}
