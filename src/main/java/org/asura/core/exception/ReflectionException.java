package org.asura.core.exception;

public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ReflectionException() {
	}

	public ReflectionException(String message) {
		super(message);
	}
	
	public ReflectionException(Exception e) {
		super(e);
	}
}
