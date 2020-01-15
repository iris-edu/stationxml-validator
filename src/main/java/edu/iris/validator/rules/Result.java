package edu.iris.validator.rules;

public class Result {

	public static Warning warning(String message) {
		return new Warning(message);
	}

	public static Error error( String message) {
		return new Error(message);
	}

	public static Success success() {
		return new Success(null);
	}

}
