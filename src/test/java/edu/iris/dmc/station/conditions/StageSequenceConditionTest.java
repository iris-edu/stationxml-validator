package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Success;

public class StageSequenceConditionTest {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("stage_sequence_test.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
		}
	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		Network iu = theDocument.getNetwork().get(0);
		Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
		StageSequenceCondition condition = new StageSequenceCondition(true, "",restrictions);

		Message result = condition.evaluate(bhz00,bhz00.getResponse());
		System.out.println(result);
		assertTrue(result instanceof Success);
		
	}
}
