package org.asura.core.xml.xpath;

import org.asura.core.xml.XmlNode;

public interface IPathParser {
	public XmlNode parser(IPathNode paramIPathNode, String paramString, XmlNode paramXmlNode);
}
