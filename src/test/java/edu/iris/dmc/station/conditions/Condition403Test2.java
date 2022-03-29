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

public class Condition403Test2 {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_403.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			StageUnitCondition condition = new StageUnitCondition(true, "", restrictions);

			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage)result;
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Stage[03] input unit Volts must equal Stage[01] output unit V"));
			assertTrue(nestedMessage.getNestedMessages().get(1).getDescription().contains("Stage[05] input unit C must equal Stage[04] output unit COUNTS"));

		}

	}


		@Test
		public void pass() throws Exception {
			try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
				theDocument = DocumentMarshaller.unmarshal(is);
				Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

				Network n = theDocument.getNetwork().get(0);
				Station s = n.getStations().get(0);
				Channel c = s.getChannels().get(0);
				StageUnitCondition condition = new StageUnitCondition(true, "", restrictions);

				Message result = condition.evaluate(c);
				assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
			}

		}
}
