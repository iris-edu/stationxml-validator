package edu.iris.dmc.station.conditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class LocationCodeCondition extends AbstractCondition {

	private String regex;

	public LocationCodeCondition(boolean required, String regex, String description) {
		super(required, description);
		this.regex = regex;
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
		String code = channel.getLocationCode();
		if (code == null) {
			if (!required) {
				return Result.success();
			}
			return Result.error("Expected a value like " + this.regex + " but was null.");
		}

		Pattern p = Pattern.compile(this.regex);
		Matcher m = p.matcher(code);

		if (!m.matches()) {
			return Result.error( "Expected a value like " + this.regex + " but was " + code);
		}
		
		if(this.regex=="-") {
		    return Result.error( "Expected a value like " + this.regex + " but was " + code);
		}
		return Result.success();
	}

}
