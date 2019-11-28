package org.asura.core.exception;

public class InvalidParamException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidParamException() {
	}

	public InvalidParamException(String message) {
		super(message);
	}
	
	public InvalidParamException(Exception e) {
		super(e);
	}
}
