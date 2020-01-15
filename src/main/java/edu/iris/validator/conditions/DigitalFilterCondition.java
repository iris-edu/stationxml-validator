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

public class DigitalFilterCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(DigitalFilterCondition.class.getName());

	public DigitalFilterCondition(boolean required, String description, Restriction[] restrictions) {
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
		return this.evaluate(channel,channel.getResponse());
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

			for (ResponseStage s : stages) {
				if (s.getCoefficients() != null && "DIGITAL".equals(s.getCoefficients().getCfTransferFunctionType())) {
					if (s.getDecimation() == null || s.getStageGain() == null) {
						return Result.error("Gain and|or decimation are missing");
					}
				}

				if (s.getPolesZeros() != null
						&& "DIGITAL (Z-TRANSFORM)".equals(s.getPolesZeros().getPzTransferFunctionType())) {
					if (s.getDecimation() == null || s.getStageGain() == null) {
						return Result.error("Gain and|or decimation are missing");
					}
				}

				if (s.getFIR() != null) {
					if (s.getDecimation() == null || s.getStageGain() == null) {
						return Result.error("Gain and|or decimation are missing");
					}
				}
			}

		}
		return Result.success();
	}
}
