package edu.iris.dmc.station.conditions;

import java.util.List;
import java.util.logging.Logger;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class DecimationStageGainCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(DecimationStageGainCondition.class.getName());

	public DecimationStageGainCondition(boolean required, String description, Restriction[] restrictions) {
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
		return this.evaluate(channel,channel.getResponse());
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
			List<ResponseStage> stages = response.getStage();

			for (ResponseStage s : stages) {
				if (s.getDecimation() != null && s.getStageGain() != null) { 
				if (s.getCoefficients() == null && s.getPolesZeros() == null && s.getFIR()==null && s.getResponseList()==null) {
					if (s.getNumber().intValue() < 10 ) {
					    nestedMessage.add(Result.error("Stage[" + String.format("%02d", s.getNumber().intValue())
					    + "] includes Decimation and StageGain but does not include a filter"));
					    returnmessage =true;
					}else {									    
						nestedMessage.add(Result.error("Stage[" + s.getNumber().intValue()
				        + "] includes Decimation and StageGain but does not include a filter"));
				        returnmessage =true;	
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
