/* Java test*/

package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;

public class F2410 {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}
	
	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_410.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);
			

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction() };

			EmptySensitivityCondition condition = new  EmptySensitivityCondition(true, "", restrictions);
			Response response = bhz00.getResponse();
			Message result = condition.evaluate(bhz00, response);
			System.out.println(result.getDescription());

			assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
			
		}

	}
}