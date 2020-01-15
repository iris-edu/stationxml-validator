package edu.iris.validator.conditions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.EmptySensitivityCondition;
import edu.iris.validator.conditions.UnitCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.NestedMessage;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class Condition402Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_402.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			Response r = c.getResponse();
			System.out.println(c);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			UnitCondition condition = new UnitCondition(true, "Stage[N]:InputUnits:Name and/or Stage[N]:OutputUnits:Name are not defined in Unit name overview for IRIS StationXML validator.", restrictions);

			Message result = condition.evaluate(c);			
			assertTrue(result instanceof edu.iris.validator.rules.NestedMessage);
			
			NestedMessage nestedMessage=(NestedMessage)result;
			assertNotNull(nestedMessage.getNestedMessages());
			assertEquals(22,nestedMessage.getNestedMessages().size());
			
		}
	}

	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			EmptySensitivityCondition condition = new EmptySensitivityCondition(true, "");

			Message result = condition.evaluate(c);
			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	}
}
