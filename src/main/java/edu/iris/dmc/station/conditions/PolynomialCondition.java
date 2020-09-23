package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class PolynomialCondition extends ChannelRestrictedCondition {

	public PolynomialCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
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
					//Stage[N] polynomial requires that an InstrumentPolynomial be included
					if (stage.getNumber().intValue() < 10 ) {
					    return Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue())
					    + "] includes a Polynomial so InstrumentPolynomial must be included");
					}else {									    
						return Result.error("Stage[" + stage.getNumber().intValue()
				        + "] includes a Polynomial so InstrumentPolynomial must be included");
					}					
				}
			}
			index++;
		}
		return Result.success();
	}

}
