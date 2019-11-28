package org.asura.core.exception;

public class DataParserException extends Exception {
	private static final long serialVersionUID = 5035539725679239105L;

	public DataParserException() {
	}

	public DataParserException(String message) {
		super(message);
	}

	public DataParserException(Exception e) {
		super(e);
	}
}
