package org.asura.core.xml.xpath;

import org.asura.core.xml.XmlNode;

public class ElementPathParser implements IPathParser {
	public XmlNode parser(IPathNode pathNode, String value, XmlNode parent) {
		if (pathNode instanceof ElementPathNode) {
			ElementPathNode element = (ElementPathNode) pathNode;
			XmlNode node = new XmlNode(element.getElementName());
			node.setText(value);
			parent.addChild(node);
			return node;
		}
		return null;
	}
}
