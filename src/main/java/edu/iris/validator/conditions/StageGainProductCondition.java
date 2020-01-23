package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Sensitivity;
import edu.iris.station.model.StageGain;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;
import edu.iris.validator.rules.Util;

public class StageGainProductCondition extends ChannelRestrictedCondition {

	public StageGainProductCondition(boolean required, String description, Restriction... restrictions) {
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
		Sensitivity sensitivity = response.getInstrumentSensitivity();
		if (sensitivity != null) {
			Double frequency = sensitivity.getFrequency();
			Double product = 1.0;

			if (response.getStage() != null && !response.getStage().isEmpty()) {
				for (ResponseStage stage : response.getStage()) {
					StageGain stageGain = stage.getStageGain();
					if (stageGain != null) {
						Double stageFrequency = stage.getStageGain().getFrequency();
						if (stageFrequency != null) {
							if (Double.compare(stageFrequency, frequency) == 0) {
								if (stageGain.getValue() != null) {
									product = product * stageGain.getValue();
								}
							}else {
								return Result.success();
							}
						}

					} else {
						return Result.success();
					}
				}

				if (!Util.equal(product, sensitivity.getValue())) {
					return Result.error("Product of stage gains " + product + " must equal total gain "
							+ response.getInstrumentSensitivity().getValue());
				}
			}
		}

		return Result.success();
	}

}
