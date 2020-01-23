package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.StageGain;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class StageGainNonZeroCondition extends ChannelRestrictedCondition {

	public StageGainNonZeroCondition(boolean required, String description, Restriction... restrictions) {
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
		return this.evaluate(channel, channel.getResponse());
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
			for (ResponseStage stage : response.getStage()) {
				StageGain stageGain = stage.getStageGain();
				if (stageGain == null) {
					if (stage.getPolynomial() == null) {
						return Result.error("Stage " + stage.getNumber() + " is missing gain");
					}
				} else {
					Double stageGainValue = stageGain.getValue();
					if (stageGainValue != 0) {

					} else {
						if (stage.getPolynomial() == null) {
							return Result.error("Stage " + stage.getNumber() + " gain cannot be zero");
						}
					}
				}
			}
		}
		return Result.success();
	}
}
