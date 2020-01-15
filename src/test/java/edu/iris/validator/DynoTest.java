package edu.iris.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import edu.iris.validator.Application;

public class DynoTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
	
	@Test
	public void convertVerbose() throws Exception {
		String[] args = new String[] { "convert" };
		exit.expectSystemExitWithStatus(1);

		/*exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals("Missing required parameter: <inputFile>\n" , systemErrRule.getLog().trim());
			}
		});*/

		Application.main(args);
	}

}
