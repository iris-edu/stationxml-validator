package edu.iris.validator.rules;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.Condition;

public class Rule {

	private int id;
	private Condition condition;
	// private Action action;

	public Rule(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public String getDescription() {
		return this.condition.getDescription();
	}

	public Message execute(Network network) {
		Message message = condition.evaluate(network);
		message.setRule(this);
		message.setNetwork(network);
		return message;/*
		if (message instanceof Success) {
			return;
		}

		update(context,action, message, network, null, null);*/
	}

	public Message execute(Network network, Station station) {
		return condition.evaluate(station);
	}

	public Message execute(Network network, Station station, Channel channel) {
		return condition.evaluate(channel);
	}

	public Message execute(Network network, Station station, Channel channel, Response response) {
		return condition.evaluate(channel, response);
	}

	public Condition getConditionExpression() {
		return condition;
	}
	
	public String getresult() {
		return condition.result();
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Rule{" + "conditionExpression=" + condition + "'}'";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
