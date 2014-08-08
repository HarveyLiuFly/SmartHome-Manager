package com.esprit.smarthome.devices;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IDeviceProcessor {

	public Device parseDevice(String devicepath);

	public List<Device> getAllDevices();

	public Device getUPnPDeviceInfo(String devicepath);

	public Device getDPWSDeviceInfo(String devicepath);

}