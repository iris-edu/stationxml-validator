package edu.iris.dmc;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.exceptions.StationxmlException;

public class MarshallTest {

	@Test
	public void umarshall() throws Exception{
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_101.xml")) {
			DocumentMarshaller.unmarshaller().unmarshal(is);
		}
	}


@Test
public void xmlxsdfailure() throws Exception{
	try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("xmlVSxsd.xml")) {
		assertThrows(StationxmlException.class, () -> {
			try {	
		    DocumentMarshaller.unmarshaller().unmarshal(is);
	    }catch(javax.xml.bind.UnmarshalException e) {
			//System.out.println("XML XSD Validation ERROR!", e);
			e.printStackTrace();
	    	throw new StationxmlException(String.format("XML XSD Validation ERROR!"), e); 	
	   }
	});
  }
 }
}