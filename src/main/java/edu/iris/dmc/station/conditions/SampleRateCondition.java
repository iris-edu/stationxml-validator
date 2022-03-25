package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class SampleRateCondition extends ChannelRestrictedCondition {

	public SampleRateCondition(boolean required, String description) {
		super(required, description);
	}

	public SampleRateCondition(boolean required, String description, Restriction[] restrictions) {
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

	// "If Channel sample rate = 0, no Response should be included.
	@Override
	public Message evaluate(Channel channel) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		SampleRate sampleRate = channel.getSampleRate();

		if (sampleRate == null || sampleRate.getValue() == null || sampleRate.getValue() == 0) {
			if (channel.getResponse() != null) {
				if (sampleRate.getValue() != 0) {

				} else {
					return Result.warning("Sample rate cannot be 0 or null.");
				}
			} else {

			}
		}
		return Result.success();
	}

}
