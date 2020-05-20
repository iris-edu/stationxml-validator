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

public class Condition111Test3 {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void success() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F3_111.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			EpochOverlapCondition condition = new EpochOverlapCondition(true, "");
			
			Message result = condition.evaluate(n);
			NestedMessage nestedMessage=(NestedMessage)result;
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Sta: 2405 2018-01-17T23:10:56 2018-03-29T21:37:57 epoch overlaps"));
			assertTrue(nestedMessage.getNestedMessages().get(1).getDescription().contains("Sta: 2405 2018-03-27T13:39:35 2018-03-29T21:37:57 epoch overlaps"));
			
			Message resultS = condition.evaluate(s);
			NestedMessage nestedMessages=(NestedMessage)resultS;
			System.out.println(nestedMessages.getNestedMessages().get(0).getDescription());

			assertTrue(nestedMessages.getNestedMessages().get(0).getDescription().contains("Chan: DP1 Loc:  2018-01-17T23:02:28 2018-01-26T16:12:38 epoch overlaps with DP1"));

			
		}

	}

	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);

			EpochOverlapCondition condition = new EpochOverlapCondition(true, "");

			Message result = condition.evaluate(s);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}
}
