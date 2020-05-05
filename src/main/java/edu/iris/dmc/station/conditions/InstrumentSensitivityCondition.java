package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class InstrumentSensitivityCondition extends ChannelRestrictedCondition {

	public InstrumentSensitivityCondition(boolean required, String description, Restriction... restrictions) {
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

		// This probably needs to be a list
		int index=1;
		boolean failAfterStages = false;
		for (ResponseStage stage : response.getStage()) {
			if(response.getInstrumentSensitivity()==null) {
			    if (stage.getPolynomial() != null) {
			    	if(response.getInstrumentPolynomial() != null) {
			    		return Result.success();
			    	}else {}
			    }else {
			    	// Add a boolean trigger that forces a failure after all stages are checked. 
			    	failAfterStages = true;
				}
			}
			index++;
		}
		if(failAfterStages==true) {
			return Result.error("InstrumentSensitivity must be included for a non-polynomial responses.");

		}else {
		return Result.success();
	}
  } 
}
