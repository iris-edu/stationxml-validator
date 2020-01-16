package edu.iris.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Assertions;

import edu.iris.validator.ResourceUtil;
import edu.iris.validator.rules.UnitTable;

public class AppicationTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Test
	public void noArgs() throws Exception {
		String[] args = new String[] {};
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemErrRule.getLog().trim().contains("Missing required command\n"));
			}
		});

		Application.main(args);
	}

	@Test
	public void emptyArgs() throws Exception {
		String[] args = new String[] { "" };
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemErrRule.getLog().trim().contains("Unmatched argument at index 0: ''\n"));
			}
		});

		Application.main(args);
	}

	@Test
	public void h() throws Exception {
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemOutRule.getLog().trim().contains("Usage"));
			}
		});

		Application.main(new String[] { "-h" });
	}

	@Test
	public void h2() throws Exception {
		String[] args = new String[] { "?" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemOutRule.getLog().trim().contains("Usage"));
			}
		});

		Application.main(args);
	}

	@Test
	public void mainHelp() throws Exception {
		String[] args = new String[] { "--help" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemOutRule.getLog().trim().contains("Usage"));
			}
		});

		Application.main(args);
	}

	@Test
	public void rules() throws Exception {
		String rules = ResourceUtil.fileToString("show-rules.msg");
		String[] args = new String[] { "show-rules" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals(rules.trim(), systemOutRule.getLog().trim());
			}
		});

		Application.main(args);
	}

	@Test
	public void units() throws Exception {
		String[] args = new String[] { "show-units" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals(UnitTable.format(4), systemOutRule.getLog().trim());
			}
		});

		Application.main(args);
	}

	@Test
	public void convertHelp() throws Exception {
		String[] args = new String[] { "convert", "-h" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemOutRule.getLog().trim().contains("Usage"));
			}
		});

		Application.main(args);
	}

	

	public void validateHelp() throws Exception {
		String[] args = new String[] { "validate", "-h" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemOutRule.getLog().trim().contains("Usage"));
			}
		});

		Application.main(args);
	}

	@Test
	public void validateVerbose() throws Exception {
		String[] args = new String[] { "validate", "-v" };
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertTrue(systemErrRule.getLog().trim().contains("Missing required parameter:"));
			}
		});

		Application.main(args);
	}
	@Test
	public void xmlxsd_ExpectedUnmarshalException() throws Exception {
	
		String[] args = new String[] { "validate", getClass().getClassLoader().getResource("xmlVSxsd.xml").getPath() };
		exit.expectSystemExitWithStatus(1);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {System.out.println(systemErrRule.getLog());
				assertTrue(systemErrRule.getLog().trim().contains("Exception parsing file: xmlVSxsd.xml"));
			}
		});

		Application.main(args);
		
		/*Assertions.assertThrows(StationxmlException.class, () -> {
			theDocument = unmarshal("xmlVSxsd.xml");
		});*/

	}
}
