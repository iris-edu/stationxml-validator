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
import edu.iris.dmc.station.rules.NestedMessage;

public class Condition212Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_212.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(s);
			//assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
			NestedMessage nestedMessage=(NestedMessage)result;
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Chan: BDF Loc: 00 startDate 1951-08-06T00:00:00 cannot occur before Station startDate 2018-08-06T00:00:00"));
			assertTrue(nestedMessage.getNestedMessages().get(1).getDescription().contains("Chan: BDF Loc: 00 endDate 2750-12-31T23:59:59 cannot occur after Station endDate 2500-12-31T23:59:59"));

		}

	}
	
	@Test
	public void fail2() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_212.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			EpochRangeCondition condition = new EpochRangeCondition(true, "");

			Message result = condition.evaluate(s);
			NestedMessage nestedMessage=(NestedMessage)result;

			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Chan: BDF Loc: 00 endDate cannot be null if station endDate is defined as: 2500-12-31T23:59:59"));

			//assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
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
