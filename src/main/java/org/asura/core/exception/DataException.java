package org.asura.core.exception;

public class DataException extends RuntimeException {
	private static final long serialVersionUID = -402377976332647347L;

	public DataException() {
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Exception e) {
		super(e);
	}
}