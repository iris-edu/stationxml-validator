package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.conditions.OrientationCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;

public class OrientationConditionTest {

	private FDSNStationXML theDocument;

	@Test
	public void n() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F1_332.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.validator.rules.Warning);
		}

	}
	@Test
	public void e() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F2_332.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.validator.rules.Warning);
		}

	}
	@Test
	public void z() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F3_332.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.validator.rules.Warning);
		}

	}
}
