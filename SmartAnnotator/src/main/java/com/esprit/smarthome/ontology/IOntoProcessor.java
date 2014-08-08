package com.esprit.smarthome.ontology;

import java.util.List;

import javax.ejb.Local;

import com.esprit.smarthome.devices.Device;

@Local
public interface IOntoProcessor {

	public List<String> ontoQuery(Device device);

	public List<String> getAllAnnotations();

	public void addIndividual(String classe, String instanceName);

}