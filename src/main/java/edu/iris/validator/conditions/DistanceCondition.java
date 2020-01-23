package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class DistanceCondition extends AbstractCondition {

	private int margin;

	public DistanceCondition(boolean required, String description, int margin) {
		super(required, description);
		this.margin = margin;
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported for network.");
	}

	@Override
	public Message evaluate(Station station) {

		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.success();
		}
		if (station.getLongitude() == null || station.getLatitude() == null) {
			return Result.warning("Expected longitude/latitude but was null!");
		}

		for (Channel channel : station.getChannels()) {
			if (channel.getLatitude() == null || channel.getLongitude() == null) {
				return Result.warning("Expected longitude/latitude but was null!");
			}
			double distance = DistanceCalculator.distance(channel.getLatitude().getValue(),
					channel.getLongitude().getValue(), station.getLatitude().getValue(),
					station.getLongitude().getValue(), "K");
			if (distance > this.margin) {
				return Result.error("Expected a distance difference of less than " + margin + " between "
						+ station.getCode() + " and " + channel.getCode() + ":" + channel.getLocationCode()
						+ " but was " + distance);
			}
		}

		return Result.success();

	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("method not supported for channel.");
	}
	
	@Override
	public String result() {
		return "Error";
	}


}
