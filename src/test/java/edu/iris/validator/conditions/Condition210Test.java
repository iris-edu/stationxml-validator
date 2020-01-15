package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.EpochRangeCondition;
import edu.iris.validator.conditions.StartTimeCondition;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class Condition210Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void F1_210() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_210.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			StartTimeCondition condition = new StartTimeCondition(true, "");

			Message result = condition.evaluate(s);
			assertTrue(result instanceof edu.iris.validator.rules.Error);
		}

	}

	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);

			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(s);
			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	}
}
