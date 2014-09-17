package start.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import start.core.AppConstant.ResultCode;
import start.core.AppException;

public class Response {
	
	public static final String RESPONSETAG="response";
	public static final String INFOTAG="info";
	public static final String CODETAG="code";
	public static final String MSGTAG="msg";
	public static final String CONTENTTAG="content";
	public static final String DATATAG="data";
	public static final String PAGEINFOTAG="pageinfo";
	public static final String LISTTAG="list";
	
	private String mCode;
	private String mMsg;
	private String responseString;
	private JSONObject mJsonObject;
	
	private JSONObject mResponsePageInfo;
	private JSONObject mResponseData;
	private JSONArray mResponseDataArray;
	
	private Map<String,String> mMapData;
	private Map<String,String> mPageInfoMapData;
	private List<Map<String,String>> mListMapData;
	
	private HttpResponse mHttpResponse;

	public String getCode() throws AppException{
		return mCode;
	}

	public String getMsg() throws AppException {
		return mMsg;
	}
	
	private JSONObject getResponseData() throws AppException {
		if(mResponseData==null){
			try {
				if(this.mJsonObject!=null){
					mResponseData=this.mJsonObject.getJSONObject(DATATAG);
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mResponseData;
	}
	
	private JSONObject getResponseData(String tag) throws AppException {
		if(mResponseData==null){
			try {
				if(this.mJsonObject!=null){
					mResponseData=this.mJsonObject.getJSONObject(tag);
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mResponseData;
	}
	
	private JSONObject getResponsePageInfo() throws AppException {
		if(mResponsePageInfo==null){
			try {
				if(this.mJsonObject!=null){
					if(this.mJsonObject.has(PAGEINFOTAG)){
						mResponsePageInfo=this.mJsonObject.getJSONObject(PAGEINFOTAG);
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mResponsePageInfo;
	}
	
	private JSONArray getResponseDataArray() throws AppException {
		if(mResponseDataArray==null){
			try {
				if(this.mJsonObject!=null){
					if(getResponsePageInfo()!=null){
						mResponseDataArray=this.mJsonObject.getJSONArray(LISTTAG);
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mResponseDataArray;
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getMapData() throws AppException {
		if(mMapData==null){
			try {
				JSONObject obj=getResponseData();
				if(obj!=null){
					mMapData=new HashMap<String,String>();
					Iterator<String> i=obj.keys();
					while(i.hasNext()){
						String key=i.next();
						mMapData.put(key,obj.getString(key));
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mMapData;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getPageInfoMapData() throws AppException {
		if(mResponsePageInfo==null){
			try {
				JSONObject obj=getResponsePageInfo();
				if(obj!=null){
					mPageInfoMapData=new HashMap<String,String>();
					Iterator<String> i=obj.keys();
					while(i.hasNext()){
						String key=i.next();
						mPageInfoMapData.put(key,obj.getString(key));
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mPageInfoMapData;
	}
	
	public List<Map<String,String>> getListMapData() throws AppException {
		if(mListMapData==null){
			try {
				JSONArray dataArray=getResponseDataArray();
				if(dataArray!=null){
					mListMapData=new ArrayList<Map<String,String>>();
					for(int i=0;i<dataArray.length();i++){
						JSONObject current=dataArray.getJSONObject(i);
						Map<String,String> datas=new HashMap<String,String>();
						JSONArray names=current.names();
						for(int j=0;j<names.length();j++){
							String name=names.getString(j);
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
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getMapData(String tag) throws AppException {
		if(mMapData==null){
			try {
				JSONObject obj=getResponseData(tag);
				if(obj!=null){
					mMapData=new HashMap<String,String>();
					Iterator<String> i=obj.keys();
					while(i.hasNext()){
						String key=i.next();
						mMapData.put(key,obj.getString(key));
					}
				}
			} catch (JSONException e) {
				throw AppException.json(e);
			}
		}
		return mMapData;
	}
	
	public List<Map<String,String>> getListMapData(String tag,String chinaTag) throws AppException {
		if(mListMapData==null){
			try {
				if(mResponseDataArray==null){
					if(this.mJsonObject!=null){
						if(getResponsePageInfo()!=null){
							mResponseDataArray=this.mJsonObject.getJSONArray(tag);
						}
					}
				}
				if(mResponseDataArray!=null){
					mListMapData=new ArrayList<Map<String,String>>();
					for(int i=0;i<mResponseDataArray.length();i++){
						JSONObject current=mResponseDataArray.getJSONObject(i).getJSONObject(chinaTag);
						Map<String,String> datas=new HashMap<String,String>();
						JSONArray names=current.names();
						for(int j=0;j<names.length();j++){
							String name=names.getString(j);
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
				BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()));
				String line =null;
				StringBuffer buffer = new StringBuffer();
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
			JSONObject response=jo.getJSONObject(RESPONSETAG);
			if(response.has(INFOTAG)){
				JSONObject info=response.getJSONObject(INFOTAG);
				this.mCode=info.getString(CODETAG);
				this.mMsg=info.getString(MSGTAG);
			}
			if(ResultCode.SUCCESS.equals(this.mCode)){
				if(response.has(CONTENTTAG)){
					this.mJsonObject=response.getJSONObject(CONTENTTAG);
				}
			}
		} catch (JSONException e) {
			throw AppException.json(e);
		}
	}
	
}