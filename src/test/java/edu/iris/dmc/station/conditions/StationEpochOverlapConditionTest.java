package edu.iris.dmc.station.conditions;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Message;

public class StationEpochOverlapConditionTest {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {


	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_211.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Network n = theDocument.getNetwork().get(0);
			assertNotNull(n.getStations());
			Station s = n.getStations().get(0);
			EpochOverlapCondition condition = new EpochOverlapCondition(true,
					"Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch.");
			Message result = condition.evaluate(s);
			assertFalse(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}
}
