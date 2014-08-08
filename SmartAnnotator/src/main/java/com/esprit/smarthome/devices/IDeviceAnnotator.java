package com.esprit.smarthome.devices;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IDeviceAnnotator {

	public void addAnnotation(List<String> tags, String srcfile);

	public void removeAnnotation(String tag, String srcfile);

	public void updateAnnotation(List<String> values, String srcfile,
			String protocol);

	public void addDPWSAnnotation(List<String> tags, String srcfile);

}