package edu.iris.validator.restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Response;

public class ChannelTypeRestriction implements Restriction {

	private static final Logger LOGGER = Logger.getLogger(ChannelTypeRestriction.class.getName());
	private String name;
	private String[] types = new String[] { "HEALTH", "FLAG", "MAINTENANCE"};

	public ChannelTypeRestriction() {
		this("TypeRestriction");
	}

	public ChannelTypeRestriction(String name) {
		this.name = name;
	}

	@Override
	public boolean qualifies(Channel channel) {
		List<String> flags = channel.getType();
		if (flags == null || flags.isEmpty()) {
			return false;
		}
		// T — Channel is triggered
		// C — Channel is recorded continuously
		// H — State of health data
		// G — Geophysical data
		// W — Weather or environmental data
		// F — Flag information (nominal, not ordinal)
		// S — Synthesized data
		// I — Channel is a calibration input
		// E — Channel is experimental or temporary
		// M — Maintenance tests are underway on channel; possible abnormal data
		// B — Data are a beam synthesis
		for (String f : flags) {
			for (String t : types) {
				if (t.equalsIgnoreCase(f)) {
					return true;
				}
			}
		}
		return false;
	}
	

	public boolean qualifies(List<String> flags) {
		if (flags == null || flags.isEmpty()) {
			return false;
		}
		// T — Channel is triggered
		// C — Channel is recorded continuously
		// H — State of health data
		// G — Geophysical data
		// W — Weather or environmental data
		// F — Flag information (nominal, not ordinal)
		// S — Synthesized data
		// I — Channel is a calibration input
		// E — Channel is experimental or temporary
		// M — Maintenance tests are underway on channel; possible abnormal data
		// B — Data are a beam synthesis
		for (String f : flags) {
			for (String t : types) {
				if (t.equalsIgnoreCase(f)) {
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public boolean qualifies(Response response) {
		return false;
	}
	@Override
	public String toString() {
		return "ChannelTypeRestriction [name=" + name + ", types=" + Arrays.toString(types) + "]";
	}

}
