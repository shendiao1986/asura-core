package org.asura.core.xml;

public class XmlBuilder {
	public static void main(String[] args) {
		long s = System.currentTimeMillis();

		for (int m = 0; m < 1000; ++m) {
			XmlNode builder = new XmlNode("songs");
			for (int i = 1; i < 11; ++i) {
				XmlNode node = new XmlNode("song" + i);
				builder.addChild(node);
				for (int j = 0; j < 5; ++j) {
					XmlNode attr = new XmlNode("attri" + j);
					attr.setText("value" + j);
					node.addChild(attr);
				}
			}

			builder.toXml();
		}

		System.out.println("time:" + (System.currentTimeMillis() - s));
	}
}
