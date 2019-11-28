package org.asura.core.xml;

import org.asura.core.xml.xpath.IPathNode;
import org.asura.core.xml.xpath.LeveledXmlSettings;
import org.asura.core.xml.xpath.PathParsers;

public class StringXmlValue implements IXmlValue {
	private String value;

	public StringXmlValue() {
	}

	public StringXmlValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void build(LeveledXmlSettings settings, XmlNode parent, String parentKey) {
		IPathNode node = settings.getPathNode(parentKey);
		PathParsers.getInstance().parse(node, this.value, parent);
	}
}