package org.asura.core.util;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	
	private Document doc;

	public XMLParser(String fileName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public XMLParser(String xml, String encoding) {
		encoding = "UTF8";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(new ByteArrayInputStream(xml.getBytes(encoding)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NodeList getNodeList(String path) {
		Node root = this.doc.getFirstChild();
		NodeList find = null;
		try {
			XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression xe = xp.compile(path);
			find = (NodeList) xe.evaluate(root, XPathConstants.NODESET);
		} catch (Exception localException) {
		}
		return find;
	}

	public String[] getPathValues(String path) {
		Node root = this.doc.getFirstChild();
		NodeList find = null;
		try {
			XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression xe = xp.compile(path);
			find = (NodeList) xe.evaluate(root, XPathConstants.NODESET);
		} catch (Exception localException) {
		}
		if (find == null) {
			return new String[0];
		}

		String[] values = new String[find.getLength()];
		for (int i = 0; i < values.length; ++i) {
			values[i] = find.item(i).getTextContent();
		}

		return values;
	}

	public String getPathValue(String path) {
		String[] ss = getPathValues(path);
		if (ss.length > 0) {
			return ss[0];
		}

		return "";
	}
}
