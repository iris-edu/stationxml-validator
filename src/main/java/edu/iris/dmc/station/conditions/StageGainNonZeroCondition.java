package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.StageGain;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class StageGainNonZeroCondition extends ChannelRestrictedCondition {

	public StageGainNonZeroCondition(boolean required, String description, Restriction... restrictions) {
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
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
		if (isRestricted(channel)) {
			return Result.success();
		}

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			for (ResponseStage stage : response.getStage()) {
				StageGain stageGain = stage.getStageGain();
				if (stageGain == null) {
					if (stage.getPolynomial() == null) {
						if (stage.getNumber().intValue() < 10 ) {
						    nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue())
						    + "] must include StageGain"));
						    returnmessage =true;
						}else {									    
							nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue()
					        + "] must include StageGain"));
					        returnmessage =true;	
						}						
					}
				} else {
					Double stageGainValue = stageGain.getValue();
					if (stageGainValue != 0) {

					} else {
						if (stage.getPolynomial() == null) {
							if (stage.getNumber().intValue() < 10 ) {
							    nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue())
							    + "] StageGain:Value must not be 0"));
							    returnmessage =true;
							}else {									    
								nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue()
						        + "] StageGain:Value must not be 0"));
						        returnmessage =true;	
							}
							
						}
					}
				}
			}
		}
		if(returnmessage==true) {
		    return nestedMessage;
		}else {
			return Result.success();
	 	}
	}
}
