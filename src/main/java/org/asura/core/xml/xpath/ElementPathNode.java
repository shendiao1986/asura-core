package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.List;

public class ElementPathNode implements IPathNode {
	private String elementName;

	public ElementPathNode() {
	}

	public ElementPathNode(String elementName) {
		this.elementName = elementName;
	}

	public String getElementName() {
		return this.elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public ElementPathNode fromPathString(String path) throws XmlPathException {
		PathUtils.checkPathName(path);

		return new ElementPathNode(path);
	}

	public String toPathString() {
		return this.elementName;
	}

	public PathNodes[] divider(PathNodes nodes) {
		List<PathNodes> list = new ArrayList<>();
		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof ElementPathNode) {
				PathNodes pns = new PathNodes();
				pns.addPathNode(node);
				list.add(pns);
			}
		}
		return list.toArray(new PathNodes[0]);
	}
}