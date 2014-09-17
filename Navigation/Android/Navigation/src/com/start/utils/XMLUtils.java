package com.start.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.start.core.Constant;
import com.start.core.XMLException;

public class XMLUtils {
	public final class RequestXmLConstant{
		
		public static final String INFO="info";
		
		public static final String PAGEINFO="pageinfo";
		
		public static final String CONTENT="content";
		
		public static final String CODE="code";
		
		public static final String MSG="msg";
		
		public static final String SUCCESSCODE="100000";
		
		public static final String LOGINERROR="110026";
	
	}
	
	/**
	 * 生成请求格式的XML
	 */
	public static String builderRequestXml(String url,Map<String,String> content) throws XMLException{
        try {
        	XmlSerializer serializer = Xml.newSerializer();  
    	    StringWriter writer = new StringWriter();  
    	    serializer.setOutput(writer);  
            serializer.startDocument(Constant.ENCODE,true);  
            serializer.startTag("","request");  
                //公共模块
                serializer.startTag("","common");
                //请求URL
                   serializer.startTag("","action");  
                   serializer.text(url);  
                   serializer.endTag("","action");
                //请求时间
                   serializer.startTag("","reqtime");  
                   serializer.text(TimeUtils.getSysTimeLong());  
                   serializer.endTag("","reqtime");
                serializer.endTag("","common");
                
                 //请求的主体内容
                serializer.startTag("","content");
                	for(String key:content.keySet()){
                		serializer.startTag("",key);  
     	               	serializer.text(content.get(key));  
     	               	serializer.endTag("",key);
                	}
                serializer.endTag("","content");  
            serializer.endTag("","request");  
			serializer.endDocument();
			return writer.toString();
		} catch (IllegalArgumentException e) {
			throw new XMLException(e);
		} catch (IllegalStateException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
	}
	
	public static Map<String,Map<String,String>>  xmlResolve(String xmlContent) throws XMLException {
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		Map<String,Map<String,String>> mapDataContent=new HashMap<String,Map<String,String>>();
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			StringReader stringReader  =  new StringReader(xmlContent);
			InputSource  inputSource  =  new  InputSource(stringReader);
			document=builder.parse(inputSource);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		Element root=document.getDocumentElement();
		NodeList nodeList=root.getElementsByTagName(RequestXmLConstant.INFO);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodeList=nodeList.item(i).getChildNodes();
			Map<String,String> content=new HashMap<String,String>();
			for (int j = 0; j < childNodeList.getLength();j++) {
				Node childNode=childNodeList.item(j);
				content.put(childNode.getNodeName(), childNode.getTextContent());
			}
			mapDataContent.put(RequestXmLConstant.INFO, content);
		}
		nodeList=root.getElementsByTagName(RequestXmLConstant.CONTENT);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodeList=nodeList.item(i).getChildNodes();
			for (int j = 0; j < childNodeList.getLength();j++) {
				Node cnl=childNodeList.item(j);
				NodeList childNode=cnl.getChildNodes();
				Map<String,String> content=new HashMap<String,String>();
				for (int k= 0; k< childNode.getLength();k++) {
					Node cNode=childNode.item(k);
					content.put(cNode.getNodeName(), cNode.getTextContent());
				}
//				if(childNodeList.getLength()==1){
//					mapDataContent.put(Constant.RequestXmLConstant.CONTENT, content);
//				}else{
					mapDataContent.put(cnl.getNodeName(), content);
//				}
			}
		}
		return mapDataContent;
	}
	/**
	 * 解析服务端返回的XML内容,普通形式
	 * @throws XMLException 
	 */
	public static Map<String,Map<String,String>>  xmlResolve(InputStream inputStream) throws XMLException {
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		Map<String,Map<String,String>> mapDataContent=new HashMap<String,Map<String,String>>();
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			document=builder.parse(inputStream);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		Element root=document.getDocumentElement();
		NodeList nodeList=root.getElementsByTagName(RequestXmLConstant.INFO);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodeList=nodeList.item(i).getChildNodes();
			Map<String,String> content=new HashMap<String,String>();
			for (int j = 0; j < childNodeList.getLength();j++) {
				Node childNode=childNodeList.item(j);
				content.put(childNode.getNodeName(), childNode.getTextContent());
			}
			mapDataContent.put(RequestXmLConstant.INFO, content);
		}
		nodeList=root.getElementsByTagName(RequestXmLConstant.CONTENT);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList childNodeList=nodeList.item(i).getChildNodes();
			for (int j = 0; j < childNodeList.getLength();j++) {
				Node cnl=childNodeList.item(j);
				NodeList childNode=cnl.getChildNodes();
				Map<String,String> content=new HashMap<String,String>();
				for (int k= 0; k< childNode.getLength();k++) {
					Node cNode=childNode.item(k);
					content.put(cNode.getNodeName(), cNode.getTextContent());
				}
//				if(childNodeList.getLength()==1){
//					mapDataContent.put(Constant.RequestXmLConstant.CONTENT, content);
//				}else{
					mapDataContent.put(cnl.getNodeName(), content);
//				}
			}
		}
		return mapDataContent;
	}
	/**
	 * 解析服务端返回的XML内容，列表形式
	 * @throws XMLException 
	 */
	public static Map<String,List<Map<String,String>>>  xmlResolvelist(InputStream inputStream) throws XMLException {
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		Map<String,List<Map<String,String>>> mapDataContent=new HashMap<String,List<Map<String,String>>>();
		
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    String line = "";
		    StringBuffer buffer = new StringBuffer();
		    while ((line = in.readLine()) != null){
		      buffer.append(line);
		    }
			StringReader stringReader  =  new StringReader(buffer.toString());
			InputSource  inputSource  =  new  InputSource(stringReader);
			document=builder.parse(inputSource);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		Element root=document.getDocumentElement();
		NodeList firstNodeList=root.getElementsByTagName(RequestXmLConstant.INFO);
		for (int i = 0; i < firstNodeList.getLength(); i++) {
			NodeList secondNodeList=firstNodeList.item(i).getChildNodes();
			Map<String,String> content=new HashMap<String,String>();
			for (int j = 0; j < secondNodeList.getLength();j++) {
				Node cNode=secondNodeList.item(j);
				content.put(cNode.getNodeName(), cNode.getTextContent());
			}
			lists.add(content);
		}
		mapDataContent.put(RequestXmLConstant.INFO, lists);
		
		firstNodeList=root.getElementsByTagName(RequestXmLConstant.CONTENT);
		for (int i = 0; i < firstNodeList.getLength(); i++) {
			NodeList secondNodeList=firstNodeList.item(i).getChildNodes();
			for (int j = 0; j < secondNodeList.getLength();j++) {
				Node node=secondNodeList.item(j);
				NodeList thirdNodeList=node.getChildNodes();
				lists=new ArrayList<Map<String,String>>();
				if(node.getNodeName().equals(RequestXmLConstant.PAGEINFO)){
					Map<String,String> content=new HashMap<String,String>();
					for (int k= 0; k< thirdNodeList.getLength();k++) {
						Node cNode=thirdNodeList.item(k);
						content.put(cNode.getNodeName(), cNode.getTextContent());
					}
					lists.add(content);
				}else{
					for (int k= 0; k< thirdNodeList.getLength();k++) {
						NodeList fourthNodeList=thirdNodeList.item(k).getChildNodes();
						Map<String,String> content=new HashMap<String,String>();
						for (int a= 0; a< fourthNodeList.getLength();a++) {
							Node cNode=fourthNodeList.item(a);
							content.put(cNode.getNodeName(), cNode.getTextContent());
						}
						lists.add(content);
					}
				}
				mapDataContent.put(node.getNodeName(), lists);
			}
		}
		return mapDataContent;
	}
	
	public static Map<String,List<Map<String,String>>>  xmlResolvelist(String xmlContent) throws XMLException {
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		Map<String,List<Map<String,String>>> mapDataContent=new HashMap<String,List<Map<String,String>>>();
		
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			StringReader stringReader  =  new StringReader(xmlContent);
			InputSource  inputSource  =  new  InputSource(stringReader);
			document=builder.parse(inputSource);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		Element root=document.getDocumentElement();
		NodeList firstNodeList=root.getElementsByTagName(RequestXmLConstant.INFO);
		for (int i = 0; i < firstNodeList.getLength(); i++) {
			NodeList secondNodeList=firstNodeList.item(i).getChildNodes();
			Map<String,String> content=new HashMap<String,String>();
			for (int j = 0; j < secondNodeList.getLength();j++) {
				Node cNode=secondNodeList.item(j);
				content.put(cNode.getNodeName(), cNode.getTextContent());
			}
			lists.add(content);
		}
		mapDataContent.put(RequestXmLConstant.INFO, lists);
		
		firstNodeList=root.getElementsByTagName(RequestXmLConstant.CONTENT);
		for (int i = 0; i < firstNodeList.getLength(); i++) {
			NodeList secondNodeList=firstNodeList.item(i).getChildNodes();
			for (int j = 0; j < secondNodeList.getLength();j++) {
				Node node=secondNodeList.item(j);
				NodeList thirdNodeList=node.getChildNodes();
				lists=new ArrayList<Map<String,String>>();
				if(node.getNodeName().equals(RequestXmLConstant.PAGEINFO)){
					Map<String,String> content=new HashMap<String,String>();
					for (int k= 0; k< thirdNodeList.getLength();k++) {
						Node cNode=thirdNodeList.item(k);
						content.put(cNode.getNodeName(), cNode.getTextContent());
					}
					if(!content.isEmpty()){
						lists.add(content);
					}
				}else{
					for (int k= 0; k< thirdNodeList.getLength();k++) {
						NodeList fourthNodeList=thirdNodeList.item(k).getChildNodes();
						Map<String,String> content=new HashMap<String,String>();
						for (int a= 0; a< fourthNodeList.getLength();a++) {
							Node cNode=fourthNodeList.item(a);
							content.put(cNode.getNodeName(), cNode.getTextContent());
						}
						if(!content.isEmpty()){
							lists.add(content);
						}
					}
				}
				if(!lists.isEmpty()){
					mapDataContent.put(node.getNodeName(), lists);
				}
			}
		}
		return mapDataContent;
	}
	
}