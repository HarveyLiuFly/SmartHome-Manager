package com.esprit.smarthome.engine;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ISemanticEngine {

	public void autoProcess();

	public void manualProcess(List<String> annotations, String udn,
			String protocol);

}