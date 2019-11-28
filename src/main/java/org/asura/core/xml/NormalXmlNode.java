package org.asura.core.xml;

import org.jdom2.Element;

public class NormalXmlNode implements IXmlNode {
	private String nodeName;
	private String value;

	public NormalXmlNode() {
	}

	public NormalXmlNode(Element element) {
		this.nodeName = element.getName();
		this.value = element.getValue();
	}

	public NormalXmlNode(String nodeName, String value) {
		this.nodeName = nodeName;
		this.value = value;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toXml() {
		return "<" + this.nodeName + ">" + "<![CDATA[" + this.value + "]]>" + "</" + this.nodeName + ">";
	}
}
