package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class StageUnitCondition extends ChannelRestrictedCondition {

	public StageUnitCondition(boolean required, String description, Restriction[] restrictions) {
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
		boolean stageskip = false;
		int prevstage=0;
		if (isRestricted(channel)) {
			return Result.success();
		}

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {

			StageUnit current = null;
			for (ResponseStage stage : response.getStage()) {

				StageUnit stageUnit = getUnits(stage);
				if (stageUnit == null) {
					stageskip = true;
					continue;
				}
				// Current is the previous stage which harvest output unit
				// stageUnit is current stage which harvest input unit. 
				if (current == null) {
					current = stageUnit;
					continue;
				}
				
				//First make this a nested message 

				if (!current.output.getName().equals(stageUnit.input.getName())) {
					if(stageskip == true) {
						prevstage  = stage.getNumber().intValue() - 2;
						stageskip=false;
					}else {
						prevstage  = stage.getNumber().intValue() - 1;
					}
					
					if (stage.getNumber().intValue() < 10 ) {
					    nestedMessage.add(Result.error("Stage[" + String.format("%02d", stage.getNumber().intValue()) + "] input unit " + stageUnit.input.getName()
					    + " must equal Stage[" + String.format("%02d", prevstage) + "] output unit "
					    + current.output.getName()));
					}else {
						nestedMessage.add(Result.error("Stage[" + stage.getNumber().intValue() + "] input unit " + stageUnit.input.getName()
						+ " must equal Stage[" + prevstage + "] output unit "
						+ current.output.getName()));
					}

					returnmessage = true;
				}
				current = stageUnit;
			}
			if(returnmessage==true) {

				return nestedMessage;
			}

		}
		
		return Result.success();
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
