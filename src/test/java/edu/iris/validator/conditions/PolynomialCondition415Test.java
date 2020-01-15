package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.validator.conditions.PolynomialCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class PolynomialCondition415Test {

	@Test
	public void pass2() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("P2_415.xml")) {
			FDSNStationXML theDocument = StationIOUtils.stationXmlDocument(is);

			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction()};

			PolynomialCondition condition = new PolynomialCondition(true, "", restrictions);

			Response response = bhz00.getResponse();
			Message result = condition.evaluate(bhz00, response);
			System.out.println(result.getDescription());

			assertTrue(result instanceof edu.iris.validator.rules.Success);
		}

	}
	
	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_415.xml")) {
			FDSNStationXML theDocument = StationIOUtils.stationXmlDocument(is);

			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction()};

			PolynomialCondition condition = new PolynomialCondition(true, "", restrictions);

			Response response = bhz00.getResponse();
			Message result = condition.evaluate(bhz00, response);

			assertTrue(result instanceof edu.iris.validator.rules.Error);
		}

	}
}
