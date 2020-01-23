package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Station;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;

public class PolynomialCondition extends ChannelRestrictedCondition {

	public PolynomialCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
	}
	
	@Override
	public String result() {
		return "Error";
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (channel == null) {
			return Result.success();
		}
		return this.evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}

		int index=1;
		for (ResponseStage stage : response.getStage()) {
			if (stage.getPolynomial() != null) {
				if (response.getInstrumentPolynomial() == null) {
					//Stage [N] polynomial requires that an InstrumentPolynomial be included
					return Result.error("Stage ["+index+"] polynomial requires that an InstrumentPolynomial be included");
				}
			}
			index++;
		}
		return Result.success();
	}

}
