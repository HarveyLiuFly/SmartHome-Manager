package com.esprit.smarthome.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.esprit.smarthome.devices.Device;

public class Test {

	public static void main(String[] args) throws SAXException, IOException,
			ParserConfigurationException {
		Device device = new Device();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream is = Test.class.getClassLoader().getResourceAsStream(
				"dpws.xml");
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		String prefixname = doc.getDocumentElement().getPrefix();
		System.out.println(prefixname);
		String rootname = doc.getDocumentElement().getNodeName();

		System.out.println(rootname);
		NodeList nList = doc.getElementsByTagName(rootname);
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				device.setDeviceFriendlyName(eElement
						.getElementsByTagName("wsdp:FriendlyName").item(0)
						.getTextContent());
				device.setDeviceModel(eElement
						.getElementsByTagName("wsdp:ModelName").item(0)
						.getTextContent());
				device.setDeviceManufacturer(eElement
						.getElementsByTagName("wsdp:Manufacturer").item(0)
						.getTextContent());
			}

		}
		System.out.println(device);

	}

}
