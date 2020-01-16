package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;
import edu.iris.validator.rules.XmlUtil;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		if (network == null) {
			throw new IllegalArgumentException("Network cannot be null.");
		}

		if (network.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}

		if (network.getStations() != null) {
			for (Station s : network.getStations()) {
				if (s.getStartDate() != null && network.getStartDate() != null
						&& s.getStartDate().isBefore(network.getStartDate())) {

					return Result.error("Network Start Time " + XmlUtil.toText(network.getStartDate())
							+ " should be greater than Station Start Time " + XmlUtil.toText(s.getStartDate()));
				}
				if (network.getEndDate() != null && s.getEndDate() != null) {
					if (s.getEndDate().isAfter(network.getEndDate())) {

						return Result.error("Network End Time " + XmlUtil.toText(network.getEndDate())
								+ " should be greater than Station End Time " + XmlUtil.toText(s.getEndDate()));
					}
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Station station) {
		if (station == null) {
			throw new IllegalArgumentException("Station cannot be null.");
		}

		if (station.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}

		if (station.getChannels() != null) {
			for (Channel c : station.getChannels()) {
				if (c.getStartDate() != null && station.getStartDate() != null
						&& c.getStartDate().isBefore(station.getStartDate())) {
					return Result.error("Channel startDate " + XmlUtil.toText(c.getStartDate())
							+ " cannot occur before Station startDate " + XmlUtil.toText(station.getStartDate()));
				}
				if (station.getEndDate() != null && c.getEndDate() != null) {
					if (c.getEndDate().isAfter(station.getEndDate())) {
						return Result.error("Channel endDate " + XmlUtil.toText(c.getEndDate())
								+ " cannot occur after Station endDate " + XmlUtil.toText(station.getEndDate()));
					}
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Channel channel) {
		return null;
	}

}
