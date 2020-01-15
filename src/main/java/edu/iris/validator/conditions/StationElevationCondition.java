package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Distance;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class StationElevationCondition extends AbstractCondition {
	public StationElevationCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public String toString() {
		return "CodeRule [description=" + description + ", required=" + required + " ]";
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported for network.");
	}

	@Override
	public Message evaluate(Station station) {
		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.success();
		}
		Distance distance = station.getElevation();
		if (distance == null) {
			return Result.success();
		}
		if (station.getElevation() == null) {
			return Result.success();
		}
		for (Channel channel : station.getChannels()) {
			if (channel.getElevation() != null) {
				if (Math.abs(channel.getElevation().getValue() - station.getElevation().getValue()) > 1000) {
					return Result.error("expected " + station.getCode() + " elevation "
							+ station.getElevation().getValue() + " to be equal to or above " + channel.getCode() + ":"
							+ channel.getLocationCode() + " elevation " + channel.getElevation().getValue());
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("Not supported for channel.");
	}

}
