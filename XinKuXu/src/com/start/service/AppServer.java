package com.start.service;

import java.io.File;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import android.content.Context;
import android.util.Log;

import com.start.core.BaseActivity;
import com.start.service.bean.WordItem;
import com.start.xinkuxue.BaseContext;

public class AppServer {

	/**
	 * 检测应用更新
	 */
	public static void checkAppUpdate(BaseActivity activity,Boolean status){
		UpdateApplication updateApplication = new UpdateApplication(activity);
		updateApplication.startCheck(status);
	}
	
	public static String ToDBC(String input) {          
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {              
        if (c[i] == 12288) {                 
        c[i] = (char) 32;                  
        continue;
         }
         if (c[i] > 65280 && c[i] < 65375)
            c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }  
	
	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style>]*?>[\s\S]*?<\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr.trim();
	}

	public static void importExcelWord(Context context) {
		try {
			Log.v("AppServer","准备导入数据.....");
			WordService mWordService=new WordService(context);
			String path=BaseContext.getInstance().getStorageDirectory("data")+"20150619.xls";
			Workbook book = Workbook.getWorkbook(new File(path));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int Rows = sheet.getRows();
			for (int i = 1; i < Rows; ++i) {
				WordItem w=new WordItem();
				w.setId((sheet.getCell(0, i)).getContents());
				w.setEnglishName((sheet.getCell(1, i)).getContents());
				w.setPhoneticSymbols((sheet.getCell(2, i)).getContents());
				w.setChineseSignificance((sheet.getCell(3, i)).getContents());
				w.setExampleEnglish((sheet.getCell(4, i)).getContents());
				w.setExampleChinese((sheet.getCell(5, i)).getContents());
				w.setFillProblem((sheet.getCell(6, i)).getContents().replaceAll("        ", "_________"));
				w.setFillAnswer((sheet.getCell(7, i)).getContents());
				w.setMemoryMethodA((sheet.getCell(8, i)).getContents());
				w.setMemoryMethodB((sheet.getCell(9, i)).getContents());
//				Log.v(TAG,w.getFillProblem());
				mWordService.saveToWordItem(w);
				Log.v("AppServer","第"+w.getId()+"条数据插入成功");
			}
			Log.v("AppServer","导入成功");
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
}
