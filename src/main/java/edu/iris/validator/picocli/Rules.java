package edu.iris.validator.picocli;

import java.util.Collections;
import java.util.Comparator;
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
		Collections.sort(list, Comparator.comparing(Rule::getId));
		//System.out.print(list.get(0));
		//list.get(0);
		System.out.print("---------------------------------\n");
		System.out.print("|StationXML Validation Rule List|\n");
		System.out.print("---------------------------------\n");
		System.out.print("\n");
		System.out.print("Enforces FDSN StationXML Schema Version 1.1 Compliance\n");
		System.out.print("Level 100: Network\n");
		System.out.print("Level 200: Station\n");
		System.out.print("Level 300: Channel\n");
		System.out.print("Level 400: Response\n");
		System.out.print("Error: IF Error==False then document is invalid\n");
		System.out.print("Warning: IF Warning==TRUE then Return message and PASS else PASS\n");
		System.out.print("Epoch=startDate-endDate\n");
		System.out.print("Indices: (N AND M) > 1 AND (N > M)\n");
		System.out.print("\n");
		System.out.print("--------------------------------\n");
		System.out.print("| Rule ID | Description | Type |\n");
		System.out.print("--------------------------------\n");




		for(Rule r:list) {
			System.out.print(String.format("| %-3s | %s | %s |%n",r.getId(),r.getDescription(), r.getresult()));
		}
	}
}
