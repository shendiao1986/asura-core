package org.asura.core.xml.xpath;

import org.asura.core.xml.XmlNode;

public class MixedPathParser implements IPathParser {
	public XmlNode parser(IPathNode pathNode, String value, XmlNode parent) {
		if (pathNode instanceof MixedPathNode) {
			MixedPathNode mix = (MixedPathNode) pathNode;
			XmlNode node = null;
			if (mix.getParents() != null) {
				for (ElementPathNode epn : mix.getParents()) {
					if (node == null) {
						node = new XmlNode(epn.getElementName());
						parent.addChild(node);
					} else {
						XmlNode temp = new XmlNode(epn.getElementName());
						node.addChild(temp);
						node = temp;
					}
				}
			}
			if (node != null) {
				parent = node;
			}
			return PathParsers.getInstance().parse(mix.getCurrent(), value, parent);
		}
		return null;
	}
}
