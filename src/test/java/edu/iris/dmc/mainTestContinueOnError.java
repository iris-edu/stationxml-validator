package edu.iris.dmc;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;

import org.junit.Rule;



import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.Application;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.exceptions.StationxmlException;

public class mainTestContinueOnError {

		// This class test the main method and its outputs. 
	    
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream newContent = new ByteArrayOutputStream();

		
		@Test
		public void mainTest() throws Exception{
		     PrintStream ps = new PrintStream(outContent);
		     PrintStream nerrps = new PrintStream(newContent);
		     System.setOut(ps);
		     System.setErr(nerrps);
			 URL url = mainTestContinueOnError.class.getClassLoader().getResource("continueonerror");
			 String[] args = new String[] {"--verbose","--continue-on-error", url.getPath()};
			 Application.main(args);
	         System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	         String sysout = outContent.toString().replaceAll( "\r", "" );
	         System.setErr( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	         String syserr = newContent.toString().replaceAll( "\r", "" );
	         outContent.close();
	         newContent.close();
	         nerrps.close();
	         ps.close();
	         //System.out.println(sysout.toString());
	         //System.out.println(syserr.toString());
		     boolean content1  = syserr.toString().contains("[SEVERE] edu.iris.dmc.Application exitWithError: edu.iris.dmc.seed.io.RecordInputStream.getRecordLength(RecordInputStream.java:89)");
		     boolean content2  = syserr.toString().contains("[SEVERE] edu.iris.dmc.Application exitWithError: XML Document does not comply with the FDSN-StationXML xsd schema.");
		     boolean content3  = syserr.toString().contains("\n");
		     assertTrue(content1==true);
		     assertTrue(content2==true);
		     assertTrue(content3==true);     	     
		}
		
		
		@Test
		public void version() throws Exception{
			boolean versionnumber = Application.getVersion().contains("1.6.0");
			assertTrue(versionnumber==true);
		
		}
		
		@Test
		public void unitsOutput() throws Exception{
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
		    Application.printUnits();
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	        String sysout = outContent.toString().replaceAll( "\r", "" );
	        ps.close();
	        outContent.close();
		    boolean content1  = sysout.toString().contains("Table of Acceptable Units");
		    boolean content2  = sysout.toString().contains("meter              us     hectopascal              nT");
		    assertTrue(content1==true);
		    assertTrue(content2==true);
		
		}
		
		@Test
		public void rulesMessage() throws Exception{
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
			Application.printRules();
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	        String sysout = outContent.toString().replaceAll( "\r", "" );
	        ps.close();
	        outContent.close();
		    boolean content1  = sysout.toString().contains("StationXML Validation Rule List ");
		    boolean content2  = sysout.toString().contains("Indices: (N AND M) > 1 AND (N > M)");
		    boolean content3  = sysout.toString().contains("101      Network:Code must be assigned");

		    assertTrue(content1==true);
		    assertTrue(content2==true);
		    assertTrue(content3==true);

		}
		
		@Test
		public void helpMessage() throws Exception{
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
			Application.help();
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	        String sysout = outContent.toString().replaceAll( "\r", "" );
	        ps.close();
	        outContent.close();
		    boolean content1  = sysout.toString().contains("FDSN StationXml Validator ");
		    boolean content2  = sysout.toString().contains("Version 1.6.0.2-SNAPSHOT");
		    boolean content3  = sysout.toString().contains("--ignore-warnings    : don't show warnings");

		    assertTrue(content1==true);
		    assertTrue(content2==true);
		    assertTrue(content3==true);

			
		}
		
	}
