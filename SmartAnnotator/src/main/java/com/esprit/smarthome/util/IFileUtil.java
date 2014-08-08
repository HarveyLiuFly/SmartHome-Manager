package com.esprit.smarthome.util;

import java.util.List;

public interface IFileUtil {

	List<String> listFiles(String path);

	List<String> listFilePaths(String path);

	void renameFile(String filepath, String name);

	void cleanDirectory(String dirpath);

	public String getFileByUDN(String udn);

}