package edu.iris.validator.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.XmlUtil;

public class HtmlPrintStream extends PrintStream implements RuleResultPrintStream {

	public HtmlPrintStream(OutputStream out) {
		super(out);
	}

	public void printHeader(String text) throws IOException {
		printHeader();
	}

	public void printHeader() {
		this.println("<!DOCTYPE html><html>");
		this.println("<head>");
		this.println("<title>IRIS: DMC Web Services</title>");
		this.println(
				"<link rel=\"stylesheet\" media=\"all\" type=\"text/css\" href=\"https://service.iris.edu/static/css/iris.css\" />");
		this.println(
				"<link rel=\"stylesheet\" media=\"print\" type=\"text/css\" href=\"https://service.iris.edu/static/css/print.css\" />");
		this.println("<link rel=\"shortcut icon\" href=\"https://service.iris.edu/static/img/favicon.ico\" />");

		this.println("</head>");
		this.println("<body>");

		this.println("<header>");
		this.println("<div id=\"header\">");
		this.println("<div id=\"header-left\">");
		this.println("<div id=\"logo\">");
		this.println("<a href=\"http://ds.iris.edu/hq/\" title=\"IRIS\">");
		this.println(
				"<img alt=\"IRIS\" src=\"https://service.iris.edu/static/img/layout/logos/iris_logo_shadow.png\"/>");
		this.println("</a>");
		this.println("</div>");
		this.println("<div id=\"logo-name\">");
		this.println("Incorporated Research Institutions for Seismology");
		this.println("</div>");
		this.println("</div>");
		this.println("<!-- #header-right -->");
		this.println("<div id=\"header-right\">");
		this.println("<ul id=\"top-menu\">");
		this.println("<li>");
		this.println("<a href=\"http://ds.iris.edu/ds/nodes/dmc/\" title=\"IRIS DMC\">DMC</a>");
		this.println("</li>");
		this.println("<li>");
		this.println("<a href=\"http://www.iris.edu/hq\" title=\"IRIS HQ\">Home</a>");
		this.println("</li>");
		this.println("</ul>");
		this.println("<div id=\"nsf-logo\">");
		this.println("<a href=\"http://www.nsf.gov/\" target=\"_blank\" title=\"The National Science Foundation\">");
		this.println(
				"<img alt=\"The National Science Foundation\" src=\"https://service.iris.edu/static/img/layout/logos/nsf.png\"/>");
		this.println("</a>");
		this.println("</div>");
		this.println("<!-- #nsf -->");
		this.println("</div>");
		this.println("<div class=\"shadow-down\"></div>");
		this.println("</div>");
		this.println("<!-- #header -->");
		this.println("<!--Iris only meny  -->");
		this.println("</header>");

		this.println("<TABLE class=\"table table-striped table-bordered table-condensed\">");

		this.println("<thead>");

		this.print("<TR>");
		this.print("<TH>");
		this.print("RuleId");
		this.print("</TH>");

		this.print("<TH>");
		this.print("Network");
		this.print("</TH>");

		this.print("<TH>");
		this.print("Station");
		this.print("</TH>");

		this.print("<TH>");
		this.print("Channel");
		this.print("</TH>");

		this.print("<TH>");
		this.print("Location");
		this.print("</TH>");

		this.print("<TH>");
		this.print("StartDate");
		this.print("</TH>");

		this.print("<TH>");
		this.print("EndDate");
		this.print("</TH>");

		this.print("<TH>");
		this.print("Message");
		this.print("</TH>");
		this.print("</TR>");
		this.print("</thead>");
	}

	public void print(Message result) {
		print("", result);
	}

	public void print(String source, Message result) {
		this.println("<TR>");
		this.print("<TD>");
		this.print(result.getRule().getId());
		this.print("</TD>");

		this.print("<TD>");
		if (result.getNetwork() != null) {
			this.print(result.getNetwork().getCode());
		}
		this.print("</TD>");

		this.print("<TD>");
		if (result.getStation() != null) {
			this.print(result.getStation().getCode());
		}
		this.print("</TD>");

		this.print("<TD>");
		if (result.getChannel() != null) {
			this.print(result.getChannel().getCode());
		}
		this.print("</TD>");

		this.print("<TD>");
		if (result.getChannel() != null) {
			this.print(result.getChannel().getLocationCode());
		}
		this.print("</TD>");

		if (result.getChannel() != null) {
			this.print("<TD>");
			this.print(XmlUtil.toText(result.getChannel().getStartDate()));
			this.print("</TD>");

			this.print("<TD>");
			this.print(XmlUtil.toText(result.getChannel().getEndDate()));
			this.print("</TD>");
		} else if (result.getStation() != null) {
			this.print("<TD>");
			this.print(XmlUtil.toText(result.getStation().getStartDate()));
			this.print("</TD>");

			this.print("<TD>");
			this.print(XmlUtil.toText(result.getStation().getEndDate()));
			this.print("</TD>");
		} else if (result.getNetwork() != null) {
			this.print("<TD>");
			this.print(XmlUtil.toText(result.getNetwork().getStartDate()));
			this.print("</TD>");

			this.print("<TD>");
			this.print(XmlUtil.toText(result.getNetwork().getEndDate()));
			this.print("</TD>");
		}

		this.print("<TD>");
		this.print(result.getDescription());
		this.print("</TD>");
		this.println("</TR>");

	}

	public void printRow(String text) {
		this.println("<TD COLSPAN=\"8\">");
		this.print(text);
		this.print("</TD>");
		this.println("</TR>");
	}

	public void printFooter() {
		this.println("</TABLE>");
		this.println("</BODY></HTML>");
	}

	@Override
	public void printMessage(String text) throws IOException {
		// TODO Auto-generated method stub

	}

}
