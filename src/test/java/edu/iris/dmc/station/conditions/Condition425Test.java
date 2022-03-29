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

public class Condition425Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_425.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			DecimationAnalogFilterCondition condition = new DecimationAnalogFilterCondition(true, "", restrictions);
               
			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage) result;
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Stage[01] includes PolesZeros:PzTransferFunctionType:LAPLACE (RADIANS/SECOND) so decimation must not be included in Stage[01]"));
		}

	}
	
	@Test
	public void fail2() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F2_425.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			DecimationAnalogFilterCondition condition = new DecimationAnalogFilterCondition(true, "", restrictions);
               
			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage) result;
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Stage[01] includes PolesZeros:PzTransferFunctionType:LAPLACE (HERTZ) so decimation must not be included in Stage[01]"));
		}

	}
	
	@Test
	public void fail3() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F3_425.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network n = theDocument.getNetwork().get(0);
			Station s = n.getStations().get(0);
			Channel c = s.getChannels().get(0);
			
			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

			DecimationAnalogFilterCondition condition = new DecimationAnalogFilterCondition(true, "", restrictions);
               
			Message result = condition.evaluate(c);
			NestedMessage nestedMessage=(NestedMessage) result;
			System.out.println(nestedMessage.getNestedMessages().get(0).getDescription());
			assertTrue(nestedMessage.getNestedMessages().get(0).getDescription().contains("Stage[04] includes CoefficientsType:CfTransferFunctionType:ANALOG (RADIANS/SECOND) so decimation must not be included in Stage[04]"));
			assertTrue(nestedMessage.getNestedMessages().get(1).getDescription().contains("Stage[10] includes CoefficientsType:CfTransferFunctionType:ANALOG (HERTZ) so decimation must not be included in Stage[10]"));

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
			DecimationStageUnitCondition condition = new DecimationStageUnitCondition(true, "", restrictions);

			Message result = condition.evaluate(c);
			assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}
}
