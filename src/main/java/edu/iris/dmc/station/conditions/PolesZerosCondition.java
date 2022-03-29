package edu.iris.dmc.station.conditions;

import java.util.List;
import java.util.logging.Logger;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.PoleZero;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.StageGain;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class PolesZerosCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(PolesZerosCondition.class.getName());

	public PolesZerosCondition(boolean required, String description, Restriction... restrictions) {
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
				return Result.error("Expected a response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			List<ResponseStage> stages = response.getStage();

			Sensitivity sensitivity = response.getInstrumentSensitivity();
			boolean t = false;
			// Deals with instrument sensitivity
			if (sensitivity == null || sensitivity.getFrequency() == 0.0) {
				t = true;
			}

			int stage = 1;
			for (ResponseStage s : stages) {
				StageGain gain = s.getStageGain();
				if (t || gain == null || gain.getFrequency() == 0) {
					if (s.getPolesZeros() != null) {
						if (s.getPolesZeros().getZero() != null) {
							for (PoleZero z : s.getPolesZeros().getZero()) {
								if (z.getReal().getValue() == 0 && z.getImaginary().getValue() == 0) {
									//"stage 2 zero at index 0 is at 0; stage 2 StageGain:Frequency cannot be 0”
									
									if(t) {
									    if (s.getNumber().intValue() < 10 ) {
										    nestedMessage.add(Result.error("Stage[" + String.format("%02d", s.getNumber().intValue())
												+ "] Zero:number[" + z.getNumber() +"] Zero:Real==0 and Zero:Imaginary==0 InstrumentSensitivity:Frequency must not equal 0"));
										    returnmessage =true;

									    }else {
										    nestedMessage.add(Result.error("Stage[" + s.getNumber().intValue()
										    + "] Zero:number[" + z.getNumber() +"] Zero:Real==0 and Zero:Imaginary==0 InstrumentSensitivity:Frequency must not equal 0"));
										    returnmessage =true;
									    }	
									}else {	
									    if (s.getNumber().intValue() < 10 ) {
										    nestedMessage.add(Result.error("Stage[" + String.format("%02d", s.getNumber().intValue())
												+ "] Zero:number[" + z.getNumber() +"] Zero:Real==0 and Zero:Imaginary==0 StageGain:Frequency must not equal 0"));
										    returnmessage =true;

									    }else {
										    nestedMessage.add(Result.error("Stage[" + s.getNumber().intValue()
										    + "] Zero:number[" + z.getNumber() +"] Zero:Real==0 and Zero:Imaginary==0 StageGain:Frequency must not equal 0"));
										    returnmessage =true;
										}
									}
									
								}
							}
						}
					}
				}
				stage++;
			}

		}
		if(returnmessage==true) {
			   return nestedMessage;
			}else {
			   return Result.success();
			}
	}
}
