package edu.iris.validator.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.station.io.StationIOUtils;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.validator.conditions.CodeCondition;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.RuleEngineServiceTest;

public class CodeConditionTest {

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
		CodeCondition condition = new CodeCondition(true, "[A-Za-z0-9\\*\\?]{1,2}", "");
		Message result = condition.evaluate(iu);
		assertTrue(result instanceof edu.iris.validator.rules.Success);

		iu.setCode("IIIIII");
		result = condition.evaluate(iu);
		assertTrue(result instanceof edu.iris.validator.rules.Error);

		

	}
}
