package edu.iris.dmc.station.conditions;

import java.util.Objects;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class OrientationConditionE extends AbstractCondition {

	private Restriction[] restrictions;

	public OrientationConditionE(boolean required, String description, Restriction[] restrictions) {
		super(required, description);
		this.restrictions = restrictions;
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
		Objects.requireNonNull(channel, "channel cannot be null");

		String code = channel.getCode();
		if (code == null) {
			return Result.success();
		}
		code = code.trim();
		if (code.isEmpty() || code.length() < 3 || channel.getAzimuth() == null || channel.getDip() == null) {
			return Result.success();
		}

		for (Restriction r : this.restrictions) {
			if (r.qualifies(channel)) {
				return Result.success();
			}
		}
		if (channel.getCode() == null || channel.getCode().trim().isEmpty() || channel.getAzimuth() == null
				|| channel.getDip() == null) {
			return Result.success();
		}

		if (channel.getAzimuth().getValue() == null || channel.getDip().getValue() == null) {
			return Result.success();
		}
		char[] array = channel.getCode().toCharArray();

		double azimuth = channel.getAzimuth().getValue();
		double dip = channel.getDip().getValue();

		boolean valid = true;
		StringBuilder messageBuilder = new StringBuilder();
		if (array.length < 3) {
			return Result.success();
		}
		if ('E' == array[2]) {
			if (azimuth < 95 && azimuth > 85 || azimuth < 275 && azimuth > 265) {

			} else {
				valid = false;
				messageBuilder.append("azimuth: ").append(azimuth).append(" ");
			}

			if (dip > 5 || dip < -5) {
				valid = false;
				messageBuilder.append("dip: ").append(dip).append(" ");
			}
		} 
	

		if (valid) {
			return Result.success();
		}
		return Result
				.warning("Invalid channel orientation: " + messageBuilder.toString() + "for " + channel.getCode());

	}
}