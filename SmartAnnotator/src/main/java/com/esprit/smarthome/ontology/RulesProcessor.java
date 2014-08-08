package com.esprit.smarthome.ontology;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.esprit.smarthome.devices.Device;

@Stateless
public class RulesProcessor implements IRulesProcessor {
	private List<String> annotations;

	public List<String> getAnnotationList(Device device) {
		annotations = new ArrayList<String>();
		if (device.getDeviceType().equalsIgnoreCase("HVACSYSTEM"))
			annotations.add("HVAC");
		return annotations;

	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

}
