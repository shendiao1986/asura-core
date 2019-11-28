package org.asura.core.xml.xpath;

import org.asura.core.xml.XmlNode;

public class AttributePathParser implements IPathParser {
	public XmlNode parser(IPathNode pathNode, String value, XmlNode parent) {
		if (pathNode instanceof AttributePathNode) {
			AttributePathNode attr = (AttributePathNode) pathNode;
			parent.addAttribute(attr.getAttribute(), value);

			return parent;
		}

		return null;
	}
}
