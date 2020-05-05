package edu.iris.dmc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;

import org.junit.jupiter.api.Test;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;

public class MainTest {

		// This class test the main method and its outputs. 
	    
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream newContent = new ByteArrayOutputStream();

		
		@Test
		public void mainTest() throws Exception{
		     PrintStream ps = new PrintStream(outContent);
		     PrintStream nerrps = new PrintStream(newContent);
		     System.setOut(ps);
		     System.setErr(nerrps);
			 URL url = MainTest.class.getClassLoader().getResource("pass.xml");
			 String[] args = new String[] {"--verbose","--ignore-warnings", "--input", url.getPath()};
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
		     boolean content1  = syserr.toString().contains("edu.iris.dmc.Application read: Input file:");
		     boolean content2  = sysout.toString().contains("PASSED");
		     boolean content3  = sysout.toString().contains("\n");
		     assertTrue(content1==true);
		     assertTrue(content2==true);
		     assertTrue(content3==true);     	     
		}
		
		@Test
		@ExpectSystemExitWithStatus(0)
		public void help() throws Exception{
			//URL url = FileConverterRunner.class.getClassLoader().getResource("CU_ANWB_BH2.xml");
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
			String[] args = new String[] {"--help"};
	 		Application.main(args);	
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	 		outContent.close();
	 		ps.close();
				
		}
		
		@Test
		@ExpectSystemExitWithStatus(0)
		public void rules() throws Exception{
			//URL url = FileConverterRunner.class.getClassLoader().getResource("CU_ANWB_BH2.xml");
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
			String[] args = new String[] {"--rules"};
	 		Application.main(args);	
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	 		outContent.close();
	 		ps.close();
				
		}

		@Test
		@ExpectSystemExitWithStatus(0)
		public void units() throws Exception{
			//URL url = FileConverterRunner.class.getClassLoader().getResource("CU_ANWB_BH2.xml");
		    PrintStream ps = new PrintStream(outContent);
		    System.setOut(ps);
			String[] args = new String[] {"--units"};
	 		Application.main(args);	
	        System.setOut( new PrintStream( new FileOutputStream( FileDescriptor.out ) ) );
	 		outContent.close();
	 		ps.close();
				
		}		
		
	}
