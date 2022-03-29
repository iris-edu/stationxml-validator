package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.exceptions.StationxmlException;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;

public class BandCodeCondition extends AbstractCondition {
	private Restriction[] restrictions;

	public BandCodeCondition(boolean required, String description, Restriction[] restrictions) {
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
		NestedMessage nestedMessage = new NestedMessage();
		boolean returnmessage = false;
		String inputUnit ="";
		String code = channel.getCode();
		try {
		if(channel.getResponse().getStage().size()==0) {
			throw new StationxmlException("Response is missing from input StationXML");
		}
		for (Restriction r : this.restrictions) 
			if (r.qualifies(channel)) {
				return Result.success();
			}
		
		Double samplerate = channel.getSampleRate().getValue();
		String bandcode =  Character.toString(code.charAt(0));
		bandcode=bandcode.toUpperCase();
		switch (bandcode){
	    case "F": 
	        if(samplerate < 1000 || samplerate >= 5000) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 1000 and < 5000"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "G": 
	        if(samplerate < 1000 || samplerate >= 5000) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 1000 and < 5000"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "D": 
	        if(samplerate < 250 || samplerate >= 1000) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 250 and < 1000"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "C": 
	        if(samplerate < 250 || samplerate >= 1000) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 250 and < 1000"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "E": 
	        if(samplerate < 80 || samplerate >= 250) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 80 and < 250"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "H": 
	        if(samplerate < 80 || samplerate >= 250) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 80 and < 250"));
				returnmessage = true;
			break;
	        } 
	        break;
	    case "S": 
	        if(samplerate < 10.0 || samplerate >= 80.0) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 10 and < 80"));
				returnmessage = true;
			break;
	        } 
	        break;
	    case "B": 
	        if(samplerate < 10.0 || samplerate >= 80.0) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 10 and < 80"));
				returnmessage = true;
			break;
	        } 
	        break;
	    case "M": 
	        if(samplerate < 1 || samplerate >= 10) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 1 and < 10"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "L": 
	        if(samplerate < 0.1 || samplerate >= 10) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.1 and < 10"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "V": 
	        if(samplerate < 0.01 || samplerate >= 1) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.01 and < 1"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "U": 
	        if(samplerate < 0.001 || samplerate >= 0.1) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.001 and < 0.1"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "R": 
	        if(samplerate < 0.0001 || samplerate >= 0.001) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.0001 and < 0.001"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "P": 
	        if(samplerate < 0.00001 || samplerate >= 0.0001) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.00001 and < 0.0001"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "T": 
	        if(samplerate < 0.000001 || samplerate >= 0.00001) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned between >= 0.000001 and < 0.00001"));
				returnmessage = true;
			break;
	        }
	        break;
	    case "Q": 
	        if(samplerate >= 0.000001) {
				nestedMessage.add(Result.warning("Frequency Band " + bandcode +" must have a sample rate assigned < 0.000001"));
				returnmessage = true;
				break;
	        } 
	        break;
	    default:
			break;

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


