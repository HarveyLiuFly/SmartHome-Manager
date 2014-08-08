package com.esprit.smarthome.devices;

import java.util.List;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.esprit.smarthome.util.PathFinder;

@Stateless
public class DeviceAnnotator implements IDeviceAnnotator {
	static final Log log = LogFactory.getLog(DeviceAnnotator.class);
	private Document document;
	private Element root;
	private PathFinder p = new PathFinder();

	public void init(String srcfile) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			document = documentBuilder.parse(srcfile);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void addAnnotation(List<String> tags, String srcfile) {
		init(srcfile);

		try {
			root = (Element) document.getElementsByTagName("device").item(0);
			Element categories = document.createElement("categories");
			for (String tag : tags) {
				Element category = (Element) categories.appendChild(document
						.createElement("category"));
				category.appendChild(document.createTextNode(tag));
			}
			root.appendChild(categories);
			writeToFile(srcfile);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void writeToFile(String srcfile) {

		try {
			DOMSource source = new DOMSource(document);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			java.io.File file = new java.io.File(srcfile);
			StreamResult result = new StreamResult(p.annotatedPath()
					+ file.getName());
			transformer.transform(source, result);
		} catch (Exception e) {
			log.error(e.getMessage());
			;
		}

	}

	public void removeAnnotation(String tag, String srcfile) {
		init(srcfile);
		NodeList list = root.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if (tag.equals(node.getNodeName())) {
				root.removeChild(node);
			}

		}
		writeToFile(srcfile);

	}

	public void updateAnnotation(List<String> values, String srcfile,
			String protocol) {
		init(srcfile);
		if (protocol.equals("UPnP")) {
			root = (Element) document.getElementsByTagName("device").item(0);
			Node categories = root.getElementsByTagName("categories").item(0);
			for (String value : values) {
				Element category = (Element) categories.appendChild(document
						.createElement("category"));
				category.appendChild(document.createTextNode(value));
			}
		} else if (protocol.equals("DPWS")) {
			root = (Element) document.getElementsByTagName("wsdp:ThisModel")
					.item(0);
			Node categories = root.getElementsByTagName("wsdp:categories")
					.item(0);
			for (String value : values) {
				Element category = (Element) categories.appendChild(document
						.createElement("wsdp:category"));
				category.appendChild(document.createTextNode(value));
			}
		}
		writeToFile(srcfile);

	}

	public void addDPWSAnnotation(List<String> tags, String srcfile) {
		init(srcfile);
		try {
			root = (Element) document.getElementsByTagName("wsdp:ThisModel")
					.item(0);
			Element categories = document.createElement("wsdp:categories");
			for (String tag : tags) {
				Element category = (Element) categories.appendChild(document
						.createElement("wsdp:category"));
				category.appendChild(document.createTextNode(tag));
			}
			root.appendChild(categories);
			writeToFile(srcfile);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
