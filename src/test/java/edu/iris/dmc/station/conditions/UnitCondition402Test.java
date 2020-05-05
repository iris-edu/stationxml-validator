package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Coefficients;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Success;

public class UnitCondition402Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("test.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
		}
	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		Network iu = theDocument.getNetwork().get(0);
		Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
		
		StageUnitCondition condition = new StageUnitCondition(true, "",restrictions);
		Response response = bhz00.getResponse();
		Message result = condition.evaluate(bhz00);
		assertTrue(result instanceof Success);

		List<ResponseStage> stages = response.getStage();
		ResponseStage stage = stages.get(1);
		assertEquals(2, stage.getNumber().intValue());
		Coefficients coefficients = stage.getCoefficients();
		Units originalUnits = coefficients.getOutputUnits();
		Units units = new Units();
		units.setName("Dummy");
		units.setDescription("Dummy");
		coefficients.setOutputUnits(units);

		result = condition.evaluate(bhz00);
		//assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		coefficients.setOutputUnits(originalUnits);

		result = condition.evaluate(bhz00);
		assertTrue(result instanceof Success);

	}
}
