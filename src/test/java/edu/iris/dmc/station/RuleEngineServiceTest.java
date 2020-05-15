package edu.iris.dmc.station;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.station.exceptions.StationxmlException;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;

public class RuleEngineServiceTest {

	private String datePattern = "yyyy-MM-dd'T'HH:mm:ss";

	private FDSNStationXML theDocument;
	private RuleEngineService ruleEngineService = new RuleEngineService(false, null);

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			for (Network network : theDocument.getNetwork()) {
				ruleEngineService.executeNetworkRules(network);
			}
		}
	}

	@Test
	public void rule101() throws Exception {

		theDocument = unmarshal("F1_101.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);
		assertNotNull(m);

		Set<Message> s = m.get(101);
		assertEquals(1, s.size());
		assertEquals(101, s.iterator().next().getRule().getId());
	}

	@Test
	public void rule110() throws Exception {

		theDocument = unmarshal("F1_110.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(110);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(110, message.getRule().getId());
	}

	@Test
	public void rule111() throws Exception {

		theDocument = unmarshal("F1_111.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(111);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(111, message.getRule().getId());
	}

	@Test
	public void rule112() throws Exception {

		theDocument = unmarshal("F1_112.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(112);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(112, message.getRule().getId());
	}

	@Test
	public void rule201() throws Exception {

		theDocument = unmarshal("F1_201.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(201);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(201, message.getRule().getId());
	}

	@Test
	public void rule210() throws Exception {

		theDocument = unmarshal("F1_210.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(210);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(210, message.getRule().getId());
	}

	@Test
	public void rule211() throws Exception {

		theDocument = unmarshal("F1_211.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(211);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(211, message.getRule().getId());
	}

	@Test
	public void rule212() throws Exception {

		theDocument = unmarshal("F1_212.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(212);
		assertNotNull(s);
		assertEquals(4, s.size());
		Message message = s.iterator().next();
		assertEquals(212, message.getRule().getId());
	}

	@Test
	public void rule222() throws Exception {

		theDocument = unmarshal("F1_222.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(222);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(222, message.getRule().getId());
	}

	@Test
	public void rule223() throws Exception {

		theDocument = unmarshal("F1_223.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(223);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(223, message.getRule().getId());
	}

	@Test
	public void rule304() throws Exception {

		theDocument = unmarshal("F1_304.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(304);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(304, message.getRule().getId());
	}
	
	@Test
	public void rule320() throws Exception {

		theDocument = unmarshal("F1_320.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(320);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(320, message.getRule().getId());
	}
	
	@Test
	public void rule321() throws Exception {

		theDocument = unmarshal("F1_321.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(321);
		assertNotNull(s);
		assertEquals(2, s.size());
		Message message = s.iterator().next();
		assertEquals(321, message.getRule().getId());
	}



	@Test
	public void rule402() throws Exception {

		theDocument = unmarshal("F1_402.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		for (Map.Entry<Integer, Set<Message>> e : m.entrySet()) {
			System.out.println(e.getKey());
			Set<Message> set = e.getValue();
			for (Message mmm : set) {
				if (mmm instanceof NestedMessage) {
					NestedMessage v = (NestedMessage) mmm;
					for (Message l : v.getNestedMessages()) {
						System.out.println(l);
					}
				}
			}
		}
		Set<Message> s = m.get(402);

		assertNotNull(s);
		assertEquals(22, s.size());
		Message message = s.iterator().next();
		assertEquals(402, message.getRule().getId());
	}

	@Test
	public void rule405() throws Exception {

		theDocument = unmarshal("F1_405.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(405);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(405, message.getRule().getId());
	}

	@Test
	public void rule410() throws Exception {

		theDocument = unmarshal("F1_410.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(410);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(410, message.getRule().getId());

		theDocument = unmarshal("F2_410.xml");

		m = ruleEngineService.executeAllRules(theDocument);

		s = m.get(410);
		assertNotNull(s);
		assertEquals(1, s.size());

	}

	@Test
	public void rule411() throws Exception {

		theDocument = unmarshal("F1_411.xml");

		Map<Integer, Set<Message>> resultSet = ruleEngineService.executeAllRules(theDocument);
		System.out.println(resultSet.toString());


		assertEquals(2, resultSet.size());

	}

	@Test
	public void rule412() throws Exception {
		theDocument = unmarshal("F1_412.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);
        
		Set<Message> s = m.get(412);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(412, message.getRule().getId());

	}

	@Test
	public void rule413() throws Exception {

		theDocument = unmarshal("F1_413.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(413);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(413, message.getRule().getId());

	}

	@Test
	public void rule414() throws Exception {
		theDocument = unmarshal("F1_414.xml");

		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(414);
		assertNotNull(s);
		assertEquals(2, s.size());
		Message message = s.iterator().next();
		assertEquals(414, message.getRule().getId());

	}

	@Test
	public void rule421() throws Exception {

		theDocument = unmarshal("F1_421.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(421);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(421, message.getRule().getId());

	}

	@Test
	public void rule422() throws Exception {

		theDocument = unmarshal("F1_422.xml");
		Map<Integer, Set<Message>> m = ruleEngineService.executeAllRules(theDocument);

		Set<Message> s = m.get(422);
		assertNotNull(s);
		assertEquals(1, s.size());
		Message message = s.iterator().next();
		assertEquals(422, message.getRule().getId());

	}

	@Test
	public void xmlxsd_ExpectedUnmarshalException() throws Exception {
		Assertions.assertThrows(StationxmlException.class, () -> {
			theDocument = unmarshal("xmlVSxsd.xml");
		});

	}

	private FDSNStationXML unmarshal(String file) throws IOException, StationxmlException {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream(file)) {
			return DocumentMarshaller.unmarshal(is);
		} catch (SAXException | JAXBException e) {
			throw new IOException(e);
		}
	}

}
