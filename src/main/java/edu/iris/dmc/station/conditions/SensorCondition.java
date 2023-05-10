package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Equipment;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class SensorCondition extends AbstractCondition {

	public SensorCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported for network.");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("method not supported for station.");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (channel == null) {
			return Result.success();
		}

		Equipment equipment = channel.getSensor();
        if (this.required) {
			if (equipment == null) {
				return Result.warning("expected equipment/sensor but was null");
			}else{
				if (equipment.getDescription() == null||equipment.getDescription().trim().isEmpty()) {
					return Result.warning("expected equipment/sensor description but was null");
				}
			}
		}
		return Result.success();
	}

}
