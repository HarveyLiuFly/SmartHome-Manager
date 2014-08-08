package com.esprit.smarthome.ontology;

import java.util.List;

import javax.ejb.Local;

import com.esprit.smarthome.devices.Device;

@Local
public interface IRulesProcessor {

	public List<String> getAnnotationList(Device device);

}