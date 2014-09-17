package com.start.model.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.start.core.XMLException;
import com.start.model.process.Action.ActionType;
import com.start.model.process.Junction.NodeType;
import com.start.navigation.AppContext;
import com.start.utils.CommonFn;
import com.start.utils.Utils;

public class ProcessService {
	
	private static final String PROCESSDEFINITIONS="process_definitions.xml";
	
	private String currentJunctionId;
	
	private String nextJunctionId;
	
	private String startJunctionId;
	
	private String endJunctionId;
	
	private Map<String,Junction> junctions;
	
	private Context mContext;
	
	private AppContext mAppContext;
	
	private ProcessListener processListener;
	
	private String processImage;
	
	public ProcessService(Context context,ProcessListener listener){
		this.mContext=context;
		this.mAppContext=AppContext.getInstance();
		this.processListener=listener;
		try {
			File dataFile=new File(Utils.getFile(mContext,mAppContext.getCurrentDataNo()),"process/"+PROCESSDEFINITIONS);
			buildProcessXML(new FileInputStream(dataFile));
		} catch (Exception e) {
			e.printStackTrace();
			mAppContext.makeTextLong("流程文件定义出错:"+e.getMessage());
		}
	}
	
	private void buildProcessXML(InputStream in) throws XMLException{
		junctions=new HashMap<String,Junction>();
		Document document=null;
		DocumentBuilder builder=null;
		DocumentBuilderFactory factory=null;
		factory=DocumentBuilderFactory.newInstance();
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
		try {
			document=builder.parse(in);
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
		Element root=document.getDocumentElement();
		NamedNodeMap pnnM=root.getAttributes();
		setProcessImage(pnnM.getNamedItem("image").getNodeValue());
		NodeList nodeList=root.getChildNodes();
		for (int j= 0; j< nodeList.getLength();j++) {
			Node cNode=nodeList.item(j);
			NodeList aoChildList=cNode.getChildNodes();
			String nodeName=cNode.getNodeName().toLowerCase();
			NamedNodeMap nnM=cNode.getAttributes();
			Junction jun=null;
			String id=null;
			if("start".equals(nodeName)){
				id=nnM.getNamedItem("id").getNodeValue();
				String title=nnM.getNamedItem("title").getNodeValue();
				String next=nnM.getNamedItem("next").getNodeValue();
				String image=nnM.getNamedItem("image").getNodeValue();
				jun=new Junction(id,title,NodeType.START);
				jun.setImage(image);
				jun.setNextNodeId(next);
				startJunctionId=id;
			}else if("switch".equals(nodeName)){
				id=nnM.getNamedItem("id").getNodeValue();
				String title=nnM.getNamedItem("title").getNodeValue();
				jun=new Junction(id,title,NodeType.SWITCH);
				List<Option> options=new ArrayList<Option>();
				for (int l= 0; l< aoChildList.getLength();l++) {
					Node actionNode=aoChildList.item(l);
					if("option".equals(actionNode.getNodeName().toLowerCase())){
						NamedNodeMap actionNNM=actionNode.getAttributes();
						String oId=actionNNM.getNamedItem("id").getNodeValue().toLowerCase();
						String oNext=actionNNM.getNamedItem("next").getNodeValue();
						Option option=new Option(oId,oNext);
						options.add(option);
					}
				}
				jun.setOptions(options);
			}else if("node".equals(nodeName)){
				id=nnM.getNamedItem("id").getNodeValue();
				String title=nnM.getNamedItem("title").getNodeValue();
				String next=nnM.getNamedItem("next").getNodeValue();
				String image=nnM.getNamedItem("image").getNodeValue();
				jun=new Junction(id,title,NodeType.EXEC);
				jun.setImage(image);
				jun.setNextNodeId(next);
			}else if("end".equals(nodeName)){
				id=nnM.getNamedItem("id").getNodeValue();
				String title=nnM.getNamedItem("title").getNodeValue();
				String image=nnM.getNamedItem("image").getNodeValue();
				jun=new Junction(id,title,NodeType.END);
				jun.setImage(image);
				endJunctionId=id;
			}
			if("start".equals(nodeName)
					||"node".equals(nodeName)
					||"end".equals(nodeName)){
				for (int l= 0; l< aoChildList.getLength();l++) {
					Node actionNode=aoChildList.item(l);
					if("action".equals(actionNode.getNodeName().toLowerCase())){
						NamedNodeMap actionNNM=actionNode.getAttributes();
						Node node=actionNNM.getNamedItem("type");
						if(node==null){
							throw new XMLException("动作类型不能为空");
						}
						String type=node.getNodeValue().toLowerCase();
						Action action=new Action();
						if("dialog".equals(type)){
							node=actionNNM.getNamedItem("message");
							if(node==null){
								throw new XMLException("对话框动作消息不能为空");
							}
							action.setMessage(node.getNodeValue());
							action.setType(ActionType.DIALOG);
						}else if("location".equals(type)){
							node=actionNNM.getNamedItem("message");
							if(node!=null){
								action.setMessage(node.getNodeValue());
							}
							node=actionNNM.getNamedItem("content");
							if(node==null){
								throw new XMLException("位置动作内容不能为空");
							}
							action.setContent(node.getNodeValue());
							action.setType(ActionType.LOCATION);
						}else if("openweb".equals(type)){
							node=actionNNM.getNamedItem("message");
							if(node!=null){
								action.setMessage(node.getNodeValue());
							}
							node=actionNNM.getNamedItem("url");
							if(node==null){
								throw new XMLException("打开浏览器动作url不能为空");
							}
							String url=node.getNodeValue();
							action.setUrl(url);
							action.setType(ActionType.OPENWEB);
						}
						
						jun.setAction(action);
					}
				}
				
			}
			if("start".equals(nodeName)
					||"switch".equals(nodeName)
					||"node".equals(nodeName)
					||"end".equals(nodeName)){
				junctions.put(id, jun);
			}
		}
	}
	
	public void init(){
		currentJunctionId=startJunctionId;
		nextJunctionId=currentJunctionId;
	}
	
	public void execute(){
		if(!currentJunctionId.equals(nextJunctionId)){
			currentJunctionId=nextJunctionId;
		}
		Junction jun=junctions.get(currentJunctionId);
		if(jun!=null){
			if(jun.getNodeType()==NodeType.START){
				
				nextJunctionId=jun.getNextNodeId();
			}else if(jun.getNodeType()==NodeType.SWITCH){
				final Option o1=jun.getOptions().get(0);
				final Option o2=jun.getOptions().get(1);
				final AlertDialog.Builder ad=new AlertDialog.Builder(this.mContext)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(jun.getTitle())
				.setPositiveButton(o1.getTitle(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						nextJunctionId=o1.getNextNodeId();
						//选择分支直接执行下一步
						execute();
						dialog.dismiss();
					}
				}).setNeutralButton(o2.getTitle(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						nextJunctionId=o2.getNextNodeId();
						//选择分支直接执行下一步
						execute();
						dialog.dismiss();
					}
				});
				if(jun.getOptions().size()==3){
					final Option o3=jun.getOptions().get(2);
					ad.setNegativeButton(o3.getTitle(), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							
							nextJunctionId=o3.getNextNodeId();
							//选择分支直接执行下一步
							execute();
							dialog.dismiss();
						}
					});
				}
				ad.show();
			}else if(jun.getNodeType()==NodeType.EXEC){
				
				nextJunctionId=jun.getNextNodeId();
			}else if(jun.getNodeType()==NodeType.END){
				
			}
			if(jun.getNodeType()!=NodeType.SWITCH&&jun.getAction()!=null){
				Action action=jun.getAction();
				if(action.getType()==ActionType.DIALOG){
					CommonFn.alertDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,int which) {
							dialog.dismiss();
						}
						
					}).show();
				}else if(action.getType()==ActionType.LOCATION){
					final String[] maps=action.getContent().split(";");
					if(action.getMessage()!=null){
						CommonFn.alertDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								processListener.location(maps[0], maps[1]);
								dialog.dismiss();
							}
							
						}).show();
					}else{
						processListener.location(maps[0], maps[1]);
					}
				}else if(action.getType()==ActionType.OPENWEB){
					final Intent intent=new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(action.getUrl()));
					if(action.getMessage()!=null){
						CommonFn.alertDialog(this.mContext, action.getMessage(), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								mContext.startActivity(intent);
								dialog.dismiss();
							}
							
						}).show();
					}else{
						this.mContext.startActivity(intent);
					}
				}
			}
			processListener.result(jun);
		}else{
			mAppContext.makeTextLong("当前节点不存在，所在节点：'"+currentJunctionId+"'，流程定义文件有误!");
		}
	}

	/**
	 * 判断当前流程是否已经结束
	 */
	public Boolean isProcessEnd(){
		return currentJunctionId.equals(endJunctionId);
	}
	
	public File getProcessImageFile() {
		return new File(Utils.getFile(mContext,mAppContext.getCurrentDataNo()),"process/"+processImage);
	}

	public void setProcessImage(String processImage) {
		this.processImage = processImage;
	}

	public interface ProcessListener{
		void result(Junction jun);
		void location(String mapId,String vertexId);
		void location(String mapId, String latitude,String longitude);
	}
	
}
