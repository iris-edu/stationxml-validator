package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Equipment;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

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
				return Result.error("expected equipment/sensor but was null");
			}else{
				if (equipment.getDescription() == null||equipment.getDescription().trim().isEmpty()) {
					return Result.error("expected equipment/sensor description but was null");
				}
			}
		}
		return Result.success();
	}

}
