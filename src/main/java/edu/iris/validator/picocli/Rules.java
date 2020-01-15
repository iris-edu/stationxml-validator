package edu.iris.validator.picocli;

import java.util.List;

import edu.iris.validator.Application;
import edu.iris.validator.rules.Rule;
import edu.iris.validator.rules.RuleEngineRegistry;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "show-rules", description = "Show rules used to validate.")
public class Rules implements Runnable {
	@ParentCommand
	private Application parent;

	@Override
	public void run() {
		List<Rule>list=new RuleEngineRegistry(null).getRules();
		for(Rule r:list) {
			System.out.print(String.format("%-8s %s%n",r.getId(),r.getDescription()));
		}
	}
}
