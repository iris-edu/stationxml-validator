package edu.iris.validator.exceptions;

public class StationxmlException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6968498161740847487L;

	private byte[] bytes;

	public StationxmlException(Exception e) {
		this(e, null);
	}

	public StationxmlException(Exception e, byte[] bytes) {
		super(e);
		this.bytes = bytes;
	}

	public StationxmlException(String message) {
		super(message);
	}

	public StationxmlException(String message, Exception e) {
		super(message, e);
	}

	public StationxmlException(String message, Throwable e) {
		super(message, e);
	}


	public byte[] getBytes() {
		return bytes;
	}

}
