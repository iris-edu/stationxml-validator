package edu.iris.dmc;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.station.exceptions.StationxmlException;

public class DocumentMarshaller {

	private DocumentMarshaller() {
	}

	public static FDSNStationXML unmarshal(InputStream inputStream) throws JAXBException, SAXException, IOException, StationxmlException {
		Unmarshaller jaxbUnmarshaller = unmarshaller();
		try {
			return (FDSNStationXML) jaxbUnmarshaller.unmarshal(inputStream);
		}catch(javax.xml.bind.UnmarshalException e) {
			if(e.getCause() != null && e.getCause() instanceof org.xml.sax.SAXParseException) {
			System.out.println("XML Document does not comply with the FDSN-StationXML xsd schema. \n"+ 
			"Error occurs in the StationXML Document and is described by the line below (refer to trace for line #):");
			    throw new StationxmlException(e);
			}else {
			    throw new StationxmlException(e);
			}
		}
	}

	public static FDSNStationXML unmarshalString(String inputStream) throws JAXBException, SAXException, IOException {
		Unmarshaller jaxbUnmarshaller = unmarshaller();
		return (FDSNStationXML) jaxbUnmarshaller.unmarshal(new StringReader(inputStream));
	}

	public static Unmarshaller unmarshaller() throws JAXBException, SAXException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		try (InputStream stream = DocumentMarshaller.class.getClassLoader().getResourceAsStream("station.1.1.xsd");) {
			Schema stationSchema = sf.newSchema(new StreamSource(stream));
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(stationSchema);
			return unmarshaller;
		}
	}
}
