package edu.iris.validator;

import static org.junit.Assert.assertEquals;
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

public class ValidatorAppTest {

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
				System.out.println(systemErrRule.getLog());
				assertTrue(systemErrRule.getLog().trim().contains("Missing required command"));
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
				assertTrue(systemErrRule.getLog().trim().contains("Unmatched argument"));
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
				System.out.println(systemErrRule.getLog());
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

}
