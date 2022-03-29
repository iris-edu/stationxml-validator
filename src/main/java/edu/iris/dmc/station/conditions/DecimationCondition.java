package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class DecimationCondition extends ChannelRestrictedCondition {

	public DecimationCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
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
		if(channel==null){
			throw new IllegalArgumentException("Channel cannot be null.");
		}
		return evaluate(channel,channel.getResponse());
	}

	// The value of Channel::SampleRate must be equal to the value of
	// Decimation::InputSampleRate divided by Decimation::Factor of the final
	// response stage.

	@Override
	public Message evaluate(Channel channel, Response response) {
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
		if (isRestricted(channel)) {
			return Result.success();
		}
		List<ResponseStage> stages = response.getStage();
		if (stages == null || stages.isEmpty()) {
			Result.success();
		}
		Double inputSampleRateByFactor = null;
		int i = 1;
		for (ResponseStage stage : stages) {
			Decimation decimation = stage.getDecimation();
			if (stage.getDecimation() != null) {
				Frequency sampleRate = decimation.getInputSampleRate();
				if(sampleRate==null) {
					if (stage.getNumber().intValue() < 10 ) {
					    nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue())
					    + "] must include Decimation:Samplerate"));
					    returnmessage =true;
					}else {									    
						nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue()
				        + "] must include Decimation:Samplerate"));
				        returnmessage =true;	
					}					
				}
				double inputSampleRate = sampleRate.getValue();
				
				if (inputSampleRateByFactor != null) {
					if (Math.abs(inputSampleRate - inputSampleRateByFactor.doubleValue()) > 0.001) {
						if (stage.getNumber().intValue() < 10 ) {
						    nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue())
						    + "] Decimation:InputSampleRate "+ inputSampleRate +" != Stage["+ String.format("%02d", (stage.getNumber().intValue()-1))
						    + "] Decimation:InputSampleRate/Decimation:Factor "+inputSampleRateByFactor.doubleValue()));
						    returnmessage =true;
						}else {									    
						    nestedMessage.add(Result.error("Stage[" +  stage.getNumber().intValue()
						    + "] Decimation:InputSampleRate "+ inputSampleRate +" != Stage["+ (stage.getNumber().intValue()-1)
						    + "] Decimation:InputSampleRate/Decimation:Factor "+inputSampleRateByFactor.doubleValue()));
					        returnmessage =true;	
						}						
					}
				}
				inputSampleRateByFactor = inputSampleRate / decimation.getFactor().doubleValue();
			}
			i++;
		}
		if(returnmessage==true) {
		    return nestedMessage;
		}else {
			return Result.success();
	 	}
	}

}
