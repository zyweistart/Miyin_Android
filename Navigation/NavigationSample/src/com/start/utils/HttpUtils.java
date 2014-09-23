package com.start.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import com.start.core.Constant;
import com.start.core.NetException;

public class HttpUtils {

	public static String requestServerByXmlContent(Map<String, String> requestHeader, String requestContent)
			throws NetException {
		try {
			HttpResponse response = requestDownServer(requestHeader,requestContent);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line =null;
			StringBuffer buffer = new StringBuffer();
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new NetException(e);
		}
	}

	public static InputStream requestServer(Map<String, String> requestHeader,String requestContent) throws NetException {
		try {
			HttpResponse response = requestDownServer(requestHeader,
					requestContent);
			return response.getEntity().getContent();
		} catch (Exception e) {
			throw new NetException(e);
		}
	}

	public static HttpResponse requestDownServer(Map<String, String> requestHeader, String requestContent)
			throws NetException {
		HttpClient client = new DefaultHttpClient();
		ProtocolVersion protocolVersion =new ProtocolVersion("HTTP", 1, 0);
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, protocolVersion);
		client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Constant.ENCODE);
		// 设置超时时间为30秒
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30000);
		HttpPost post = new HttpPost(Constant.RESTURL);
		try {
			post.addHeader("reqlength", 
					StringUtils.encode(String.valueOf(requestContent.getBytes(Constant.ENCODE).length)));
			if (requestHeader != null) {
				for (String key : requestHeader.keySet()) {
					// 循环遍历添加请求头对请求头内容进行URL编码
					post.addHeader(key,StringUtils.encode(requestHeader.get(key)));
				}
			} else {
				post.addHeader("sign",StringUtils.encode(MD5.md5(requestContent)));
			}
			post.setEntity(new ByteArrayEntity(requestContent.getBytes(Constant.ENCODE)));
			return client.execute(post);
		} catch (Exception e) {
			throw new NetException(e);
		}
	}

}