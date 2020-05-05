package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;

public class StageUnitConditionTest {

	private FDSNStationXML theDocument;

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_402.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
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
		assertTrue(m instanceof edu.iris.dmc.station.rules.Error);
	}
}
