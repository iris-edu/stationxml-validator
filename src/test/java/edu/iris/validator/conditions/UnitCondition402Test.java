package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.Channel;
import edu.iris.station.model.Coefficients;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;
import edu.iris.station.model.Units;
import edu.iris.validator.conditions.StageUnitCondition;
import edu.iris.validator.restrictions.ChannelCodeRestriction;
import edu.iris.validator.restrictions.ChannelTypeRestriction;
import edu.iris.validator.restrictions.Restriction;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;
import edu.iris.validator.rules.Success;

public class UnitCondition402Test {

	private FDSNStationXML theDocument;

	@BeforeEach
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("test.xml")) {
			theDocument = StationIOUtils.stationXmlDocument(is);
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
		assertTrue(result instanceof edu.iris.validator.rules.Error);
		coefficients.setOutputUnits(originalUnits);

		result = condition.evaluate(bhz00);
		assertTrue(result instanceof Success);

	}
}
