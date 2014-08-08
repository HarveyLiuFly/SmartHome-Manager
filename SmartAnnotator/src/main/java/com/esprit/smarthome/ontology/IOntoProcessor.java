package com.esprit.smarthome.ontology;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IOntoProcessor {

	public List<String> ontoQuery(String queryParam);

	public List<String> getAllAnnotations();

	public void addIndividual(String classe, String instanceName);

}