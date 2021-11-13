package com.sanoj.devildeveloper.enoughmovies.Utility;
import com.sanoj.devildeveloper.enoughmovies.Config.Config;

public final class DownloadFileUtility {
	private DownloadFileUtility() {}

	public static boolean isDownloadableFile(String url) {
		url = url.toLowerCase();

		for (String type : Config.DOWNLOAD_FILE_TYPES) {
			if (url.matches(type)) return true;
		}

		return false;
	}

	public static String getFileName(String url) {
		int index = url.indexOf("?");
		if (index > -1) {
			url = url.substring(0, index);
		}
		url = url.toLowerCase();

		index = url.lastIndexOf("/");
		if (index > -1) {
			return url.substring(index + 1, url.length());
		} else {
			return Long.toString(System.currentTimeMillis());
		}
	}
}