package org.asura.core.xml;

public interface IXmlNode {
	public static final String START_LEFT_ANGLE = "<";
	public static final String END_LEFT_ANGLE = "</";
	public static final String RIGHT_ANGLE = ">";
	public static final String DATA_START = "<![CDATA[";
	public static final String DATA_END = "]]>";

	public abstract String toXml();
}
