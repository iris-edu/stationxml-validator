package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;

public class Condition322Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void azimuthfail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_322.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			InstrumentCodeUnitsConditionN condition = new InstrumentCodeUnitsConditionN(true, "", restrictions);
			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage)result;
 			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Instrument code N should have stage 1 input units similar to ?m/s**2 but input units are m/s"));
 			assertTrue(nestedMessage.getNestedMessages().get(1).getDescription().contains("Instrument code N should have stage last output units similar to count? but output units are m/s"));

		}

	}
	
	@Test
	public void Test2_322() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_322.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			InstrumentCodeUnitsConditionN condition = new InstrumentCodeUnitsConditionN(true, "", restrictions);
			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage)result;			
 			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Instrument code N should have stage last output units similar to count? but output units are m/s"));

		}

	}
	

	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };


			InstrumentCodeUnitsCondition condition = new InstrumentCodeUnitsCondition(true, "", restrictions);

			Message result = condition.evaluate(c);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}
}

