package edu.iris.validator.picocli;

import edu.iris.validator.Application;
import edu.iris.validator.rules.UnitTable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "show-units", description = "Show units used to validate.")
public class Units implements Runnable {

	@ParentCommand
	private Application parent;

	@Override
	public void run() {
		System.out.print(UnitTable.format(4));
	}
}
