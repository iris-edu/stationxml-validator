package edu.iris.dmc.station.conditions;


import edu.iris.seed.lang.time.TimeUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.AbstractCondition;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;


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
				if (network.getEndDate() != null && s.getEndDate() == null) {
					return Result.error("Station endDate cannot be null"
					+ " if network endDate is defined as: " + TimeUtils.toString(network.getEndDate()));
				}
				if (s.getStartDate() != null && TimeUtils.isBefore(s.getStartDate(), network.getStartDate())) {
					return Result.error("Station startDate " + TimeUtils.toString(s.getStartDate())
							+ " cannot occur before network startDate " + TimeUtils.toString(network.getStartDate()));
				}
				if (network.getEndDate() != null && s.getEndDate() != null) {
					if (TimeUtils.isAfter(s.getEndDate(), network.getEndDate())) {
						return Result.error("Station endDate " + TimeUtils.toString(s.getEndDate())
								+ " cannot occur after network endDate " + TimeUtils.toString(network.getEndDate()));
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

				if (station.getEndDate() != null && c.getEndDate() == null) {
					return Result.error("Channel endDate cannot be null"
						+ " if station endDate is defined as: " + TimeUtils.toString(station.getEndDate()));
				}
				if (c.getStartDate() != null && TimeUtils.isBefore(c.getStartDate(), station.getStartDate())) {
					return Result.error("Channel startDate " + TimeUtils.toString(c.getStartDate())
							+ " cannot occur before Station startDate " + TimeUtils.toString(station.getStartDate()));
				}
				if (station.getEndDate() != null && c.getEndDate() != null) {
					if (TimeUtils.isAfter(c.getEndDate(), station.getEndDate())) {
						return Result.error("Channel endDate " + TimeUtils.toString(c.getEndDate())
								+ " cannot occur after Station endDate " + TimeUtils.toString(station.getEndDate()));
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
