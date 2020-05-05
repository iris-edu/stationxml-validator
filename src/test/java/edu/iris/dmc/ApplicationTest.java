package edu.iris.dmc;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;

public class ApplicationTest {

	@Test
	public void testPass() throws Exception{
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		URL url = ApplicationTest.class.getClassLoader().getResource("pass.xml");
		assertEquals("file", url.getProtocol());
		CommandLine commandLine = new CommandLine().setContinueError(true).setLogLevel(Level.INFO).setIgnoreWarnings(true).setInput(new File(url.toURI()).toPath());
		Application application = new Application(commandLine);
		application.run("csv", outContent);

		String output = new String(outContent.toByteArray());
		assertTrue(output.contains("PASSED"));
	}

	@Test
	public void testFail() throws Exception{
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		URL url = ApplicationTest.class.getClassLoader().getResource("continueonerror/fail.xml");
		assertEquals("file", url.getProtocol());
		CommandLine commandLine = new CommandLine().setContinueError(true).setLogLevel(Level.INFO).setIgnoreWarnings(true).setInput(new File(url.toURI()).toPath());
		AtomicReference<Exception> ref = new AtomicReference<>();
		Application application = new Application(commandLine) {
			@Override
			protected void error(Exception e) {
			  ref.set(e);
			}
		};
		application.run("csv", outContent);

		String output = new String(outContent.toByteArray());
		assertTrue(output.isEmpty());
		assertTrue(ref.get() != null);
		assertTrue(ref.get().getMessage().contains("The element type \"Station\" must be terminated by the matching end-tag \"</Station>\""));
	}
}
