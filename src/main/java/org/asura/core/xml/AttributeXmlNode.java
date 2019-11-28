package org.asura.core.xml;

import org.jdom2.Attribute;

public class AttributeXmlNode implements IXmlNode {
	private String name;
	private String value;

	public AttributeXmlNode() {
	}

	public AttributeXmlNode(Attribute attribute) {
		this.name = attribute.getName();
		this.value = attribute.getValue();
	}

	public AttributeXmlNode(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toXml() {
		return " " + this.name + "=\"" + this.value + "\" ";
	}
}
