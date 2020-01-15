package edu.iris.validator.rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.Condition;

public class RuleEngineService {
	private static final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);
	private RuleEngineRegistry ruleEngineRegistry;
	boolean ignoreWarnings = false;

	public RuleEngineService(boolean ignoreWarnings, int... ignoreRules) {
		this.ruleEngineRegistry = new RuleEngineRegistry(ignoreRules);
		this.ignoreWarnings = ignoreWarnings;
	}

	public void setRuleEngineRegistry(RuleEngineRegistry ruleEngineRegistry) {
		this.ruleEngineRegistry = ruleEngineRegistry;
	}

	public void registerRule(int id, Condition condition, Class<?> clazz) {
		this.ruleEngineRegistry.add(id, condition, clazz);
	}

	public void unregister(int id) {
		this.ruleEngineRegistry.unregister(id);
	}

	public Map<Integer, Set<Message>> executeAllRules(FDSNStationXML document) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (document != null && document.getNetwork() != null) {
			for (Network network : document.getNetwork()) {
				Map<Integer, Set<Message>> m = this.executeAllRules(network);
				for (Map.Entry<Integer, Set<Message>> e : m.entrySet()) {
					map.computeIfAbsent(e.getKey(), k -> new HashSet<Message>()).addAll(e.getValue());
				}
			}
		}
		return map;
	}

	public Map<Integer, Set<Message>> executeNetworkRules(Network network) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (network != null) {
			for (Rule rule : this.ruleEngineRegistry.getNetworkRules()) {
				Message m = rule.execute(network);
				m.setRule(rule);
				m.setNetwork(network);
				if (m instanceof edu.iris.validator.rules.Error || (!ignoreWarnings && m instanceof Warning)) {
					map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(m);
				} else if (m instanceof edu.iris.validator.rules.NestedMessage) {
					List<Message> list = ((edu.iris.validator.rules.NestedMessage) m).getNestedMessages();
					if (list != null) {
						for (Message msg : list) {
							if (msg instanceof edu.iris.validator.rules.Error
									|| (!ignoreWarnings && msg instanceof Warning)) {
								map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(msg);
							}
						}
					}
				}
			}
		}
		return map;

	}

	public Map<Integer, Set<Message>> executeAllRules(Network network) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (network == null) {
			return map;
		}
		for (Rule rule : this.ruleEngineRegistry.getNetworkRules()) {
			logger.debug("Executing rule {}",rule.getId());
			Message m = rule.execute(network);
			m.setRule(rule);
			m.setNetwork(network);
			if (m instanceof edu.iris.validator.rules.Error || (!ignoreWarnings && m instanceof Warning)) {
				map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(m);
			} else if (m instanceof edu.iris.validator.rules.NestedMessage) {
				List<Message> list = ((edu.iris.validator.rules.NestedMessage) m).getNestedMessages();
				if (list != null) {
					for (Message msg : list) {
						if (msg instanceof edu.iris.validator.rules.Error
								|| (!ignoreWarnings && msg instanceof Warning)) {
							map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(msg);
						}
					}
				}
			}
		}

		if (network.getStations() != null) {
			for (Station station : network.getStations()) {
				Map<Integer, Set<Message>> m = this.executeAllRules(network, station);
				for (Map.Entry<Integer, Set<Message>> e : m.entrySet()) {
					map.computeIfAbsent(e.getKey(), k -> new HashSet<Message>()).addAll(e.getValue());
				}
			}
		}
		return map;
	}

	public Map<Integer, Set<Message>> executeAllRules(Network network, Station station) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (station != null) {
			Collection<Rule> col = this.ruleEngineRegistry.getStationRules();
			for (Rule rule : col) {
				Message m = rule.execute(network, station);
				m.setRule(rule);
				m.setNetwork(network);
				m.setStation(station);
				if (m instanceof edu.iris.validator.rules.Error || (!ignoreWarnings && m instanceof Warning)) {
					map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(m);
				} else if (m instanceof edu.iris.validator.rules.NestedMessage) {
					List<Message> list = ((edu.iris.validator.rules.NestedMessage) m).getNestedMessages();
					if (list != null) {
						for (Message msg : list) {
							if (msg instanceof edu.iris.validator.rules.Error
									|| (!ignoreWarnings && msg instanceof Warning)) {
								map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(msg);
							}
						}
					}
				}
			}
			if (station.getChannels() != null) {
				for (Channel channel : station.getChannels()) {
					Map<Integer, Set<Message>> m = this.executeAllRules(network, station, channel);
					for (Map.Entry<Integer, Set<Message>> e : m.entrySet()) {
						map.computeIfAbsent(e.getKey(), k -> new HashSet<Message>()).addAll(e.getValue());
					}

				}
			}
		}
		return map;
	}

	public Map<Integer, Set<Message>> executeAllRules(Network network, Station station, Channel channel) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (channel != null) {
			if (isSpecial(channel)) {
				return map;
			}
			for (Rule rule : this.ruleEngineRegistry.getChannelRules()) {
				Message m = rule.execute(network, station, channel);
				m.setRule(rule);
				m.setNetwork(network);
				m.setStation(station);
				m.setChannel(channel);
				if (m instanceof edu.iris.validator.rules.Error || (!ignoreWarnings && m instanceof Warning)) {
					map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(m);
				} else if (m instanceof edu.iris.validator.rules.NestedMessage) {
					List<Message> list = ((edu.iris.validator.rules.NestedMessage) m).getNestedMessages();
					if (list != null) {
						for (Message msg : list) {
							if (msg instanceof edu.iris.validator.rules.Error
									|| (!ignoreWarnings && msg instanceof Warning)) {
								map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(msg);
							}
						}
					}
				}
			}
			map.putAll(this.executeAllRules(network, station, channel, channel.getResponse()));
		}
		return map;
	}

	public Map<Integer, Set<Message>> executeAllRules(Network network, Station station, Channel channel,
			Response response) {
		Map<Integer, Set<Message>> map = new HashMap<>();
		if (isSpecial(channel)) {
			return map;
		}
		if (response != null && !isEmpty(response)) {
			for (Rule rule : this.ruleEngineRegistry.getResponseRules()) {
				Message m = rule.execute(network, station, channel, response);
				m.setRule(rule);
				m.setNetwork(network);
				m.setStation(station);
				m.setChannel(channel);
				if (m instanceof edu.iris.validator.rules.Error || (!ignoreWarnings && m instanceof Warning)) {
					map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(m);
				} else if (m instanceof edu.iris.validator.rules.NestedMessage) {
					List<Message> list = ((edu.iris.validator.rules.NestedMessage) m).getNestedMessages();
					if (list != null) {
						for (Message msg : list) {
							if (msg instanceof edu.iris.validator.rules.Error
									|| (!ignoreWarnings && msg instanceof Warning)) {
								map.computeIfAbsent(rule.getId(), k -> new HashSet<Message>()).add(msg);
							}
						}
					}
				}
			}
		}
		return map;
	}

	private boolean isSpecial(Channel channel) {
		if (channel == null) {
			throw new IllegalArgumentException("Channel cannot be null");
		}
		if (channel.getCode().startsWith("A")) {
			return true;
		}
		if ("LOG".equals(channel.getCode()) || "SOH".equals(channel.getCode())) {
			return true;
		}
		return false;
	}

	private boolean isEmpty(Response response) {
		if (response == null) {
			return true;
		}
		if (response.getInstrumentPolynomial() != null || response.getInstrumentSensitivity() != null
				|| (response.getStage() != null && !response.getStage().isEmpty())) {
			return false;
		}
		return true;
	}

	public List<Rule> getRules() {
		return this.ruleEngineRegistry.getRules();
	}

	public Rule getRule(int id) {
		return this.ruleEngineRegistry.getRule(id);
	}

}
