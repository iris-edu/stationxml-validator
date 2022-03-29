package edu.iris.dmc.station.conditions;

import java.math.BigInteger;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Frequency;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class DecimationSampleRateCondition extends ChannelRestrictedCondition {

	public DecimationSampleRateCondition(boolean required, String description, Restriction... restrictions) {
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
		if(channel==null){
			throw new IllegalArgumentException("Channel cannot be null.");
		}
		return evaluate(channel,channel.getResponse());
	}
	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		SampleRate sampleRate = channel.getSampleRate();

		if(sampleRate==null){
			return Result.warning("Expected samplerate but was null");
		}

		Decimation decimation = null;
		int stageindex = 0;
		for (ResponseStage stage : response.getStage()) {
			if (stage.getDecimation() != null) {
				decimation = stage.getDecimation();
			}
			stageindex = stageindex+1;
		}
		if (decimation == null) {
			return Result.warning("Decimation cannot be null");
		}

		Frequency frequence = decimation.getInputSampleRate();
		BigInteger factor = decimation.getFactor();

		if (frequence == null) {
			if (stageindex < 10 ) {
				return Result.error("Stage["  + String.format("%02d", stageindex) + 
						"] Decimation:Frequency must not be null");
			}else {									    
				return Result.error("Stage["  +  stageindex + 
						"] Decimation:Frequency must not be null");
			}
			
			
		}
		if (Math.abs(sampleRate.getValue() - (frequence.getValue() / factor.doubleValue())) > 0.0001) {
			if (stageindex < 10 ) {
				return Result.error("Chan:Samplerate "+sampleRate.getValue() + " != Stage[" 
						+ String.format("%02d", stageindex) + "] Decimation:Frequency/Decimation:Factor "
						+ (frequence.getValue() / factor.doubleValue()));
			}else {									    
				return Result.error("Channel:Samplerate: "+sampleRate.getValue() + " != Stage[" 
						+  stageindex + "] Decimation:Frequency/Decimation:Factor "
						+ (frequence.getValue() / factor.doubleValue()));
			}
						
		}

		return Result.success();
	}
	
}
