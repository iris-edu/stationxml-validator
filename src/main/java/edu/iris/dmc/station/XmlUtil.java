package edu.iris.dmc.station;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XmlUtil {

	public static Date toDate(String pattern, String text) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.parse(text);
	}

	public static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		GregorianCalendar cal = calendar.toGregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		return cal.getTime();
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(String pattern, String text) throws IOException {
		Date date = null;
		try {
			date = toDate(pattern, text);
		} catch (ParseException e) {
			throw new IOException(e);
		}
		return toXMLGregorianCalendar(date);
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) throws IOException {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		gCalendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
			throw new IOException(ex);
		}
		return xmlCalendar;
	}

	public static String toText(ZonedDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneId.of("UTC"));
		return format.format(dateTime);

	}

	public static String toText(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(date);

	}
}
