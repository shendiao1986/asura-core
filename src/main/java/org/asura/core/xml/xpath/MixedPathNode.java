package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.List;

public class MixedPathNode implements IPathNode {
	public static final String SPLITER = ":";
	private List<ElementPathNode> parents;
	private IPathNode current;

	public MixedPathNode() {
		this.parents = new ArrayList<>();
	}

	public void addParentNode(String nodePath) {
		this.parents.add(new ElementPathNode(nodePath));
	}

	public List<ElementPathNode> getParents() {
		return this.parents;
	}

	public void setParents(List<ElementPathNode> parents) {
		this.parents = parents;
	}

	public IPathNode getCurrent() {
		return this.current;
	}

	public void setCurrent(IPathNode current) {
		this.current = current;
	}

	public MixedPathNode fromPathString(String path) throws XmlPathException {
		if (path != null) {
			String[] ps = path.split(":");
			if (ps.length > 1) {
				MixedPathNode node = new MixedPathNode();
				for (int i = 0; i < ps.length - 1; ++i) {
					node.addParentNode(ps[i]);
				}
				node.setCurrent(PathParsers.getInstance().parse(ps[(ps.length - 1)]));

				return node;
			}
		}
		return null;
	}

	public String toPathString() {
		String path = "";
		for (ElementPathNode node : this.parents) {
			path = path + ":" + node.getElementName();
		}
		if (path.length() > 0) {
			path = path.substring(":".length());
		}
		path = path + ":" + this.current.toPathString();

		return path;
	}

	public PathNodes[] divider(PathNodes nodes) {
		List<PathNodes> list = new ArrayList<>();
		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof MixedPathNode) {
				PathNodes pns = new PathNodes();
				pns.addPathNode(node);
				list.add(pns);
			}
		}
		return list.toArray(new PathNodes[0]);
	}
}