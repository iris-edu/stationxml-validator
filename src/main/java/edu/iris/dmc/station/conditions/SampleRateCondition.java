package edu.iris.dmc.station.conditions;

import java.math.BigInteger;
import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Result;

public class SampleRateCondition extends AbstractCondition {

	public SampleRateCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Channel channel) {
		SampleRate sampleRate = channel.getSampleRate();
		Response response = channel.getResponse();
		if (sampleRate == null || sampleRate.getValue() == 0) {
			if (response != null) {
				return Result.of(false, "Sample rate cannot be 0 or null.");
			} else {

			}
		} else {
			if (response == null) {
				return Result.of(false,
						"response cannot be null.");
			} else {
				List<ResponseStage> stages = channel.getResponse().getStage();
				if (stages == null || stages.isEmpty()) {
					return Result.of(false,
							"Response has no stages");
				}
				Decimation decimation = null;
				for (ResponseStage stage : stages) {
					if (stage.getDecimation() != null) {
						decimation = stage.getDecimation();
					}
				}
				if (decimation == null) {
					return Result.of(false,
							"Decimation cannot be null");
				}
			}
			if (response.getInstrumentPolynomial() == null && response.getInstrumentSensitivity() == null) {
				return Result.of(false,
						"If Channel sample rate > 0, total instrument response must exist as either or.");
			}

		}
		return Result.of(true, null);
	}
}
