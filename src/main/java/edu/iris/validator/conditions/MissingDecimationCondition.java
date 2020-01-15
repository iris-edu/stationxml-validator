package edu.iris.validator.conditions;

import java.util.List;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class MissingDecimationCondition extends ChannelRestrictedCondition {

	public MissingDecimationCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
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
		if(channel==null) {
			
		}
		return evaluate(channel,channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			return Result.success();
		}

		for (ResponseStage stage : stages) {
			if (stage.getDecimation() != null) {
				return Result.success();
			}
		}
		return Result.warning("No decimation found");
	}
}
