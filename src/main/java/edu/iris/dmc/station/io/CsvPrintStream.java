package edu.iris.dmc.station.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Warning;

public class CsvPrintStream extends PrintStream implements RuleResultPrintStream {
	// This class controls the validator's output messages. 

	private static final Object[] FILE_HEADER = {"RuleId", "Type", "Network", "Station", "Channel",
			"Location", "StartDate", "EndDate", "Message" };
	private CSVPrinter csvFilePrinter = null;

	public CsvPrintStream(OutputStream out) throws IOException {
		super(out);
		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(System.lineSeparator());
		this.csvFilePrinter = new CSVPrinter(this, csvFileFormat.withQuote(null));
		//this.csvFilePrinter = new CSVPrinter(this, CSVFormat.EXCEL);
	}

	public void printHeader() throws IOException {
		csvFilePrinter.printRecord(FILE_HEADER);
	}
	
	public void printHeader(String text) throws IOException {
		printHeader();
	}

	public void print(Message message) throws IOException {
		print("", message);
	}

	public void print(String source, Message message) throws IOException {
		List<String> record = new ArrayList<>();
		if (message instanceof NestedMessage) {
			NestedMessage nestedMessage =(NestedMessage)message;
			for(Message m:nestedMessage.getNestedMessages()){
				m.setRule(nestedMessage.getRule());
				m.setNetwork(nestedMessage.getNetwork());
				m.setStation(nestedMessage.getStation());
				m.setChannel(nestedMessage.getChannel());
				print(source,m);
			}
		} else {
			//record.add(source);
			record.add(""+message.getRule().getId());
			if (message instanceof Warning) {
				record.add("Warning");
			} else if (message instanceof edu.iris.dmc.station.rules.Error) {
				record.add("Error");
			} else {
				record.add("");
			}
			record.add(message.getNetwork() == null ? null : message.getNetwork().getCode());
			record.add(message.getStation() == null ? null : message.getStation().getCode());
			record.add(message.getChannel() == null ? null : message.getChannel().getCode());
			record.add(message.getChannel() == null ? null : message.getChannel().getLocationCode());

			if (message.getChannel() != null) {
				record.add(message.getChannel() == null ? null : XmlUtil.toText(message.getChannel().getStartDate()));
				record.add(message.getChannel() == null ? null : XmlUtil.toText(message.getChannel().getEndDate()));
			} else if (message.getStation() != null) {
				record.add(XmlUtil.toText(message.getStation().getStartDate()));
				record.add(XmlUtil.toText(message.getStation().getEndDate()));
			} else if (message.getNetwork() != null) {
				record.add(XmlUtil.toText(message.getNetwork().getStartDate()));
				record.add(XmlUtil.toText(message.getNetwork().getEndDate()));
			}
			record.add(message.getDescription());
			csvFilePrinter.printRecord(record);
			csvFilePrinter.flush();
		}
	}

	@Override
	public void printFooter() {

	}

	@Override
	public void printRow(String text) {
		this.println(text);
	}

	@Override
	public void printMessage(String text) {
		this.print(text);
	}

}
