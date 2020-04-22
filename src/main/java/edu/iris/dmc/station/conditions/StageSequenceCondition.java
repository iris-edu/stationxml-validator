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

public class StageSequenceCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(StageSequenceCondition.class.getName());

	public StageSequenceCondition(boolean required, String description, Restriction[] restrictions) {
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
		return evaluate(channel, channel.getResponse());
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
			ResponseStage stage = stages.get(stages.size() - 1);
			if (stage.getNumber().intValue() == stages.size() - 1) {
				nestedMessage.add(Result.error("Invalid stage sequence number " + stage.getNumber().intValue()));
				returnmessage=true;
			} else {
				int i = 1;
				for (ResponseStage s : stages) {
					if (s.getNumber().intValue() != i) {
						    nestedMessage.add(Result.error("Stage sequence number [" + s.getNumber().intValue() + "] is invalid [" +i+"] is expected"));
						returnmessage=true;
					}
					i++;
				}
			}
		}
		if(returnmessage==true) {
			   return nestedMessage;
			}else {
			   return Result.success();
			}	}
}
