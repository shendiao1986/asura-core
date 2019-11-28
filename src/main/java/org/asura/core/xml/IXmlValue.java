package org.asura.core.xml;

import org.asura.core.xml.xpath.LeveledXmlSettings;

public interface IXmlValue {
	public static final String KEY_JOIN = "-";

	public void build(LeveledXmlSettings paramLeveledXmlSettings, XmlNode paramXmlNode, String paramString);
}
