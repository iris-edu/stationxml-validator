package edu.iris.dmc.station.conditions;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.PoleZero;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.StageGain;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class PolesZerosSequenceCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(PolesZerosSequenceCondition.class.getName());

	public PolesZerosSequenceCondition(boolean required, String description, Restriction... restrictions) {
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
		int zeroInc = 0;
		int poleInc = 0;
		if (isRestricted(channel)) {
			return Result.success();
		}
		if (this.required) {
			if (response == null) {
				return Result.error("Expected a response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			List<ResponseStage> stages = response.getStage();

			for (ResponseStage s : stages) {
				StageGain gain = s.getStageGain();
					if (s.getPolesZeros() != null) {
						if (s.getPolesZeros().getZero() != null) {
							zeroInc = 0;
							for (PoleZero z : s.getPolesZeros().getZero()) {
								if (z.getNumber().compareTo(BigInteger.valueOf(zeroInc))!=0) {
									if (s.getNumber().intValue() < 10 ) {
									    nestedMessage.add(Result.error("Stage[" + String.format("%02d", s.getNumber().intValue())
									    + "] Zero:number "+z.getNumber()+ " is out of sequence " +zeroInc+ " is expected"));
									    returnmessage =true;
									}else {									    
										nestedMessage.add(Result.error("Stage[" + s.getNumber().intValue()
								        + "] Zero:number "+z.getNumber()+ " is out of sequence "+zeroInc+ " is expected"));
								        returnmessage =true;
										
									}
								}
							    zeroInc  = zeroInc+ 1;
							}
					}
						if (s.getPolesZeros().getPole() != null) {
							poleInc=0;
							for (PoleZero p : s.getPolesZeros().getPole()) {
								if (p.getNumber().compareTo(BigInteger.valueOf(poleInc))!=0) {
									if (s.getNumber().intValue() < 10 ) {
									    nestedMessage.add(Result.error("Stage[" + String.format("%02d", s.getNumber().intValue())
									    + "] Pole:number "+p.getNumber()+ " is out of sequence " +poleInc+ " is expected"));
									    returnmessage =true;
									}else {									    
										nestedMessage.add(Result.error("Stage[" + s.getNumber().intValue()
								        + "] Pole:number "+p.getNumber()+ " is out of sequence " +poleInc+ " is expected"));
								        returnmessage =true;	
									}
								}
								 poleInc  = poleInc+ 1;

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
