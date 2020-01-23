package edu.iris.validator.conditions;

import java.util.List;
import java.util.logging.Logger;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.PoleZero;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Sensitivity;
import edu.iris.station.model.StageGain;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class PolesZerosCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(PolesZerosCondition.class.getName());

	public PolesZerosCondition(boolean required, String description, Restriction... restrictions) {
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

			Sensitivity sensitivity = response.getInstrumentSensitivity();
			boolean t = false;
			if (sensitivity == null || sensitivity.getFrequency() == 0.0) {
				t = true;
			}

			int stage = 1;
			for (ResponseStage s : stages) {
				StageGain gain = s.getStageGain();
				if (t || gain == null || gain.getFrequency() == 0) {
					if (s.getPolesZeros() != null) {
						if (s.getPolesZeros().getZero() != null) {
							for (PoleZero z : s.getPolesZeros().getZero()) {
								if (z.getReal().getValue() == 0 || z.getImaginary().getValue() == 0) {
									//"stage 2 zero at index 0 is at 0; stage 2 StageGain:Frequency cannot be 0”
									return Result.error("stage " + s.getNumber() + " zero at index " + z.getNumber()
											+ " is at 0; stage " + s.getNumber() + " StageGain:Frequency cannot be 0");
								}
							}
						}
					}
				}
				stage++;
			}

		}

		return Result.success();
	}
}
