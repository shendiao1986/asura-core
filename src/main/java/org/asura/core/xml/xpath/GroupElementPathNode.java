package org.asura.core.xml.xpath;

import java.util.HashMap;
import java.util.Map;

public class GroupElementPathNode implements IPathNode {
	public static final String SPLITER = "\\|";
	private IPathNode group;
	private IPathNode pathNode;

	public GroupElementPathNode() {
	}

	public GroupElementPathNode(MixedPathNode group, IPathNode pathNode) {
		this.group = group;
		this.pathNode = pathNode;
	}

	public IPathNode getGroup() {
		return this.group;
	}

	public void setGroup(IPathNode group) {
		this.group = group;
	}

	public IPathNode getPathNode() {
		return this.pathNode;
	}

	public void setPathNode(IPathNode pathNode) {
		this.pathNode = pathNode;
	}

	public GroupElementPathNode fromPathString(String path) throws XmlPathException {
		GroupElementPathNode node = null;

		if (path != null) {
			String[] ps = path.split("\\|");

			if (ps.length == 2) {
				node = new GroupElementPathNode();
				node.group = PathParsers.getInstance().parse(ps[0]);
				node.pathNode = PathParsers.getInstance().parse(ps[1]);
			}

		}

		return node;
	}

	public String toPathString() {
		return this.group.toPathString() + "\\|" + this.pathNode.toPathString();
	}

	public PathNodes[] divider(PathNodes nodes) {
		Map<String, PathNodes> map = new HashMap<>();

		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof GroupElementPathNode) {
				GroupElementPathNode gepn = (GroupElementPathNode) node;
				String key = gepn.getGroup().toPathString();
				if (!(map.containsKey(key))) {
					map.put(key, new PathNodes());
				}

				((PathNodes) map.get(key)).addPathNode(gepn);
			}
		}

		return map.values().toArray(new PathNodes[0]);
	}
}
