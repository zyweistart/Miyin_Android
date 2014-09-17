package com.start.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Utils {

	public static boolean isExternalStorageAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	public static void writeStreamToExternalStorage(Context context, InputStream is, String relativePath) throws IOException {
		File cacheFile = createExternalFile(context, relativePath);
		OutputStream out = null;
		try {
			out = new FileOutputStream(cacheFile);
			copyFile(is, out);
			out.flush();
		} finally {
			closeOutputStream(out);
		}
	}

	public static String openUrl(URL url) {
		if (url == null) {
			return null;
		}

		String html = null;
		InputStream is = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			int response = conn.getResponseCode();

			if (response == 200) {
				is = conn.getInputStream();
				html = readIt(is);
			}
		} catch (IOException e) {
			Log.d("debug", "can not connect to library");
			html = null;
		} finally {
			closeInputStream(is);
		}

		return html;
	}

	/**
	 * Get file under application's external download directory
	 * 
	 * @param context
	 * @param relativePath
	 * @return
	 */
	public static File getFile(Context context, String relativePath) {
		return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), relativePath);
	}

	private static void closeOutputStream(OutputStream os) {
		if (os == null) {
			return;
		}

		try {
			os.close();
		} catch (IOException e) {
		}
	}

	public static void closeInputStream(InputStream is) {
		if (is == null) {
			return;
		}

		try {
			is.close();
		} catch (IOException e) {
		}
	}

	private static String readIt(InputStream is) throws IOException {
		int len;
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();
		while ((len = is.read(buf)) != -1) {
			sb.append(new String(buf, 0, len));
		}
		return sb.toString();
	}
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		if (in == null || out == null) {
			return;
		}

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}

	private static File createExternalFile(Context context, String relativePath) {
		File file = getFile(context, relativePath);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return file;
	}

	/**
	 * Convert location to baidu map point
	 * 
	 * @param location
	 * @return
	 */
//	public static com.baidu.platform.comapi.basestruct.GeoPoint toBDPoint(Location location) {
//		return new com.baidu.platform.comapi.basestruct.GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
//	}

	/**
	 * Construct a baidu map point
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
//	public static com.baidu.platform.comapi.basestruct.GeoPoint toBDPoint(double latitude, double longitude) {
//		return new com.baidu.platform.comapi.basestruct.GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
//	}

	/**
	 * Construct a mapsforge map point
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static GeoPoint toMFPoint(double latitude, double longitude) {
		return new GeoPoint(latitude, longitude);
	}

	/**
	 * Dialog indentifier
	 */
	public static final int DLG_SEARCH_OPTION = 1;
	public static final int DLG_POI = 2;
	public static final int DLG_EXIT_NAVIGATION = 3;
	public static final int DLG_EXIT = 4;
}
