package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.List;

public class PathNodes {
	private List<IPathNode> paths;

	public PathNodes() {
		this.paths = new ArrayList<>();
	}

	public void addPathNode(IPathNode pathNode) {
		this.paths.add(pathNode);
	}

	public IPathNode[] getPathNodes() {
		return ((IPathNode[]) this.paths.toArray(new IPathNode[0]));
	}

	public int size() {
		return this.paths.size();
	}
}
