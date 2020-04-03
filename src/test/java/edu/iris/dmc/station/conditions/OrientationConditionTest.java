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
import edu.iris.dmc.station.conditions.OrientationCondition;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;

public class OrientationConditionTest {

	private FDSNStationXML theDocument;

	@Test
	public void n() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F1_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Warning);
		}

	}
	@Test
	public void e() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F2_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationConditionE condition = new OrientationConditionE(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Warning);
		}

	}
	@Test
	public void z() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F3_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationConditionZ condition = new OrientationConditionZ(true, "", restrictions);
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Warning);
		}

	}
}
