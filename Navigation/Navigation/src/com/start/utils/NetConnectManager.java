package com.start.utils;

import java.util.Hashtable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
/**
 * 网络连接管理
 * @author Start
 */
public class NetConnectManager {
	
//	private static final String LOG_TAG = "NetConnectManager";
	/**
	 * 中国移动cmwap
	 */
	public static String CMWAP = "cmwap";
	/**
	 * 中国移动cmnet
	 */
	public static String CMNET = "cmnet";
	// 中国联通3GWAP设置 中国联通3G因特网设置 中国联通WAP设置 中国联通因特网设置
	// 3gwap 3gnet uniwap uninet
	/**
	 * 3G wap 中国联通3gwap APN
	 */
	public static String GWAP_3 = "3gwap";
	/**
	 * 3G net 中国联通3gnet APN
	 */
	public static String GNET_3 = "3gnet";
	/**
	 * uni wap 中国联通uni wap APN
	 */
	public static String UNIWAP = "uniwap";
	/**
	 * uni net 中国联通uni net APN
	 */
	public static String UNINET = "uninet";
	/**
	 * 电信APN列表
	 */
	public static final String CTWAP = "ctwap";
	public static final String CTNET = "ctnet";

	private static Hashtable<String, String> networkTable = new Hashtable<String, String>();

	public static void switchNetType(Context context, String from, String to) {

	}

	public static boolean isNetWorkAvailable(Context context) {
		boolean isAvailable = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				DetailedState detailedState = networkInfo.getDetailedState();
				String detailedName = detailedState.name();
				if (detailedName == null) {
					detailedName = "";
				}
				networkTable.put("detailedName", detailedName);
				String extraInfo = networkInfo.getExtraInfo();
				if (extraInfo == null) {
					extraInfo = "";
				}
				networkTable.put("extraInfo", extraInfo);
				String reason = networkInfo.getReason();
				if (reason == null) {
					reason = "";
				}
				networkTable.put("reason", reason);
				NetworkInfo.State state = networkInfo.getState();
				String stateName = "";
				if (state != null && state.name() != null) {
					stateName = state.name();
				}
				// 经过多次测试，只有stateName可以准确的判断网络连接是否正常
				if ("CONNECTED".equalsIgnoreCase(stateName)) {
					isAvailable = true;
				}
				networkTable.put("stateName", stateName);
				int subType = networkInfo.getSubtype();
				networkTable.put("subType", subType + "");
				String subtypeName = networkInfo.getSubtypeName();
				if (subtypeName == null) {
					subtypeName = "";
				}
				networkTable.put("subtypeName", subtypeName);
//				int type = networkInfo.getType();
				String typeName = networkInfo.getTypeName();
				if (typeName == null) {
					typeName = "";
				}
				networkTable.put("typeName", typeName);
//				Log.d(LOG_TAG, getLogString());
			}
		}
		return isAvailable;
	}

	public static String getLogString() {
		StringBuilder sb = new StringBuilder();
		sb.append("detailedName=" + networkTable.get("detailedName"));
		sb.append(" extraInfo=" + networkTable.get("extraInfo"));
		sb.append(" reason=" + networkTable.get("reason"));
		sb.append(" stateName=" + networkTable.get("stateName"));
		sb.append(" subtypeName=" + networkTable.get("subtypeName"));
		sb.append(" typeName=" + networkTable.get("typeName"));
		return sb.toString();
	}

	public static String apnType() {
		String apn = "";
		String netType = networkTable.get("typeName");
		if ("CONNECTED".equals(networkTable.get("stateName"))) {
			if ("wifi".equalsIgnoreCase(netType)) {
				apn = netType;
			} else if ("mobile".equalsIgnoreCase(netType)) {
				netType = networkTable.get("extraInfo").toLowerCase();
				apn = matchAPN(netType);
			} else {
				//new FeedBackException("UNKNOW networkType:::" + getLogString());
			}
		} else {

		}
		return apn;
	}

	public static String matchAPN(String currentName) {
		if ("".equals(currentName) || null == currentName) {
			return "";
		}
		currentName = currentName.toLowerCase();
		if (currentName.startsWith(CMNET))
			return CMNET;
		else if (currentName.startsWith(CMWAP))
			return CMWAP;
		else if (currentName.startsWith(GNET_3))
			return GNET_3;
		else if (currentName.startsWith(GWAP_3))
			return GWAP_3;
		else if (currentName.startsWith(UNINET))
			return UNINET;
		else if (currentName.startsWith(UNIWAP))
			return UNIWAP;
		else if (currentName.startsWith(CTWAP))
			return CTWAP;
		else if (currentName.startsWith(CTNET))
			return CTNET;
		else if (currentName.startsWith("default"))
			return "default";
		else
			return "";
	}
	/**
	 * 判断是否在移动网络模式下
	 */
	public static Boolean isMobilenetwork(Context context){
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo[] info;
	       if (connectivity != null) {
	           info = connectivity.getAllNetworkInfo();
	            if (info != null) {
	              for (int i = 0; i < info.length; i++) {
	                  if (info[i].getTypeName().equals("WIFI") &&
	                         info[i].isConnected()) {
	                      return false;
	                  }
	              }
	          }
	      }
	      return true;
	}
	
}