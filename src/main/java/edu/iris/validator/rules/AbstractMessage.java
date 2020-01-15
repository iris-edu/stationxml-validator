package edu.iris.validator.rules;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;

public abstract class AbstractMessage implements Message {

	private Rule rule;
	private String source;
	private Network network;
	private Station station;
	private Channel channel;

	private String message;

	protected AbstractMessage(String message) {
		this.message = message;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Rule getRule() {
		return this.rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getDescription() {
		return this.message;
	}

	public String getSource() {
		return this.source;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Result ");
		if (rule != null) {
			builder.append("[ruleId=" + rule.getId());
		}
		if (network != null) {
			builder.append(", network=" + network.getCode());
		}
		if (station != null) {
			builder.append(", station=" + station.getCode());
		}
		if (channel != null) {
			builder.append(", channel=" + channel.getCode() + "|" + channel.getLocationCode());
			builder.append(channel.getStartDate() + "|" + channel.getEndDate());
		}
		builder.append(", message=" + message + "]");

		return builder.toString();
	}

}
