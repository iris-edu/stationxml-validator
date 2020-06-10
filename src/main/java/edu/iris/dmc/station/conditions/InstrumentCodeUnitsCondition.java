package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.exceptions.StationxmlException;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class InstrumentCodeUnitsCondition extends AbstractCondition {

	public InstrumentCodeUnitsCondition(boolean required, String description) {
		super(required, description);

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
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
		String inputUnit ="";
		String code = channel.getCode();
		try {
		if(channel.getResponse().getStage().size()==0) {
			throw new StationxmlException("Response is missing from input StationXML");
		}
		ResponseStage stage1 = channel.getResponse().getStage().get(0);
		if(stage1.getCoefficients() != null) {
			inputUnit = stage1.getCoefficients().getInputUnits().getName();
		}else if(stage1.getPolesZeros() != null){
			inputUnit = stage1.getPolesZeros().getInputUnits().getName();
		}else if(stage1.getResponseList() != null){
			inputUnit = stage1.getResponseList().getInputUnits().getName();
		}else if(stage1.getFIR()!=null) {
			inputUnit = stage1.getFIR().getInputUnits().getName();
		}else if(stage1.getPolynomial()!= null){
			inputUnit = stage1.getPolynomial().getInputUnits().getName();
		}else {
			return Result.success();
		}
		
		int lastindex =  channel.getResponse().getStage().size()-1;
		ResponseStage stagelast = channel.getResponse().getStage().get(lastindex);
		String outputUnit ="";
		if(stagelast.getCoefficients() != null) {
			outputUnit = stagelast.getCoefficients().getOutputUnits().getName();
		}else if(stagelast.getPolesZeros() != null){
			outputUnit = stagelast.getPolesZeros().getOutputUnits().getName();
		}else if(stagelast.getResponseList() != null){
			outputUnit = stagelast.getResponseList().getOutputUnits().getName();
		}else if(stagelast.getFIR()!=null) {
			outputUnit = stagelast.getFIR().getOutputUnits().getName();
		}else if(stagelast.getPolynomial()!= null){
			outputUnit = stagelast.getPolynomial().getOutputUnits().getName();
		}else {
			return Result.success();
		}

		

		if("HLN".indexOf(code.charAt(1)) >=0 | "hln".indexOf(code.charAt(1)) >=0) {
			if(!inputUnit.toLowerCase().contains("m/s")) {
				nestedMessage.add(Result.warning("Instrument code " +code.charAt(1)+" should have stage 1 input units similar to *m/s* but input units are "+inputUnit));
				returnmessage=true;
			}
			if(!outputUnit.toLowerCase().contains("count")) {
				nestedMessage.add(Result.warning("Instrument code " +code.charAt(1)+ " should have stage last output units similar to count* but output units are "+outputUnit));
				returnmessage=true;
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
 
}


