package edu.iris.dmc.station.conditions;

import edu.iris.dmc.TimeUtil;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
		if (network == null) {
			throw new IllegalArgumentException("Network cannot be null.");
		}

		if (network.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}
		if (network.getStations() != null) {
			for (Station s : network.getStations()) {
				if (network.getEndDate() != null && s.getEndDate() == null) {
					nestedMessage.add(Result.error("Sta: " + s.getCode()+" endDate cannot be null"
					+ " if network endDate is defined as: " + XmlUtil.toText(network.getEndDate())));
					returnmessage = true;
				}
				if (s.getStartDate() != null && TimeUtil.isBefore(s.getStartDate(), network.getStartDate())) {
					nestedMessage.add(Result.error("Sta: " + s.getCode()+" startDate " + XmlUtil.toText(s.getStartDate())
					+ " cannot occur before network startDate " + XmlUtil.toText(network.getStartDate())));
					returnmessage = true;
				}
				if (network.getEndDate() != null && s.getEndDate() != null) {
					if (TimeUtil.isAfter(s.getEndDate(), network.getEndDate())) {
						nestedMessage.add(Result.error("Sta: " + s.getCode()+" endDate " + XmlUtil.toText(s.getEndDate())
								+ " cannot occur after network endDate " + XmlUtil.toText(network.getEndDate())));
						returnmessage = true;
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

	@Override
	public Message evaluate(Station station) {
		NestedMessage nestedStaMessage = new NestedMessage();
		boolean returnStaMessage = false;
		if (station == null) {
			throw new IllegalArgumentException("Station cannot be null.");
		}

		if (station.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}

		if (station.getChannels() != null) {
			for (Channel c : station.getChannels()) {
				if (station.getEndDate() != null && c.getEndDate() == null) {
					nestedStaMessage.add(Result.error("Chan: " + c.getCode() + " Loc: "+c.getLocationCode()+ " endDate cannot be null"
						+ " if station endDate is defined as: " + XmlUtil.toText(station.getEndDate())));
					returnStaMessage = true;

				}
				if (c.getStartDate() != null && TimeUtil.isBefore(c.getStartDate(), station.getStartDate())) {
					nestedStaMessage.add(Result.error("Chan: " + c.getCode() + " Loc: "+c.getLocationCode()+" startDate " + XmlUtil.toText(c.getStartDate())
							+ " cannot occur before Station startDate " + XmlUtil.toText(station.getStartDate())));
					returnStaMessage = true;
				}
				if (station.getEndDate() != null && c.getEndDate() != null) {
					if (TimeUtil.isAfter(c.getEndDate(), station.getEndDate())) {
						nestedStaMessage.add(Result.error("Chan: "+ c.getCode() +" Loc: "+c.getLocationCode()+" endDate " + XmlUtil.toText(c.getEndDate())
								+ " cannot occur after Station endDate " + XmlUtil.toText(station.getEndDate())));
						returnStaMessage = true;
					}
				}
			}
		}
		if(returnStaMessage==true) {
			return nestedStaMessage;
		}else {
		    return Result.success();
		}
	}

	@Override
	public Message evaluate(Channel channel) {
		return null;
	}

}
