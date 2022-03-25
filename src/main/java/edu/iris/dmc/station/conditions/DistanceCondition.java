package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

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
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;

		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.success();
		}
		if (station.getLongitude() == null || station.getLatitude() == null) {
			nestedMessage.add(Result.warning("Latitude value is null"));
			returnmessage=true;
		}

		for (Channel channel : station.getChannels()) {
			if (channel.getLatitude() == null || channel.getLongitude() == null) {
				nestedMessage.add(Result.warning("Longitude value is null"));
				returnmessage=true;
			}
			double distance = DistanceCalculator.distance(channel.getLatitude().getValue(),
					channel.getLongitude().getValue(), station.getLatitude().getValue(),
					station.getLongitude().getValue(), "K");
			if (distance > this.margin) {
				nestedMessage.add(Result.warning("Distance between Sta: "
						+ station.getCode() + " and Chan: " + channel.getCode() + " Loc: " + channel.getLocationCode()
						+ " is expected to be less than " + margin + " km but is " + distance + " km"));
				returnmessage=true;
			}
		}

		if(returnmessage==true) {
			   return nestedMessage;
			}else {
			   return Result.success();
			}
	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("method not supported for channel.");
	}

}
