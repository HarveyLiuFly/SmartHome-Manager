package com.esprit.smarthome.engine;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import com.esprit.smarthome.devices.Device;
import com.esprit.smarthome.devices.IDeviceAnnotator;
import com.esprit.smarthome.devices.IDeviceProcessor;
import com.esprit.smarthome.ontology.IOntoProcessor;
import com.esprit.smarthome.ontology.IRulesProcessor;
import com.esprit.smarthome.util.IFileUtil;
import com.esprit.smarthome.util.PathFinder;

@Stateless
public class SemanticEngine implements ISemanticEngine {
	List<String> anno;
	@EJB
	IDeviceProcessor idp;
	@EJB
	IOntoProcessor iop;
	@EJB
	IDeviceAnnotator ida;
	@EJB
	IFileUtil ifp;
	@EJB
	IRulesProcessor irp;
	private PathFinder p = new PathFinder();
	private static final Log log = LogFactory.getLog(SemanticEngine.class);

	@Schedule(minute = "*/2", hour = "*", persistent = false)
	public void autoProcess() {
		prepareFiles();
		List<String> filepaths = ifp.listFiles(p.tempPath());
		for (String filepath : filepaths) {
			log.info("filepath:" + filepath);
			Device device = idp.parseDevice(filepath);
			log.info(device);
			if (iop.ontoQuery(device.getDeviceType()).isEmpty())
				anno = irp.getAnnotationList(device);
			else
				anno = iop.ontoQuery(device.getDeviceType());
			log.info(anno);
			ida.addAnnotation(anno, filepath);
		}
		push();

	}

	public void manualProcess(List<String> annotations, String udn) {
		ida.updateAnnotation(annotations, ifp.getFileByUDN(udn));
		// iop.addIndividual("TV", "Plasma");
	}

	public void prepareFiles() {
		// vider le repertoire temp
		log.info(p.tempPath());
		ifp.cleanDirectory(p.tempPath());
		// lister les fichiers dans le repertoire original
		List<String> filepaths = ifp.listFiles(p.basePath());
		for (String filepath : filepaths) {
			Device device = idp.parseDevice(filepath);
			// renommer les fichiers de description selon leurs ID et déplacer
			// vers temp
			ifp.renameFile(filepath, device.getDeviceUDN());
		}
		ifp.cleanDirectory(p.annotatedPath());

	}

	public void push() {
		PushContext pushContext = PushContextFactory.getDefault()
				.getPushContext();
		pushContext.push("/counter", null);
	}

}
