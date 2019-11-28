package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asura.core.xml.XmlNode;

public class LeveledXmlSettings {
	private HashMap<String, String> map;
	private static final String ROOT = "root";

	public LeveledXmlSettings() {
		this.map = new HashMap<>();
		this.map.put(ROOT, ROOT);
	}

	public void addMapping(String valuePath, String nodePath) {
		this.map.put(valuePath, nodePath);
	}

	public void setRootPath(String path) {
		this.map.put(ROOT, path);
	}

	public IPathNode getRootPathNode() {
		return PathParsers.getInstance().parse((String) this.map.get(ROOT));
	}

	public IPathNode getPathNode(String valueKey) {
		String path = (String) this.map.get(valueKey);
		return PathParsers.getInstance().parse(path);
	}

	public void processPairs(List<PathValuePair> pairs, XmlNode parent) {
		PathNodes nodes = new PathNodes();
		Map<String, PathValuePair> map = new HashMap<>();
		for (PathValuePair pair : pairs) {
			map.put(pair.getPathNode().toPathString(), pair);
			nodes.addPathNode(pair.getPathNode());
		}

		PathNodes[] pns = PathParsers.getInstance().divide(nodes);
		for (PathNodes pn : pns) {
			if (pn.size() == 1) {
				PathParsers.getInstance().parse(pn.getPathNodes()[0],
						((PathValuePair) map.get(pn.getPathNodes()[0].toPathString())).getValue(), parent);
			} else {
				List<PathValuePair> list = new ArrayList<>();
				for (IPathNode n : pn.getPathNodes()) {
					list.add(map.get(n.toPathString()));
				}
				PathParsers.getInstance().parse(list.toArray(new PathValuePair[0]), parent);
			}
		}
	}
}
