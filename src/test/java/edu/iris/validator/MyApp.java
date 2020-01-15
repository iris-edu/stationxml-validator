package edu.iris.validator;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "myapp", mixinStandardHelpOptions = true, version = "1.0")
class MyApp implements Callable<Integer> {

    @Option(names = "-x") int x;

    @Override
    public Integer call() { // business logic
        System.out.printf("x=%s%n", x);
        return 123; // exit code
    }

    public static void main(String... args) { // bootstrap the application
        System.exit(new CommandLine(new MyApp()).execute(args));
    }
}