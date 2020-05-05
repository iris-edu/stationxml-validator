package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Message;

public class Condition112Pass {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}
	
	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_112.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			//Station s = n.getStations().get(0);
			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(n);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}

	@Test
	public void pass_EpochRange() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("P1_112.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			//Station s = n.getStations().get(0);
			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(n);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}

	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);

			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(s);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}
}
