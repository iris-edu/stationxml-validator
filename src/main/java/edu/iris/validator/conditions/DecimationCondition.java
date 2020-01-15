package edu.iris.validator.conditions;

import java.util.List;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Decimation;
import edu.iris.station.model.Frequency;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class DecimationCondition extends ChannelRestrictedCondition {

	public DecimationCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		if(channel==null){
			throw new IllegalArgumentException("Channel cannot be null.");
		}
		return evaluate(channel,channel.getResponse());
	}

	// The value of Channel::SampleRate must be equal to the value of
	// Decimation::InputSampleRate divided by Decimation::Factor of the final
	// response stage.

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.success();
		}
		Double inputSampleRateByFactor = null;
		int i = 1;
		for (ResponseStage stage : stages) {
			Decimation decimation = stage.getDecimation();
			if (stage.getDecimation() != null) {
				Frequency sampleRate = decimation.getInputSampleRate();
				if(sampleRate==null) {
					return Result.error("expected samplerate but was null");
				}
				double inputSampleRate = sampleRate.getValue();
				
				if (inputSampleRateByFactor != null) {
					if (Math.abs(inputSampleRate - inputSampleRateByFactor.doubleValue()) > 0.001) {
						return Result.error("stage number: " + i+" inputSampleRate="+inputSampleRate+" : inputSampleRateByFactor="+inputSampleRateByFactor.doubleValue());
					}
				}
				inputSampleRateByFactor = inputSampleRate / decimation.getFactor().doubleValue();
			}
			i++;
		}

		return Result.success();
	}

}
