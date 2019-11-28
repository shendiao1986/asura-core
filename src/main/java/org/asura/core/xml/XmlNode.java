package org.asura.core.xml;

import java.util.Iterator;

import org.asura.core.util.StringUtil;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class XmlNode implements IXmlNode {
	private Element element;

	public XmlNode(String name) {
		this.element = new Element(name);
	}

	public void setText(String text) {
		this.element.setText(text);
	}

	public void addChild(XmlNode node) {
		this.element.addContent(node.element);
	}

	public void addAttribute(String name, String value) {
		this.element.setAttribute(name, value);
	}

	public String toXml() {
		return getElementString(this.element);
	}

	private String getElementString(Element element) {
		String xml = "<";
		xml = xml + element.getName();

		for (Iterator<Attribute> its = element.getAttributes().iterator(); its.hasNext();) {
			Attribute at = its.next();
			xml = xml + new AttributeXmlNode(at).toXml();
		}

		xml = xml + ">";

		if (!(StringUtil.isNullOrEmpty(element.getText()))) {
			xml = xml + "<![CDATA[" + element.getText() + "]]>";
		}

		for (Iterator<Element> its = element.getChildren().iterator(); its.hasNext();) {
			Element obj = its.next();
			xml = xml + getElementString(obj);
		}

		xml = xml + "</" + element.getName() + ">";

		return xml;
	}

}
