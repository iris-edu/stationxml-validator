package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.exceptions.StationxmlException;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class InstrumentUnitsStageCondition extends ChannelRestrictedCondition {
	private Restriction[] restrictions;

	public InstrumentUnitsStageCondition(boolean required, String description, Restriction[] restrictions) {
		super(required, description);
		this.restrictions = restrictions;
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
		if (channel == null) {
			return Result.success();
		}
		return this.evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
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
		for (Restriction r : this.restrictions) {
			if (r.qualifies(channel)) {
				return Result.success();
			}
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
		

		if (channel.getResponse().getInstrumentSensitivity() != null) {
			String instrumentInputUnit = channel.getResponse().
					getInstrumentSensitivity().getInputUnits().getName();
			String instrumentOutputUnit = channel.getResponse().
					getInstrumentSensitivity().getOutputUnits().getName();
			if(!inputUnit.equalsIgnoreCase(instrumentInputUnit)){
				nestedMessage.add(Result.error("InsturmentSensitivity input "
						+ "units "+ instrumentInputUnit+" must equal Stage[01] input "
						+ "units "+ inputUnit));
				returnmessage=true;
			}
			if(!outputUnit.equalsIgnoreCase(instrumentOutputUnit)){
				if (lastindex+1 < 10 ) {
				    nestedMessage.add(Result.error("InsturmentSensitivity output "
						+ "units "+ instrumentOutputUnit+" must equal Stage["
						+String.format("%02d",(lastindex+1))+"] output units "+ outputUnit));
				    returnmessage=true;
				}else {
				    nestedMessage.add(Result.error("InsturmentSensitivity output "
						+ "units "+ instrumentOutputUnit+" must equal Stage["
						+(lastindex+1)+"] output units "+ outputUnit));
				    returnmessage=true;
				}
		}
			
		}else{
			String instrumentInputUnit = channel.getResponse().
					getInstrumentPolynomial().getInputUnits().getName();
			String instrumentOutputUnit = channel.getResponse().
					getInstrumentPolynomial().getOutputUnits().getName();
			if(!inputUnit.equalsIgnoreCase(instrumentInputUnit)){
				nestedMessage.add(Result.error("InsturmentPolynomial input "
						+ "units "+ instrumentInputUnit+" must equal Stage[01] input "
						+ "units "+ inputUnit));
				returnmessage=true;
			}
			if(!outputUnit.equalsIgnoreCase(instrumentOutputUnit)){
				if (lastindex+1 < 10 ) {
				    nestedMessage.add(Result.error("InsturmentPolynomial output "
						+ "units "+ instrumentOutputUnit+" must equal Stage["
						+String.format("%02d",(lastindex+1))+"] output units "+ outputUnit));
				    returnmessage=true;
				}else {
				    nestedMessage.add(Result.error("InsturmentPolynomial output "
						+ "units "+ instrumentOutputUnit+" must equal Stage["
						+(lastindex+1)+"] output units "+ outputUnit));
				    returnmessage=true;
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
 
}


