package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.List;

public class AttributePathNode implements IPathNode {
	public static final String AT = "@";
	private String attribute;

	public AttributePathNode() {
	}

	public AttributePathNode(String attribute) {
		this.attribute = attribute;
	}

	public String getAttribute() {
		return this.attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public AttributePathNode fromPathString(String path) throws XmlPathException {
		if ((path != null) && (path.startsWith("@"))) {
			String attr = path.substring(1);
			if (attr.length() > 0) {
				AttributePathNode node = new AttributePathNode();
				node.setAttribute(attr);
				return node;
			}
		}

		return null;
	}

	public String toPathString() {
		return "@" + this.attribute;
	}

	public PathNodes[] divider(PathNodes nodes) {
		List<PathNodes> list = new ArrayList<>();
		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof AttributePathNode) {
				PathNodes pns = new PathNodes();
				pns.addPathNode(node);

				list.add(pns);
			}
		}

		return list.toArray(new PathNodes[0]);
	}
}
