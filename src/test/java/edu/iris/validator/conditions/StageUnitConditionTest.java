package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Units;
import edu.iris.validator.conditions.UnitCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class StageUnitConditionTest {

	private FDSNStationXML theDocument;

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_402.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction() };

			UnitCondition condition = new UnitCondition(true, "", restrictions);
			Message result = condition.evaluate(bhz00);

		}
	}

	@Test
	public void singleUnit() throws Exception {
		UnitCondition condition = new UnitCondition(true, "", null);
		Units u = new Units();
		u.setName("COUNTS1");
		u.setDescription("Testing");
		Message m = condition.evaluate(u);
		assertNotNull(m);
		assertTrue(m instanceof edu.iris.validator.rules.Error);
	}
}
