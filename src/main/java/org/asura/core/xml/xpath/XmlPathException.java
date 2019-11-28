package org.asura.core.xml.xpath;

public class XmlPathException extends RuntimeException {
	private static final long serialVersionUID = -1116657224061400665L;

	public XmlPathException() {
	}

	public XmlPathException(String message) {
		super(message);
	}

	public XmlPathException(Exception e) {
		super(e);
	}
}
