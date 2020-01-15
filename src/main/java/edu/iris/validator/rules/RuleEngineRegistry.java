package edu.iris.validator.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.CalibrationUnitCondition;
import edu.iris.validator.conditions.CodeCondition;
import edu.iris.validator.conditions.Condition;
import edu.iris.validator.conditions.DecimationCondition;
import edu.iris.validator.conditions.DecimationSampleRateCondition;
import edu.iris.validator.conditions.DigitalFilterCondition;
import edu.iris.validator.conditions.DistanceCondition;
import edu.iris.validator.conditions.EmptySensitivityCondition;
import edu.iris.validator.conditions.EpochOverlapCondition;
import edu.iris.validator.conditions.EpochRangeCondition;
import edu.iris.validator.conditions.FrequencyCondition;
import edu.iris.validator.conditions.LocationCodeCondition;
import edu.iris.validator.conditions.MissingDecimationCondition;
import edu.iris.validator.conditions.OrientationCondition;
import edu.iris.validator.conditions.PolesZerosCondition;
import edu.iris.validator.conditions.PolynomialCondition;
import edu.iris.validator.conditions.ResponseListCondition;
import edu.iris.validator.conditions.SampleRateCondition;
import edu.iris.validator.conditions.SensorCondition;
import edu.iris.validator.conditions.StageGainNonZeroCondition;
import edu.iris.validator.conditions.StageGainProductCondition;
import edu.iris.validator.conditions.StageSequenceCondition;
import edu.iris.validator.conditions.StageUnitCondition;
import edu.iris.validator.conditions.StartTimeCondition;
import edu.iris.validator.conditions.StationElevationCondition;
import edu.iris.validator.conditions.UnitCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.ResponsePolynomialRestriction;
import edu.iris.validator.restrictions.Restriction;

public class RuleEngineRegistry {

	private Map<Integer, Rule> networkRules = new HashMap<>();
	private Map<Integer, Rule> stationRules = new HashMap<>();
	private Map<Integer, Rule> channelRules = new HashMap<>();
	private Map<Integer, Rule> responseRules = new HashMap<>();

	public RuleEngineRegistry(int... ignoreRules) {
		init(ignoreRules);
	}

	private void init(int... ignoreRules) {
		Set<Integer> s = new HashSet<>();
		if (ignoreRules != null) {
			for (int nt : ignoreRules) {
				s.add(nt);
			}
		}
		defaultNetworkRules(s);
		defaultStationRules(s);
		defaultChannelRules(s);
		defaultResponseRules(s);
	}

	private void defaultNetworkRules(Set<Integer> set) {
		String codeRegex = "[A-Z0-9_\\*\\?]{1,2}";
		if (!set.contains(101)) {
			add(101, new CodeCondition(true, codeRegex,
					"Network:Code must be assigned a string consisting of 1-2 uppercase characters A-Z and or numeric characters 0-9."),
					Network.class);
		}
		if (!set.contains(110)) {
			add(110, new StartTimeCondition(true,
					"Network:startDate must occur before Network:endDate if Network:endDate is available."),
					Network.class);
		}
		if (!set.contains(111)) {
			add(111, new EpochOverlapCondition(true,
					"Station:Epoch cannot be partly concurrent with any other Station:Epoch encompassed in parent Network:Epoch."),
					Network.class);
		}
		if (!set.contains(112)) {
			add(112, new EpochRangeCondition(true,
					"Network:Epoch must encompass all subordinate Station:Epoch [Epoch=startDate-endDate]"),
					Network.class);
		}
	}

