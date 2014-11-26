package com.start.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppException;

public class Response {
	
	public static final String CODETAG="code";
	public static final String MSGTAG="Message";
	public static final String CURRENTPAGE="PageIndex";
	public static final String PAGESIZE="Size";
	public static final String DATATAG="Data";
	public static final String LISTTAG="Tab";
	public static final String SUCCESS="0";
	
	private String mCode;
	private String mMsg;
	private String responseString;
	private JSONObject mJsonObject;
	
	private List<Map<String,Object>> mListMapData;
	
	private HttpResponse mHttpResponse;

	public String getCode() throws AppException{
		return mCode;
	}

	public String getMsg() throws AppException {
		return mMsg;
	}
	
	public Response(HttpResponse httpResponse) {
		this.mHttpResponse=httpResponse;
	}
	
	/**
	 * 请求并获取响应的流对象
	 */
	public InputStream getInputStream() throws AppException{
		try {
			return mHttpResponse.getEntity().getContent();
		} catch (Exception e) {
			throw AppException.http(e);
		}
	}
	
	/**
	 * 请求并获取响应的字符串数据
	 */
	public String getResponseString() throws AppException {
		if(responseString==null){
			try {
				String line =null;
				StringBuffer buffer = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()));
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
				responseString=buffer.toString();
			} catch (Exception e) {
				throw AppException.http(e);
			}
		}
		return responseString;
	}
	
	public void setResponseString(String responseString){
		this.responseString=responseString;
	}
	
	/**
	 * 解析JSON
	 */
	public void resolveJson() throws AppException {
		try {
			JSONObject jo = new JSONObject(getResponseString());
			this.mCode=jo.getString(CODETAG);
			this.mMsg=jo.getString(MSGTAG);
			if(SUCCESS.equals(this.mCode)){
				this.mJsonObject=jo.getJSONObject(DATATAG);
			}
		} catch (JSONException e) {
			throw AppException.json(e);
		}
	}
	
	public Object getData(String key) throws AppException{
		try {
			return this.mJsonObject.get(key);
		} catch (JSONException e) {
			throw AppException.json(e);
		}
	}
	
	public List<Map<String,Object>> getListMapData() throws AppException {
		if (mListMapData == null) {
			try {
				JSONArray dataArray = (JSONArray)getData(LISTTAG);
				if (dataArray != null) {
					mListMapData = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject current = dataArray.getJSONObject(i);
						Map<String, Object> datas = new HashMap<String, Object>();
						JSONArray names = current.names();
						for (int j = 0; j < names.length(); j++) {
							String name = names.getString(j);
							datas.put(name, current.getString(name));
						}
						mListMapData.add(datas);
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mListMapData;
	}
}