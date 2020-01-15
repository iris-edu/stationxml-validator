package edu.iris.validator.conditions;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.EpochOverlapCondition;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class StationEpochOverlapConditionTest {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {


	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_211.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
			Network n = theDocument.getNetwork().get(0);
			assertNotNull(n.getStations());
			Station s = n.getStations().get(0);
			EpochOverlapCondition condition = new EpochOverlapCondition(true,
					"Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch.");
			Message result = condition.evaluate(s);
			System.out.println(result.getDescription());
			assertTrue(result instanceof edu.iris.validator.rules.Error);
		}

	}
}
