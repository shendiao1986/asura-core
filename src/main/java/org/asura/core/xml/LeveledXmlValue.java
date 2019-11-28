package org.asura.core.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.asura.core.util.StringUtil;
import org.asura.core.xml.xpath.LeveledXmlSettings;
import org.asura.core.xml.xpath.PathParsers;
import org.asura.core.xml.xpath.PathValuePair;

public class LeveledXmlValue implements IXmlValue {
	private HashMap<String, IXmlValue> map;

	public LeveledXmlValue() {
		this.map = new HashMap<>();
	}

	public void addXmlValue(String key, IXmlValue value) {
		this.map.put(key, value);
	}

	public void addStringXmlValue(String key, String value) {
		this.map.put(key, new StringXmlValue(value));
	}

	public String toXmlString(LeveledXmlSettings settings) {
		XmlNode root = PathParsers.getInstance().parse(settings.getRootPathNode(), null, null);
		build(settings, root, "");

		return root.toXml();
	}

	public void build(LeveledXmlSettings settings, XmlNode parent, String parentKey) {
		List<PathValuePair> list = new ArrayList<>();

		for (String key : this.map.keySet()) {
			IXmlValue value = (IXmlValue) this.map.get(key);
			if (value instanceof StringXmlValue) {
				if (!(StringUtil.isNullOrEmpty(parentKey))) {
					key = parentKey + "-" + key;
				}
				PathValuePair pair = new PathValuePair();
				pair.setValue(((StringXmlValue) value).getValue());
				pair.setPathNode(settings.getPathNode(key));

				list.add(pair);
			}
		}
		settings.processPairs(list, parent);

		for (String key : this.map.keySet()) {
			IXmlValue value = (IXmlValue) this.map.get(key);
			if (!(StringUtil.isNullOrEmpty(parentKey))) {
				key = parentKey + "-" + key;
			}

			if (value instanceof StringXmlValue) {
				continue;
			}
			XmlNode node = PathParsers.getInstance().parse(settings.getPathNode(key), null, parent);
			value.build(settings, node, key);
		}
	}
}
