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

public class DecimationStageUnitCondition extends AbstractCondition {
	private Restriction[] restrictions;

	public DecimationStageUnitCondition(boolean required, String description, Restriction[] restrictions) {
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

			StageUnit current = null;
			int lastStage = response.getStage().size();
			for (ResponseStage stage : response.getStage()) {
				StageUnit stageUnit = getUnits(stage);
				// Check to make sure the last stage is not null
				if (stageUnit == null) {
					if(stage.getDecimation()!= null) {
						if (stage.getNumber().intValue() < 10 ) {
							nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "] output unit "
						    + " must not be null because response.Decimation is included"));
							returnmessage=true;
						}else {
							nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "] output unit "
							+ " must not be null because response.Decimation is included"));
							returnmessage=true;
						}
					}else {
					stageskip = true;
					continue;
					}
				}
				if (stage.getDecimation()!= null) {
					if(stageUnit.output.getName().toLowerCase().contains("count")) {
						continue;
					}else {
				            if (stage.getNumber().intValue() < 10 ) {
				            	nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "] output unit " 
				                + stageUnit.output.getName()
					            + " must be count(s) because Stage[" + String.format("%02d",stage.getNumber().intValue()) + "].Decimation is included"));
								returnmessage=true;
					        }else { 
					        	nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "] output unit "
						        + stageUnit.output.getName()
					            + " must be count(s) because Stage[" + stage.getNumber().intValue() + "].Decimation is included"));
								returnmessage=true;
						    }
					    }
				    }
			    }
		    }
	    }catch(Exception e) {	
	}
	if(returnmessage==true) {
	    return nestedMessage;
	}else {
		   return Result.success();
		}
	}

	public StageUnit getUnits(ResponseStage stage) {

		Units input = null;
		Units output = null;
		if (stage.getPolesZeros() != null) {
			input = stage.getPolesZeros().getInputUnits();
			output = stage.getPolesZeros().getOutputUnits();
		}
		if (stage.getResponseList() != null) {
			input = stage.getResponseList().getInputUnits();
			output = stage.getResponseList().getOutputUnits();
		}
		if (stage.getFIR() != null) {
			input = stage.getFIR().getInputUnits();
			output = stage.getFIR().getOutputUnits();
		}
		if (stage.getPolynomial() != null) {
			input = stage.getPolynomial().getInputUnits();
			output = stage.getPolynomial().getOutputUnits();
		}
		if (stage.getCoefficients() != null) {
			input = stage.getCoefficients().getInputUnits();
			output = stage.getCoefficients().getOutputUnits();
		}

		if (input == null || output == null) {
			return null;
		}
		return new StageUnit(input, output);
	}

	class StageUnit {
		Units input;
		Units output;

		public StageUnit(Units input, Units output) {
			this.input = input;
			this.output = output;
		}
	}
}
