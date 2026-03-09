package edu.iris.dmc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.testutil.ProcessTest;

public class mainTestContinueOnError {

		// This class test the main method and its outputs. 
	    
		@Test
		public void mainTest() throws Exception{
			URL url = mainTestContinueOnError.class.getClassLoader().getResource("continueonerror");
			String[] args = new String[] {"--verbose","--continue-on-error", url.getPath()};
			ProcessTest.ExecResult r = ProcessTest.runJavaMain(edu.iris.dmc.Application.class, args);
	        System.out.println("STDOUT:\n" + r.getStdout() + "ENDSTDOUT");
			System.out.println("STDERR:\n" + r.getStderr() + "ENDSTDERR");
		    boolean content1  = r.getStderr().contains("[SEVERE] edu.iris.dmc.Application exitWithError: ");
			boolean content1a  = r.getStderr().contains("edu.iris.dmc.seed.io.RecordInputStream.getRecordLength(RecordInputStream.java:89)");
		    boolean content2  = r.getStderr().contains("org.xml.sax.SAXParseException; lineNumber: 622; columnNumber: 3;");
		    boolean content3  = r.getStderr().contains("\n");
		    assertTrue(content1);
			assertTrue(content1a);
		    assertTrue(content2);
		    assertTrue(content3);
		}
		
		@Test
		public void unitsOutput() throws Exception{
			ProcessTest.ExecResult result = ProcessTest.runMethod(Application.class, "printUnits");
		    boolean content1  = result.getStdout().contains("Table of Acceptable Units");
		    boolean content2  = result.getStdout().contains("meter              us             kPa       nanotesla");
			assertTrue(content1);
		    assertTrue(content2);
		
		}
		
		@Test
		public void rulesMessage() throws Exception{
			ProcessTest.ExecResult result = ProcessTest.runMethod(Application.class, "printRules");
		    boolean content1  = result.getStdout().contains("StationXML Validation Rule List ");
		    boolean content2  = result.getStdout().contains("Indices: (N AND M) > 1 AND (N > M)");
		    boolean content3  = result.getStdout().contains("101      Network:Code must be assigned");
		    assertTrue(content1);
		    assertTrue(content2);
		    assertTrue(content3);

		}
		
		@Test
		public void helpMessage() throws Exception{
			ProcessTest.ExecResult result = ProcessTest.runMethod(Application.class, "help");
		    boolean content1  = result.getStdout().contains("FDSN StationXML Validator");
		    boolean content2  = result.getStdout().contains("Version");
		    boolean content3  = result.getStdout().contains("--ignore-warnings    : don't show warnings");
		    assertTrue(content1);
		    assertTrue(content2);
		    assertTrue(content3);

			
		}
		
	}
