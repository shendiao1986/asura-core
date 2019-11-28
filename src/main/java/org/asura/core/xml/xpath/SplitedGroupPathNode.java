package org.asura.core.xml.xpath;

import java.util.HashMap;
import java.util.Map;

public class SplitedGroupPathNode implements IPathNode {
	private IPathNode group;
	private IPathNode innerGroup;
	private String spliter;
	private IPathNode pathNode;

	public IPathNode getGroup() {
		return this.group;
	}

	public void setGroup(IPathNode group) {
		this.group = group;
	}

	public IPathNode getInnerGroup() {
		return this.innerGroup;
	}

	public void setInnerGroup(IPathNode innerGroup) {
		this.innerGroup = innerGroup;
	}

	public String getSpliter() {
		return this.spliter;
	}

	public void setSpliter(String spliter) {
		this.spliter = spliter;
	}

	public IPathNode getPathNode() {
		return this.pathNode;
	}

	public void setPathNode(IPathNode pathNode) {
		this.pathNode = pathNode;
	}

	public IPathNode fromPathString(String path) throws XmlPathException {
		SplitedGroupPathNode node = null;
		if (path != null) {
			String[] ps = path.split("\\|");

			if (ps.length == 4) {
				node = new SplitedGroupPathNode();
				node.group = PathParsers.getInstance().parse(ps[0]);
				node.innerGroup = PathParsers.getInstance().parse(ps[1]);
				node.spliter = ps[2].trim();
				node.pathNode = PathParsers.getInstance().parse(ps[3]);
			}
		}
		return node;
	}

	public String toPathString() {
		return this.group.toPathString() + "\\|" + this.innerGroup.toPathString() + "\\|" + this.spliter + "\\|"
				+ this.pathNode.toPathString();
	}

	public PathNodes[] divider(PathNodes nodes) {
		Map<String, PathNodes> map = new HashMap<>();

		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof SplitedGroupPathNode) {
				SplitedGroupPathNode gepn = (SplitedGroupPathNode) node;
				String key = gepn.getGroup().toPathString() + ":" + gepn.getInnerGroup().toPathString();
				if (!(map.containsKey(key))) {
					map.put(key, new PathNodes());
				}
				map.get(key).addPathNode(gepn);
			}
		}

		return map.values().toArray(new PathNodes[0]);
	}
}