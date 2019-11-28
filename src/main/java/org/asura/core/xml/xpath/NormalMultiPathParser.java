package org.asura.core.xml.xpath;

import org.asura.core.xml.XmlNode;

public class NormalMultiPathParser implements IMultiPathParser {
	
	public void parse(PathValuePair[] pairs, XmlNode parent) {
		if ((pairs.length <= 0) || (!(pairs[0].getPathNode() instanceof GroupElementPathNode)))
			return;
		GroupElementPathNode node = (GroupElementPathNode) pairs[0].getPathNode();
		parent = PathParsers.getInstance().parse(node.getGroup(), null, parent);
		for (PathValuePair pair : pairs) {
			PathParsers.getInstance().parse(((GroupElementPathNode) pair.getPathNode()).getPathNode(), pair.getValue(),
					parent);
		}
	}
	
}
