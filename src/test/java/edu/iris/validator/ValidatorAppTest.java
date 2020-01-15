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

public class ValidatorAppTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	String usage="Usage:  [-cVv] [?] [--ignore-warnings] [-f=<format>] [-o=<outputFile>]\n" + 
			"        [--ignore-rules=<ignoreRules>]... [<inputFiles>...] [COMMAND]\n" + 
			"      [<inputFiles>...]   Any number of input files SEED|XML\n" + 
			"      ?, -h, --help       display this help message\n" + 
			"  -c                      Continue execution when a target file fails to parse.\n" + 
			"  -f, --format=<format>   format of the generated report CSV|XML\n" + 
			"      --ignore-rules=<ignoreRules>\n" + 
			"                          list of rules to ignore by the validator\n" + 
			"      --ignore-warnings   do not show warnings, default false\n" + 
			"  -o, --output=<outputFile>\n" + 
			"                          where to output result, default is System.out\n" + 
			"  -v, --verbose           Specify multiple -v options to increase verbosity.\n" + 
			"                          For example, `-v -v -v` or `-vvv`\n" + 
			"  -V, --version           display version info\n" + 
			"Commands:\n" + 
			"  show-rules  Show rules used to validate.\n" + 
			"  show-units  Show units used to validate.";
	@Test
	public void noArgs() throws Exception {
		String[] args = new String[] {};
		exit.expectSystemExitWithStatus(2);

		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertEquals("Missing required [parameter: <inputFiles>\n" + usage, systemErrRule.getLog().trim());
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
				assertEquals("ERROR: File name cannot be empty!\n" + 
						"\n" + 
						"Usage:  [-cVv] [?] [--ignore-warnings] [-f=<format>] [-o=<outputFile>]\n" + 
						"        [--ignore-rules=<ignoreRules>]... [<inputFiles>...] [COMMAND]\n" + 
						"      [<inputFiles>...]   Any number of input files SEED|XML\n" + 
						"      ?, -h, --help       display this help message\n" + 
						"  -c                      Continue execution when a target file fails to parse.\n" + 
						"  -f, --format=<format>   format of the generated report CSV|XML\n" + 
						"      --ignore-rules=<ignoreRules>\n" + 
						"                          list of rules to ignore by the validator\n" + 
						"      --ignore-warnings   do not show warnings, default false\n" + 
						"  -o, --output=<outputFile>\n" + 
						"                          where to output result, default is System.out\n" + 
						"  -v, --verbose           Specify multiple -v options to increase verbosity.\n" + 
						"                          For example, `-v -v -v` or `-vvv`\n" + 
						"  -V, --version           display version info\n" + 
						"Commands:\n" + 
						"  show-rules  Show rules used to validate.\n" + 
						"  show-units  Show units used to validate.", systemErrRule.getLog().trim());
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
	

	
}
