package com.esprit.smarthome.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

import com.esprit.smarthome.devices.Device;
import com.esprit.smarthome.devices.IDeviceProcessor;
import com.esprit.smarthome.engine.ISemanticEngine;
import com.esprit.smarthome.ontology.IOntoProcessor;

@ManagedBean(name = "devicesBean")
@ViewScoped
public class DevicesBean implements Serializable {

	private static final Log log = LogFactory.getLog(DevicesBean.class);
	private static final long serialVersionUID = 1L;
	private List<Device> devices;
	private List<String> annotations;
	private List<String> selectAnnotations;
	private Device device;
	private boolean showDetails = false;

	@EJB
	IOntoProcessor iop;
	@EJB
	IDeviceProcessor idp;
	@EJB
	ISemanticEngine ise;

	public DevicesBean() {
	}

	@PostConstruct
	public void init() {
		setDevices(idp.getAllDevices());
		setAnnotations(iop.getAllAnnotations());
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<String> getSelectAnnotations() {
		return selectAnnotations;
	}

	public void setSelectAnnotations(List<String> selectAnnotations) {
		this.selectAnnotations = selectAnnotations;
	}

	public void doEdit() {
		System.out.println(device);
		showDetails = true;
	}

	public void doAnnotate() {
		System.out.println(selectAnnotations);
		ise.manualProcess(selectAnnotations, device.getDeviceUDN());
		selectAnnotations = new ArrayList<String>();
		devices = idp.getAllDevices();
	}

	public void poll() {
		devices = idp.getAllDevices();
	}

	public void notif() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Info", "Devices Updated"));
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public void onRowSelect(SelectEvent event) {
		showDetails = true;
	}

}
