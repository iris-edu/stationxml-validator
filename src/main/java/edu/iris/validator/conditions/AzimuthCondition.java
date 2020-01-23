package edu.iris.validator.conditions;

import edu.iris.station.model.Azimuth;
import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class AzimuthCondition extends AbstractCondition {

	private double min;
	private double max;

	public AzimuthCondition(boolean required, String description, double min, double max) {
		super(required, description);
		this.min = min;
		this.max = max;
	}
	
	@Override
	public String result() {
		return "Error";
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
		Azimuth azimuth = channel.getAzimuth();
		if (azimuth == null) {
			if (required) {
				return Result.error("Expected an Azimuth value between " + min + " and " + max + " but received null.");
			}
			return Result.success();
		}
		return Result.success();
	}
}
