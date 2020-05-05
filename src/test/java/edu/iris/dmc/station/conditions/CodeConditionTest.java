package edu.iris.dmc.station.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Message;

public class CodeConditionTest {

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
		CodeCondition condition = new CodeCondition(true, "[A-Za-z0-9\\*\\?]{1,2}", "");
		Message result = condition.evaluate(iu);
		assertTrue(result instanceof edu.iris.dmc.station.rules.Success);

		iu.setCode("IIIIII");
		result = condition.evaluate(iu);
		assertTrue(result instanceof edu.iris.dmc.station.rules.Error);

		

	}
}
