package edu.iris.dmc.station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.conditions.AzimuthDipCondition;
import edu.iris.dmc.station.conditions.BandCodeCondition;
import edu.iris.dmc.station.conditions.CalibrationUnitCondition;
import edu.iris.dmc.station.conditions.CodeCondition;
import edu.iris.dmc.station.conditions.Condition;
import edu.iris.dmc.station.conditions.DecimationAnalogFilterCondition;
import edu.iris.dmc.station.conditions.DecimationCondition;
import edu.iris.dmc.station.conditions.DecimationSampleRateCondition;
import edu.iris.dmc.station.conditions.DecimationStageGainCondition;
import edu.iris.dmc.station.conditions.DecimationStageUnitCondition;
import edu.iris.dmc.station.conditions.DigitalFilterCondition;
import edu.iris.dmc.station.conditions.DistanceCondition;
import edu.iris.dmc.station.conditions.EmptySensitivityCondition;
import edu.iris.dmc.station.conditions.EpochOverlapCondition;
import edu.iris.dmc.station.conditions.EpochRangeCondition;
import edu.iris.dmc.station.conditions.FrequencyCondition;
import edu.iris.dmc.station.conditions.InstrumentCodeUnitsCondition;
import edu.iris.dmc.station.conditions.InstrumentCodeUnitsConditionN;
import edu.iris.dmc.station.conditions.InstrumentSensitivityCondition;
import edu.iris.dmc.station.conditions.LastStageUnitCondition;
import edu.iris.dmc.station.conditions.LocationCodeCondition;
import edu.iris.dmc.station.conditions.MissingDecimationCondition;
import edu.iris.dmc.station.conditions.OrientationCondition;
import edu.iris.dmc.station.conditions.OrientationConditionE;
import edu.iris.dmc.station.conditions.OrientationConditionZ;
import edu.iris.dmc.station.conditions.PolesZerosCondition;
import edu.iris.dmc.station.conditions.PolesZerosSequenceCondition;
import edu.iris.dmc.station.conditions.PolynomialCondition;
import edu.iris.dmc.station.conditions.ResponseListCondition;
import edu.iris.dmc.station.conditions.SampleRateCondition;
import edu.iris.dmc.station.conditions.SensorCondition;
import edu.iris.dmc.station.conditions.StageGainNonZeroCondition;
import edu.iris.dmc.station.conditions.StageGainProductCondition;
import edu.iris.dmc.station.conditions.StageSequenceCondition;
import edu.iris.dmc.station.conditions.StageUnitCondition;
import edu.iris.dmc.station.conditions.StartTimeCondition;
import edu.iris.dmc.station.conditions.StationElevationCondition;
import edu.iris.dmc.station.conditions.UnitCondition;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.ResponsePolynomialRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Rule;

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
					"Network:Code must be assigned a string consisting of 1-2 uppercase A-Z and numeric 0-9 characters."),
					Network.class);
		}
		if (!set.contains(110)) {
			add(110, new StartTimeCondition(true,
					"If Network:startDate is included then it must occur before Network:endDate if included."),
					Network.class);
		}
		if (!set.contains(111)) {
			add(111, new EpochOverlapCondition(true,
					"Station:Epoch cannot be partly concurrent with any other Station:Epoch encompassed in parent Network:Epoch."),
					Network.class);
		}
		if (!set.contains(112)) {
			add(112, new EpochRangeCondition(true,
					"Network:Epoch must encompass all subordinate Station:Epoch"),
					Network.class);
		}
	}

	private void defaultStationRules(Set<Integer> set) {
		String codeRegex = "[A-Z0-9_\\*\\?]{1,5}";
		if (!set.contains(201)) {
			add(201, new CodeCondition(true, codeRegex,
					"Station:Code must be assigned a string consisting of 1-5 uppercase A-Z and numeric 0-9 characters."),
					Station.class);
		}

		if (!set.contains(210)) {
			add(210, new StartTimeCondition(true,
					"Station:startDate must be included and must occur before Station:endDate if included."),
					Station.class);
		}
		if (!set.contains(211)) {
			add(211, new EpochOverlapCondition(true,
					"Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch."),
					Station.class);
		}
		if (!set.contains(221)) {
			add(212, new EpochRangeCondition(true,
					"Station:Epoch must encompass all subordinate Channel:Epoch."),
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
					"Channel:Code must be assigned a string consisting of 3 uppercase A-Z and numeric 0-9 characters."),
					Channel.class);
		}
		if (!set.contains(302)) {
			add(302, new LocationCodeCondition(true, "([A-Z0-9\\*\\ ]{0,2})?",
					"Channel:locationCode must be assigned a string consisting of 0-2 uppercase A-Z and numeric 0-9 characters OR 2 whitespace characters OR --."),
					Channel.class);
		}
		if (!set.contains(303)) {
			add(303, new CalibrationUnitCondition(false, "If CalibrationUnits are included then CalibrationUnits:Name must be assigned a value from the IRIS StationXML Unit dictionary, case inconsistencies trigger warnings."), Channel.class);
		}
		if (!set.contains(304)) {
			add(304, new SensorCondition(true, "Channel:Sensor:Description must be included and assigned a string consisting of 1 <= case insensitive A-Z and numeric 0-9 characters."), Channel.class);
		}
		if (!set.contains(305)) {
			add(305, new SampleRateCondition(false,
					"If Channel:SampleRate equals 0 or is not included then Response must not be included.",
					restrictions), Channel.class);
		}
		if (!set.contains(310)) {
			add(310, new StartTimeCondition(true,
					"Channel:startDate must be included and must occur before Channel:endDate if included."),
					Channel.class);
		}
		if (!set.contains(320)) {
			add(320, new AzimuthDipCondition(true,
					"If Channel:Code[2]==(H | L | M | N) THEN Channel:Azimuth and Channel:Dip must be included.",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }),
					Channel.class);
		}
		if (!set.contains(321)) {
			add(321, new InstrumentCodeUnitsCondition(true,
					"If Channel:Code[2] == (H | L) then Stage[1]:InputUnit must equal ?m/s AND Stage[Last]:OutputUnits must equal count?",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }),
					Channel.class);
		}
		if (!set.contains(322)) {
			add(322, new InstrumentCodeUnitsConditionN(true,
					"If Channel:Code[2] == (N) then Stage[1]:InputUnit must equal ?m/s**2 AND Stage[Last]:OutputUnits must equal count?",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }),
					Channel.class);
		}
		if (!set.contains(330)) {
			add(330, new BandCodeCondition(true,
					"Channel:SampleRate must be assigned a float that inclusively correspounds to Channel:Code[1], which is outlined by the Band Code dictionary",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }),
					Channel.class);
		}
		if (!set.contains(332)) {
			add(332, new OrientationCondition(true,
					"If Channel:Code[LAST]==N then Channel:Azimuth must be assigned (>=355.0 or <=5.0) or (>=175.0 and <=185.0) and Channel:Dip must be assigned (>=-5 AND <=5.0).",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }), Channel.class);
		}
		
		if (!set.contains(333)) {
			add(333, new OrientationConditionE(true,
					"If Channel:Code[LAST]==E then Channel:Azimuth must be assigned (>=85.0 and <=95.0) or (>=265.0 and <=275.0) and Channel:Dip must be assigned (>=-5 and <=5.0).",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }), Channel.class);
		}
		
		if (!set.contains(334)) {
			add(334, new OrientationConditionZ(true,
					"If Channel:Code[LAST]==Z then Channel:Azimuth must be assigned (>=355.0 or <=5.0) and Channel:Dip must be assigned (>=-85.0 and <=-90.0) or (>=85.0 and <=90.0).",
					new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }), Channel.class);
		}
	}

	private void defaultResponseRules(Set<Integer> s) {

		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

		if (!s.contains(401)) {
			add(401, new StageSequenceCondition(true,
					"Stage:number must start at 1 and be sequential.",
					restrictions), Response.class);
		}
		if (!s.contains(402)) {
			add(402, new UnitCondition(true,
					"Stage[N]:InputUnits:Name and Stage[N]:OutputUnits:Name must be assigned a value from the IRIS StationXML Unit dictionary, case inconsistencies trigger warnings.",
					restrictions), Response.class);
		}
		if (!s.contains(403)) {
			add(403, new StageUnitCondition(true, "If length(Stage) > 1 then Stage[N]:InputUnits:Name must equal the previously assigned Stage[M]:OutputUnits:Name.",
					restrictions), Response.class);
		}
		if (!s.contains(404)) {
			add(404, new DigitalFilterCondition(true,
					"If Stage[N]:PolesZeros:PzTransferFunctionType:Digital or Stage[N]:FIR or Stage[N]:Coefficients:CfTransferFunctionType:DIGITAL are included then Stage[N] must include Stage[N]:Decimation and Stage[N]:StageGain elements.",
					restrictions), Response.class);
		}
		if (!s.contains(405)) {
			add(405, new ResponseListCondition(true,
					"Stage:ResponseList cannot be the only stage included in a response.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		}
		if (!s.contains(406)) {
			add(406, new LastStageUnitCondition(true,
					"Stage[LAST]::OutputUnits:Name must be assigned count(s)",
					restrictions), Response.class);
		}
		if (!s.contains(410)) {
			add(410, new EmptySensitivityCondition(true, "If InstrumentSensitivity is included then InstrumentSensitivity:Value must be assigned a double > 0.0 ",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(411)) {
			add(411, new FrequencyCondition(true,
					"If InstrumentSensitivity is included then InstrumentSensitivity:Frequency must be less than Channel:SampleRate/2 [Nyquist Frequency]. ",
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
			add(413, new StageGainNonZeroCondition(true, "Stage[1:N]:StageGain must be included and Stage[1:N]:StageGain:Value must be assigned a double > 0.0 and Stage[1:N]:StageGain:Frequency must be assigned a double.",
					new ChannelCodeRestriction(), new ResponsePolynomialRestriction(), new ChannelTypeRestriction()),
					Response.class);
		}
		if (!s.contains(414)) {
			add(414, new PolesZerosCondition(false,
					"If Stage[N]:PolesZeros contains Zero:Real==0 and Zero:Imaginary==0 then InstrumentSensitivity:Frequency cannot equal 0 and Stage[N]:StageGain:Frequency cannot equal 0.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class); 
		}
		if (!s.contains(415)) {
			add(415, new PolynomialCondition(false,
					"Response must include InstrumentPolynomial if a Polynomial stage is included.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		}
		if (!s.contains(416)) {
			add(416, new InstrumentSensitivityCondition(false,
					"Response must include InstrumentSensitivity if no Polynomial stages are included.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		}
		//if (!s.contains(417)) {
		//	add(417, new PolesZerosSequenceCondition(false,
		//			"If Stage[N]:PolesZeros contains Zeros and Poles then Zero:Number and Pole:Number must start at 0 and be sequential.",
		//			new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);
		//}
		
		if (!s.contains(420)) {
			add(420, new MissingDecimationCondition(true,
					"A Response must contain at least one instance of Response:Stage:Decimation.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(421)) {
			add(421, new DecimationSampleRateCondition(true,
					"Stage[LAST]:Decimation:InputSampleRate divided by Stage[LAST]:Decimation:Factor must equal Channel:SampleRate.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(422)) {
			add(422, new DecimationCondition(true,
					"Stage[N]:Decimation:InputSampleRate must equal the previously assigned Stage[M]:Decimation:InputSampleRate divided by Stage[M]:Decimation:Factor.",
					new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
					Response.class);
		}
		if (!s.contains(423)) {
			add(423, new DecimationStageGainCondition(true,
					"If Stage[N]:Decimation and Stage[N]:StageGain are included then Stage[N]:PolesZeros or Stage[N]:Coefficients or Stage[N]:ResponseList or Stage[N]:FIR must also be included.",
					restrictions),
					Response.class);
		}
		if (!s.contains(424)) {
			add(424, new DecimationStageUnitCondition(true,
					"If Stage[N]:Decimation is included then Stage[N]:OutputUnits:Name must equal count(s)",
					restrictions),
					Response.class);
		}
		if (!s.contains(425)) {
			add(425, new DecimationAnalogFilterCondition(true,
					"IF Stage[N]:PolesZeros:PzTransferFunctionType:LAPLACE (RADIANS/SECOND) OR Stage[N]:PolesZeros:PzTransferFunctionType:LAPLACE (HERTZ) OR\n" + 
					"Stage[N]:CoefficientsType:CfTransferFunctionType:ANALOG (RADIANS/SECOND) OR Stage[N]:CoefficientsType:CfTransferFunctionType:ANALOG (HERTZ) are included\n" + 
					"then Stage[N]:Decimation must not be included",
					restrictions),
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
