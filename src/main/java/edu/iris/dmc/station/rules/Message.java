package edu.iris.dmc.station.rules;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;

public interface Message {

	public void setRule(Rule rule);

	public Rule getRule();

	public void setSource(String source);

	public void setNetwork(Network network);

	
	public Network getNetwork();

	public void setStation(Station station);

	public Station getStation();

	public void setChannel(Channel channel);

	public Channel getChannel();

	public String getDescription();

	public String getSource();

}

