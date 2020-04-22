package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Distance;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

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
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
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
				double elevdelta = Math.abs(channel.getElevation().getValue() - station.getElevation().getValue());
				if (elevdelta > 1000) {
					nestedMessage.add(Result.error("Elevation between Sta: " + station.getCode() + " and Chan: "
						    + channel.getCode() + " Loc: "+ channel.getLocationCode() + " is expected to be less than 1000 m but is "
						    		+  elevdelta + " m"));
					returnmessage=true;
					
				}
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
		throw new IllegalArgumentException("Not supported for channel.");
	}

}
