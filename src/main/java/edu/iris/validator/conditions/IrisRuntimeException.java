package edu.iris.validator.conditions;

public class IrisRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2700954144562971270L;

	public IrisRuntimeException(String message) {
		super(message);
	}

	public IrisRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
