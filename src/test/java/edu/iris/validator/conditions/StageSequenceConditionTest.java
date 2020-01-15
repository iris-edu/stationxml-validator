package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.validator.conditions.StageSequenceCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;
import edu.iris.validator.rules.Success;

public class StageSequenceConditionTest {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("stage_sequence_test.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
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