	private void defaultStationRules(Set<Integer> set) {
		String codeRegex = "[A-Z0-9_\\*\\?]{1,5}";
		if (!set.contains(201)) {
			add(201, new CodeCondition(true, codeRegex,
					"Station:Code must be assigned a string consisting of 1-5 uppercase characters A-Z and or numeric characters 0-9."),
					Station.class);
		}

		if (!set.contains(210)) {
			add(210, new StartTimeCondition(true,
					"Station:startDate is required and must occur before Station:endDate if Station:endDate is available."),
					Station.class);
		}
		if (!set.contains(211)) {
			add(211, new EpochOverlapCondition(true,
					"Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch."),
					Station.class);
		}
		if (!set.contains(221)) {
			add(212, new EpochRangeCondition(true,
					"Station:Epoch must encompass all subordinate Channel:Epoch [Epoch=startDate-endDate]"),
					Station.class);
		}

		if (!set.contains(222)) {
			add(222, new DistanceCondition(true,
					"Station:Position must be within 1 km of all subordinate Channel:Position.", 1), Station.class);
		}
		if (!set.contains(223)) {
			add(223, new StationElevationCondition(true,
					"Station:Elevation must be within 1 km of all subordinate Channel:Elevation."), Station.class);
		}

	}

	private void defaultChannelRules(Set<Integer> set) {
		String codeRegex = "[A-Z0-9_\\*\\?]{3}";
		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
		if (!set.contains(301)) {
			add(301, new CodeCondition(true, codeRegex,
					"Channel:Code must be assigned a string consisting of 3 uppercase characters A-Z and or numeric characters 0-9."),
					Channel.class);
		}
		if (!set.contains(302)) {
			add(302, new LocationCodeCondition(true, "([A-Z0-9\\*\\ ]{0,2})?",
					"Channel:locationCode must be unassigned or be assigned a string consisting of 0-2 uppercase characters A-Z and or numeric characters 0-9."),
					Channel.class);
		}
		if (!set.contains(303)) {
			add(303, new CalibrationUnitCondition(false, "Invalid Calibration unit is invalid"), Channel.class);
		}
		if (!set.contains(304)) {
			add(304, new SensorCondition(true, "Channel:Sensor:Description cannot be null."), Channel.class);
		}
		if (!set.contains(305)) {
			add(305, new SampleRateCondition(false,
					"If Channel:SampleRate is NULL or 0 then Response information should not be included.",
					restrictions), Channel.class);
		}
		if (!set.contains(310)) {
			add(310, new StartTimeCondition(true,
					"Channel:startDate is required and must occur before Channel:endDate if Channel:endDate is available."),
					Channel.class);
		}
		
		if (!set.contains(332)) {
			add(332, new OrientationCondition(true,
					"Channel:Azimuth and or Channel:Dip do not correspond within 5 degrees of tolerance to last digit of orthogonal Channel:Code.",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }), Channel.class);
		}
	}

	private void defaultResponseRules(Set<Integer> s) {

		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

		if (!s.contains(401)) {
			add(401, new StageSequenceCondition(true,
					"The 'number' attribute of Response::Stage element must start at 1 and be sequential",
					restrictions), Response.class);
		}
		if (!s.contains(402)) {
			add(402, new UnitCondition(true,
					"Stage[N]:InputUnits:Name and/or Stage[N]:OutputUnits:Name are not defined in Unit name overview for IRIS StationXML validator.",
					restrictions), Response.class);
		}
		if (!s.contains(403)) {
			add(403, new StageUnitCondition(true, "Stage[N]:InputUnits:Name must equal Stage[N-1]:OutputUnits:Name.",
					restrictions), Response.class);
		}
		if (!s.contains(404)) {
			add(404, new DigitalFilterCondition(true,
					"Stage types FIR|Coefficient|PolesZeros with transfer function type Digital must include Decimation and StageGain elements.",
					restrictions), Response.class);
		}
		if (!s.contains(405)) {
			add(405, new ResponseListCondition(true,
					"Stage of type ResponseList cannot be the only stage available in a response.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		}
		if (!s.contains(410)) {
			add(410, new EmptySensitivityCondition(true, "InstrumentSensitivity:Value cannot be assigned 0 or Null.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(411)) {
			add(411, new FrequencyCondition(true,
					"InstrumentSensitivity:Frequency must be less than Channel:SampleRate/2 [Nyquist Frequency].",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(412)) {
			add(412, new StageGainProductCondition(true,
					"InstrumentSensitivity:Value must equal the product of all StageGain:Value if all StageGain:Frequency are equal to InstrumentSensitivity:Frequency [Normalization Frequency].",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(413)) {
			add(413, new StageGainNonZeroCondition(true, "StageGain:Value cannot be assigned 0 or Null.",
					new ChannelCodeRestriction(), new ResponsePolynomialRestriction(), new ChannelTypeRestriction()),
					Response.class);
		}
		if (!s.contains(414)) {
			add(414, new PolesZerosCondition(false,
					"If Stage[N] of type PolesZeros contains a Zero where both Real and Imaginary components equal 0 then InstrumentSensitivity:Frequency cannot equal 0 and Stage[N]:StageGain:Frequency cannot equal 0.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(415)) {
			add(415, new PolynomialCondition(false,
					"Response must be defined as Response:InstrumentPolynomial if it contains any Stages defined as ResponseStage:Polynomial",
					new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		}
		if (!s.contains(420)) {
			add(420, new MissingDecimationCondition(true,
					"A Response must contain at least one instance of Response:Stage:Decimation.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(421)) {
			add(421, new DecimationSampleRateCondition(true,
					"Stage[Final]:Decimation:InputSampleRate divided by Stage[Final]:Decimation:Factor must equal Channel:SampleRate.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(422)) {
			add(422, new DecimationCondition(true,
					"Stage[N]:Decimation:InputSampleRate must equal Stage[N-1]:Decimation:InputSampleRate divided by Stage[N-1]:Decimation:Factor.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
	}

	public void add(int id, Condition condition, Class<?> clazz) {
		if (condition == null || clazz == null) {
			throw new IllegalArgumentException("Null condition|class is not permitted");
		}
		Rule ruleToAdd = new Rule(id);
		ruleToAdd.setCondition(condition);
		add(ruleToAdd, clazz);
	}

	public void add(Rule ruleToAdd, Class<?> clazz) {
		if (ruleToAdd == null || clazz == null) {
			throw new IllegalArgumentException("Null rule|class is not permitted");
		}
		if (Network.class == clazz) {
			this.networkRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Station.class == clazz) {
			this.stationRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Channel.class == clazz) {
			this.channelRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Response.class == clazz) {
			this.responseRules.put(ruleToAdd.getId(), ruleToAdd);
		} else {
			throw new IllegalArgumentException("Unsupported class definition " + clazz.getName());
		}
	}

	public Rule unregister(int id) {
		Rule rule = this.networkRules.remove(id);
		if (rule == null) {
			rule = this.stationRules.remove(id);
		}
		if (rule == null) {
			rule = this.channelRules.remove(id);
		}

		if (rule == null) {
			rule = this.responseRules.remove(id);
		}
		return rule;
	}

	public Rule getRule(int id) {
		Rule rule = this.networkRules.remove(id);
		if (rule == null) {
			rule = this.stationRules.remove(id);
		}
		if (rule == null) {
			rule = this.channelRules.remove(id);
		}

		if (rule == null) {
			rule = this.responseRules.remove(id);
		}
		return rule;
	}

	public List<Rule> getRules() {
		List<Rule> list = new ArrayList<>();

		list.addAll(this.networkRules.values());
		list.addAll(this.stationRules.values());
		list.addAll(this.channelRules.values());
		list.addAll(this.responseRules.values());
		return list;
	}

	public Collection<Rule> getNetworkRules() {
		return this.networkRules.values();
	}

	public Collection<Rule> getStationRules() {
		return this.stationRules.values();
	}

	public Collection<Rule> getChannelRules() {
		return this.channelRules.values();
	}

	public Collection<Rule> getResponseRules() {
		return this.responseRules.values();
	}

}
