package edu.iris.validator.conditions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;

public interface Condition {
	public boolean isRequired();

	public String getDescription();

	public Message evaluate(FDSNStationXML document);
	
	public Message evaluate(Network network);

	public Message evaluate(Station station);

	public Message evaluate(Channel channel);
	
	public Message evaluate(Channel channel,Response response);
	
	public String result();
}
