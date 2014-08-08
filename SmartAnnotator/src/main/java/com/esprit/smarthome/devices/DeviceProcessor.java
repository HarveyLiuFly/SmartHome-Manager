package com.esprit.smarthome.devices;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.esprit.smarthome.util.FileUtil;
import com.esprit.smarthome.util.PathFinder;

@Stateless
public class DeviceProcessor implements IDeviceProcessor {
	static final Log log = LogFactory.getLog(DeviceProcessor.class);
	private PathFinder p = new PathFinder();
	Document doc;

	public Device parseDevice(String devicepath) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(devicepath);
			doc.getDocumentElement().normalize();
			String rootname = doc.getDocumentElement().getNodeName();
			if (rootname.equals("root"))
				return getUPnPDeviceInfo(devicepath);
			else
				return getDPWSDeviceInfo(devicepath);

		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public List<Device> getAllDevices() {
		List<Device> devices = new ArrayList<Device>();
		List<String> filepaths = new FileUtil().listFiles(p.annotatedPath());
		for (String path : filepaths) {
			devices.add(parseDevice(path));
		}
		return devices;
	}

	public Device getUPnPDeviceInfo(String devicepath) {
		Device device = new Device();
		device.setDeviceProtocol("UPnP");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(devicepath);
			doc.getDocumentElement().normalize();
			Node nNode = doc.getElementsByTagName("root").item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String devType = eElement.getElementsByTagName("deviceType")
						.item(0).getTextContent();
				String devTypeShort = devType.split(":")[3];
				device.setDeviceType(devTypeShort);
				device.setDeviceFriendlyName(eElement
						.getElementsByTagName("friendlyName").item(0)
						.getTextContent());
				device.setDeviceModel(eElement
						.getElementsByTagName("modelName").item(0)
						.getTextContent());
				device.setDeviceManufacturer(eElement
						.getElementsByTagName("manufacturer").item(0)
						.getTextContent());
				device.setDeviceUDN(eElement.getElementsByTagName("UDN")
						.item(0).getTextContent().replaceAll("uuid:", ""));
				List<String> cats = new ArrayList<String>();
				for (int j = 0; j < eElement.getElementsByTagName("category")
						.getLength(); j++) {
					cats.add(eElement.getElementsByTagName("category").item(j)
							.getTextContent());
				}
				// // List<String> deva = device.getDeviceAnnotations();
				// // deva.addAll(cats);
				device.setDeviceAnnotations(cats);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return device;

	}

	public Device getDPWSDeviceInfo(String devicepath) {
		Device device = new Device();
		device.setDeviceProtocol("DPWS");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(devicepath);
			doc.getDocumentElement().normalize();
			String rootname = doc.getDocumentElement().getNodeName();
			Node nNode = doc.getElementsByTagName(rootname).item(0);
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
				device.setDeviceUDN(eElement
						.getElementsByTagName("wsdp:SerialNumber").item(0)
						.getTextContent());
				List<String> cats = new ArrayList<String>();
				for (int j = 0; j < eElement.getElementsByTagName(
						"wsdp:category").getLength(); j++) {
					cats.add(eElement.getElementsByTagName("wsdp:category")
							.item(j).getTextContent());
				}
				// // List<String> deva = device.getDeviceAnnotations();
				// // deva.addAll(cats);
				device.setDeviceAnnotations(cats);
				device.setDeviceType("");

			}

			return device;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return device;

	}
}