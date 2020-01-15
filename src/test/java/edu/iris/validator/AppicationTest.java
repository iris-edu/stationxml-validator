package edu.iris.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import edu.iris.validator.Application;
import edu.iris.validator.ResourceUtil;
import edu.iris.validator.rules.UnitTable;

public class AppicationTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	String usage = "Usage:  [-V] [?] [COMMAND]\n" + "      ?, -h, --help   display this help message\n"
			+ "  -V, --version       display version info\n" + "Commands:\n"
			+ "  show-rules  Show rules used to validate.\n" + "  show-units  Show units used to validate.\n"
			+ "  validate\n" + "  convert";

	@Test
	public void noArgs() throws Exception {
		String[] args = new String[] {};
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals("Missing required command\n" + usage, systemErrRule.getLog().trim());
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
				assertEquals("Unmatched argument at index 0: ''\n" + usage, systemErrRule.getLog().trim());
			}
		});

		Application.main(args);
	}

	@Test
	public void h() throws Exception {
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals(usage, systemOutRule.getLog().trim());
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
				assertEquals(usage, systemOutRule.getLog().trim());
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
				assertEquals(usage, systemOutRule.getLog().trim());
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

	String convertUsage = "Usage:  convert [-Vv] [?] [-o=<outputFile>] <inputFile>\n"
			+ "      <inputFile>     File to convert SEED|XML\n" + "      ?, -h, --help   display this help message\n"
			+ "  -o, --output=<outputFile>\n" + "                      where to output result, default is System.out\n"
			+ "  -v, --verbose       Specify multiple -v options to increase verbosity.\n"
			+ "                      For example, `-v -v -v` or `-vvv`\n"
			+ "  -V, --version       display version info";

	@Test
	public void convertHelp() throws Exception {
		String[] args = new String[] { "convert", "-h" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals(convertUsage, systemOutRule.getLog().trim());
			}
		});

		Application.main(args);
	}

	@Test
	public void convertVerbose() throws Exception {
		String[] args = new String[] { "convert", "-v" };
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals("Missing required parameter: <inputFile>\n" + convertUsage, systemErrRule.getLog().trim());
			}
		});

		Application.main(args);
	}

	String validateUsage = "Usage:  validate [-cVv] [?] [--ignore-warnings] [-f=<format>] [-o=<outputFile>]\n"
			+ "                 [--ignore-rules=<ignoreRules>]... <inputFiles>...\n"
			+ "      <inputFiles>...     Any number of input files SEED|XML\n"
			+ "      ?, -h, --help       display this help message\n"
			+ "  -c, --continueonerror   Continue execution when a traget file fails to parse.\n"
			+ "  -f, --format=<format>   format of the generated report CSV|XML\n"
			+ "      --ignore-rules=<ignoreRules>\n"
			+ "                          list of rules to ignore by the validator\n"
			+ "      --ignore-warnings   do not show warnings, default false\n" + "  -o, --output=<outputFile>\n"
			+ "                          where to output result, default is System.out\n"
			+ "  -v, --verbose           Specify multiple -v options to increase verbosity.\n"
			+ "                          For example, `-v -v -v` or `-vvv`\n"
			+ "  -V, --version           display version info";

	@Test
	public void validateHelp() throws Exception {
		String[] args = new String[] { "validate", "-h" };
		exit.expectSystemExitWithStatus(0);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals(validateUsage, systemOutRule.getLog().trim());
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
				assertEquals("Missing required parameter: <inputFiles>\n" + validateUsage,
						systemErrRule.getLog().trim());
			}
		});

		Application.main(args);
	}

}
