package com.esprit.smarthome.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PathFinder {
	Properties prop = new Properties();
	InputStream input;
	private static final Log log = LogFactory.getLog(PathFinder.class);

	public PathFinder() {
		try {
			input = PathFinder.class.getClassLoader().getResourceAsStream(
					"config.properties");
			prop.load(input);
		} catch (IOException e) {
			log.error("Erreur de lecture du fichier");
			;
		}
	}

	public String annotatedPath() {
		return prop.getProperty("annotated");
	}

	public String basePath() {
		return prop.getProperty("original");
	}

	public String tempPath() {
		return prop.getProperty("temp");
	}
}