package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class DecimationAnalogFilterCondition extends AbstractCondition {
	private Restriction[] restrictions;

	public DecimationAnalogFilterCondition(boolean required, String description, Restriction[] restrictions) {
		super(required, description);
		this.restrictions = restrictions;
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
		boolean stageskip = false;
		boolean includeDec = false;
		int prevstage=0;
		try {
		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			for (ResponseStage stage : response.getStage()) {
				if (stage.getPolesZeros()!=null && stage.getPolesZeros().getPzTransferFunctionType().equalsIgnoreCase("LAPLACE (RADIANS/SECOND)")) {
				    if (stage.getDecimation()!= null) {
				            if (stage.getNumber().intValue() < 10 ) {
				            	nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]"
				            			+ " includes PolesZeros:PzTransferFunctionType:LAPLACE (RADIANS/SECOND)" 
					            + " so decimation must not be included in Stage[" +String.format("%02d", stage.getNumber().intValue()) + "]."));
								returnmessage=true;
					        }else { 
					        	nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "]"
						        + " includes PolesZeros:PzTransferFunctionType:LAPLACE (RADIANS/SECOND)"
					            + " so decimation must not be included in Stage[" + stage.getNumber().intValue() + "]."));
								returnmessage=true;
						    }
					    }
				    }else if (stage.getPolesZeros()!=null && stage.getPolesZeros().getPzTransferFunctionType().equalsIgnoreCase("LAPLACE (HERTZ)")) {
				        if (stage.getDecimation()!= null) {
				            if (stage.getNumber().intValue() < 10 ) {
				            	nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]"
				            			+ " includes PolesZeros:PzTransferFunctionType:LAPLACE (HERTZ)" 
					            + " so decimation must not be included in Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]."));
								returnmessage=true;
					        }else { 
					        	nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "]"
						        + " includes PolesZeros:PzTransferFunctionType:LAPLACE (HERTZ)"
					            + " so decimation must not be included in Stage[" + stage.getNumber().intValue() + "]."));
								returnmessage=true;
						    }
					    }
				    }else if (stage.getCoefficients() !=null && stage.getCoefficients().getCfTransferFunctionType().equalsIgnoreCase("ANALOG (RADIANS/SECOND)")) {
				    	if (stage.getDecimation()!= null) {
				            if (stage.getNumber().intValue() < 10 ) {
				            	nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]"
				            			+ " includes CoefficientsType:CfTransferFunctionType:ANALOG (RADIANS/SECOND)" 
					            + " so decimation must not be included in Stage[" +  String.format("%02d", stage.getNumber().intValue()) + "]."));
								returnmessage=true;
					        }else { 
					        	nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "]"
						        + " includes PolesZeros:PzTransferFunctionType:ANALOG (RADIANS/SECOND)"
					            + " so decimation must not be included in Stage[" + stage.getNumber().intValue() + "]."));
								returnmessage=true;
						    }
					    }
			    }else if (stage.getCoefficients() !=null && stage.getCoefficients().getCfTransferFunctionType().equalsIgnoreCase("ANALOG (HERTZ)")) {
			    	if (stage.getDecimation()!= null) {
			            if (stage.getNumber().intValue() < 10 ) {
			            	nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]"
			            			+ " includes CoefficientsType:CfTransferFunctionType:ANALOG (HERTZ)" 
				            + " so decimation must not be included in Stage[" + String.format("%02d", stage.getNumber().intValue()) + "]."));
							returnmessage=true;
				        }else { 
				        	nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "]"
					        + " includes CoefficientsType:CfTransferFunctionType:ANALOG (HERTZ)"
				            + " so decimation must not be included in Stage[" + stage.getNumber().intValue() + "]."));
							returnmessage=true;
					    }
				    }
		    }else {

	        }
			}
		   }
		}
	    catch(Exception e) {	
	}
	if(returnmessage==true) {
	    return nestedMessage;
	}else {
		   return Result.success();
		}
	}


}
