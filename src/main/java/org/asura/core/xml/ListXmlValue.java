package org.asura.core.xml;

import java.util.ArrayList;
import java.util.List;

import org.asura.core.util.StringUtil;
import org.asura.core.xml.xpath.LeveledXmlSettings;
import org.asura.core.xml.xpath.PathParsers;
import org.asura.core.xml.xpath.PathValuePair;

public class ListXmlValue implements IXmlValue {
	private String key;
	private List<IXmlValue> list;

	public ListXmlValue(String key) {
		this.key = key;
		this.list = new ArrayList<>();
	}

	public void addXmlValue(IXmlValue value) {
		this.list.add(value);
	}

	public List<IXmlValue> getList() {
		return this.list;
	}

	public void setList(List<IXmlValue> list) {
		this.list = list;
	}

	public void build(LeveledXmlSettings settings, XmlNode parent, String parentKey) {
		List<PathValuePair> list = new ArrayList<>();

		for (IXmlValue value : this.list) {
			if (value instanceof StringXmlValue) {
				PathValuePair pair = new PathValuePair();
				pair.setValue(((StringXmlValue) value).getValue());
				pair.setPathNode(settings.getPathNode(parentKey));

				list.add(pair);
			}
		}

		settings.processPairs(list, parent);

		for (IXmlValue value : this.list) {
			if (value instanceof StringXmlValue) {
				continue;
			}

			if (!(StringUtil.isNullOrEmpty(parentKey))) {
				XmlNode node = PathParsers.getInstance().parse(settings.getPathNode(parentKey + "-" + this.key), null,
						parent);
				value.build(settings, node, parentKey + "-" + this.key);
			} else {
				XmlNode node = PathParsers.getInstance().parse(settings.getPathNode(this.key), null, parent);
				value.build(settings, node, this.key);
			}
		}
	}
}
