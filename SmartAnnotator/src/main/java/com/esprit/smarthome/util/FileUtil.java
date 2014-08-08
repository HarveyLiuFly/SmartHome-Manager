package com.esprit.smarthome.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless
public class FileUtil implements IFileUtil {
	private PathFinder p = new PathFinder();
	private static final Log log = LogFactory.getLog(FileUtil.class);

	public List<String> listFiles(String path) {
		List<String> filenames = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			filenames.add(listOfFiles[i].getAbsolutePath());

		}
		return filenames;
	}

	public List<String> listFilePaths(String path) {
		List<String> filenames = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			filenames.add(listOfFiles[i].getName());

		}
		return filenames;
	}

	public void renameFile(String filepath, String name) {
		File afile = new File(filepath);
		String tf = name + ".xml";
		if (tf.contains("uuid"))
			copyFile(afile, new File(p.tempPath() + tf.replaceAll("uuid:", "")));
		else
			copyFile(afile, new File(p.tempPath() + tf));
	}

	public void cleanDirectory(String dirpath) {
		File file = new File(dirpath);
		for (File f : file.listFiles()) {
			f.delete();
		}

	}

	public String getFileByUDN(String udn) {
		String filepath = "C:\\sem\\annotated\\" + udn + ".xml";
		return filepath;

	}

	public void copyFile(File from, File to) {
		try {
			Files.copy(from.toPath(), to.toPath());
		} catch (IOException e) {
			log.error("Erreur de lecture de fichier " + from.toPath());
			;
		}
	}
}
