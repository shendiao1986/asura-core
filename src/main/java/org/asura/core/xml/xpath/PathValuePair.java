package org.asura.core.xml.xpath;

public class PathValuePair {
	private String value;
	private IPathNode pathNode;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IPathNode getPathNode() {
		return this.pathNode;
	}

	public void setPathNode(IPathNode pathNode) {
		this.pathNode = pathNode;
	}

	public String toString() {
		return this.pathNode.toPathString() + ":" + this.value;
	}
}
