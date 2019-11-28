package org.asura.core.xml.xpath;

import java.util.ArrayList;
import java.util.List;

import org.asura.core.util.cache.SimpleCache;
import org.asura.core.xml.XmlNode;

public class PathParsers {
	private List<IPathParser> parsers;
	private List<IMultiPathParser> multiParsers;
	private List<IPathNode> nodes;
	private static PathParsers instance;
	private SimpleCache<String, IPathNode> cache;

	private PathParsers() {
		this.parsers = new ArrayList<>();
		this.multiParsers = new ArrayList<>();
		this.nodes = new ArrayList<>();
		this.cache = new SimpleCache<>(100);
		initial();
	}

	public static synchronized PathParsers getInstance() {
		if (instance == null) {
			instance = new PathParsers();
		}

		return instance;
	}

	private void initial() {
		this.parsers.add(new AttributePathParser());
		this.parsers.add(new ElementPathParser());
		this.parsers.add(new MixedPathParser());

		this.multiParsers.add(new NormalMultiPathParser());
		this.multiParsers.add(new SplitedMultiPathParser());

		this.nodes.add(new AttributePathNode());
		this.nodes.add(new ElementPathNode());
		this.nodes.add(new MixedPathNode());
		this.nodes.add(new GroupElementPathNode());
		this.nodes.add(new SplitedGroupPathNode());
	}

	public PathNodes[] divide(PathNodes nodes) {
		List<PathNodes> list = new ArrayList<>();
		for (IPathNode node : this.nodes) {
			PathNodes[] pns = node.divider(nodes);
			for (PathNodes ns : pns) {
				list.add(ns);
			}
		}

		return list.toArray(new PathNodes[0]);
	}

	public XmlNode parse(IPathNode pathNode, String value, XmlNode parent) {
		for (IPathParser parser : this.parsers) {
			XmlNode node = parser.parser(pathNode, value, parent);
			if (node != null) {
				return node;
			}
		}

		return null;
	}

	public void parse(PathValuePair[] pairs, XmlNode parent) {
		for (IMultiPathParser parser : this.multiParsers)
			parser.parse(pairs, parent);
	}

	public IPathNode parse(String path) {
		IPathNode result = null;
		if (this.cache.get(path) == null) {
			for (IPathNode node : this.nodes) {
				try {
					result = node.fromPathString(path);
				} catch (XmlPathException e) {
					break;
				}

				if (result != null) {
					break;
				}

			}

			this.cache.cache(path, result);
		}

		result = (IPathNode) this.cache.get(path);

		if (result == null) {
			throw new XmlPathException("can not parse path '" + path + "'");
		}

		return result;
	}
}
