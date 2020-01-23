package edu.iris.validator.conditions;

import java.math.BigInteger;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Decimation;
import edu.iris.station.model.Frequency;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.SampleRate;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class DecimationSampleRateCondition extends ChannelRestrictedCondition {

	public DecimationSampleRateCondition(boolean required, String description, Restriction... restrictions) {
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
		if(channel==null){
			throw new IllegalArgumentException("Channel cannot be null.");
		}
		return evaluate(channel,channel.getResponse());
	}
	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		SampleRate sampleRate = channel.getSampleRate();

		if(sampleRate==null){
			return Result.warning("Expected samplerate but was null");
		}

		Decimation decimation = null;
		for (ResponseStage stage : response.getStage()) {
			if (stage.getDecimation() != null) {
				decimation = stage.getDecimation();
			}
		}
		if (decimation == null) {
			return Result.warning("Decimation cannot be null");
		}

		Frequency frequence = decimation.getInputSampleRate();
		BigInteger factor = decimation.getFactor();

		if (frequence == null) {
			return Result.error("frequency is null");
		}
		if (Math.abs(sampleRate.getValue() - (frequence.getValue() / factor.doubleValue())) > 0.0001) {
			return Result.error("samplerate: "+sampleRate.getValue() + "!= frequency/factor " + (frequence.getValue() / factor.doubleValue()));
		}

		return Result.success();
	}
	
}
