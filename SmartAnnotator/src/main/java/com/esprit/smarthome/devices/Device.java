package com.esprit.smarthome.devices;

import java.util.ArrayList;
import java.util.List;

public class Device {
	private String deviceProtocol;
	private String deviceFriendlyName;
	private String deviceModel;
	private String deviceType;
	private String deviceUDN;
	private String deviceManufacturer;
	public List<String> deviceAnnotations = new ArrayList<String>();

	public String getDeviceFriendlyName() {
		return deviceFriendlyName;
	}

	public List<String> getDeviceAnnotations() {
		return deviceAnnotations;
	}

	public void setDeviceAnnotations(List<String> deviceAnnotations) {
		this.deviceAnnotations = deviceAnnotations;
	}

	public void setDeviceFriendlyName(String deviceFriendlyName) {
		this.deviceFriendlyName = deviceFriendlyName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}

	public String getDeviceUDN() {
		return deviceUDN;
	}

	public void setDeviceUDN(String deviceudn) {
		this.deviceUDN = deviceudn;
	}

	@Override
	public String toString() {
		return "Device [deviceFriendlyName=" + deviceFriendlyName
				+ ", deviceModel=" + deviceModel + ", deviceType=" + deviceType
				+ ", deviceManufacturer=" + deviceManufacturer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceUDN == null) ? 0 : deviceUDN.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (deviceUDN == null) {
			if (other.deviceUDN != null)
				return false;
		} else if (!deviceUDN.equals(other.deviceUDN))
			return false;
		return true;
	}

	public String getDeviceProtocol() {
		return deviceProtocol;
	}

	public void setDeviceProtocol(String deviceProtocol) {
		this.deviceProtocol = deviceProtocol;
	}

}
