package edu.iris.validator.conditions;

import edu.iris.station.model.BaseNodeType;
import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class StartTimeCondition extends AbstractCondition {

	public StartTimeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		return check(network);
	}

	@Override
	public Message evaluate(Station station) {
		return check(station);
	}

	@Override
	public Message evaluate(Channel channel) {
		return check(channel);
	}

	public Message check(BaseNodeType node) {
		if (node == null) {
			throw new IllegalArgumentException("Node cannot be null");
		}

		if (node.getEndDate() == null) {
			return Result.success();
		}

		if (node.getStartDate() == null) {
			if (node instanceof Network) {
				return Result.success();
			} else {
				return Result.error("startDate is required for node:" + node.getCode());
			}
		}

		if (node.getEndDate() != null && node.getStartDate() != null) {
			if (!node.getStartDate().isBefore(node.getEndDate())) {
				return Result.error(
						"End Time " + node.getEndDate() + " should be greater than Start Time " + node.getStartDate());
			}
		}
		return Result.success();
	}
}
