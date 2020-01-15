package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.StageGainProductCondition;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class Condition412Pass2 {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void pass1() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("P2_412.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			StageGainProductCondition condition = new StageGainProductCondition(true, "");

			Message result = condition.evaluate(c);

			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	} 
	@Test
	public void pass2() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("P3_412.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			StageGainProductCondition condition = new StageGainProductCondition(true, "");

			Message result = condition.evaluate(c);

			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	}

	@Test
	public void pass3() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			StageGainProductCondition condition = new StageGainProductCondition(true, "");

			Message result = condition.evaluate(c);
			
			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	}
}
