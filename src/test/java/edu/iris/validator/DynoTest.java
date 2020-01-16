package edu.iris.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class DynoTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
	


}
