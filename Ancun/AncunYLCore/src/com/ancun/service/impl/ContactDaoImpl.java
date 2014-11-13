package com.ancun.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.core.AppConstant;
import start.utils.StringUtils;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;

import com.ancun.core.DBManageDao;
import com.ancun.yl.adapter.ContactAdapter;

public class ContactDaoImpl extends DBManageDao {

	public ContactDaoImpl(Context mContext){
		super(mContext);
	}
	
	/**
	 * 加载本地通讯录信息
	 */
	public List<Map<String,String>> loadAllContact() {
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.Contacts.CONTENT_URI, projection, null, null, sortOrder);
        List<Map<String,String>> datas=new ArrayList<Map<String,String>>();
		try{
			if (cursor.moveToFirst()) {
				do {
					Map<String,String> data=new HashMap<String,String>();
					data.put(ContactAdapter.STRID, StringUtils.convertString(cursor.getLong(0)));
					data.put(ContactAdapter.STRNAME, StringUtils.convertString(cursor.getString(1)));
					data.put(ContactAdapter.STRPHONEID, StringUtils.convertString(cursor.getLong(2)));
					data.put(ContactAdapter.STRLOOKUPKEY, StringUtils.convertString(cursor.getString(3)));
					datas.add(data);
				} while (cursor.moveToNext());
			}
		}finally{
			if(null!=cursor){
				cursor.close();
			}
		}
		return datas;
	}
	
	/**
	 * 根据号码获取联系人信息
	 */
	public Map<String,String> getContactByPhone(String phone){
		String[] projection = {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.Data.DATA1, 
				ContactsContract.Contacts.LOOKUP_KEY,
				ContactsContract.Contacts.PHOTO_ID };
		// 获得所有的联系人
		Cursor cursor=null;
		Map<String,String> data=null;
		try{
			 cursor = getContext().getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, ContactsContract.Contacts.Data.DATA1+" like ?",
					new String[]{phone},null);
			if(cursor.moveToFirst()) {
				do{
					String tmpPhone=StringUtils.convertString(cursor.getString(2));
					if(phone.equals(StringUtils.phoneFormat(tmpPhone))){
						data=new HashMap<String,String>();
						data.put(ContactAdapter.STRID, StringUtils.convertString(cursor.getLong(0)));
						data.put(ContactAdapter.STRNAME, StringUtils.convertString(cursor.getString(1)));
						data.put(ContactAdapter.STRPHONE, tmpPhone);
						data.put(ContactAdapter.STRLOOKUPKEY, StringUtils.convertString(cursor.getString(3)));
						data.put(ContactAdapter.STRPHONEID, StringUtils.convertString(cursor.getLong(4)));
						break;
					}
				}while(cursor.moveToNext());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=cursor){
				cursor.close();
			}
		}
		return data;
	}
	
	/**
	 * 根据唯一键获取当前用户对应的号码列表
	 */
	public List<String> getContactAllPhone(String key){
		List<String> phones=new ArrayList<String>();
        String[] projection = new String[] {
        		ContactsContract.Contacts.Data.DATA1
        };
        String sortOrder = ContactsContract.Contacts.Data.DATA1+ " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
        		projection,
        		ContactsContract.Contacts.LOOKUP_KEY+"=?",new String[]{key}, sortOrder);
		try{
			if (cursor.moveToFirst()) {
				do {
					phones.add(StringUtils.convertString(cursor.getString(0)));
				} while (cursor.moveToNext());
			}
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		return phones;
	}
	
	/**
	 * 根据号码获取对应的联系人姓名多个用逗号分隔
	 */
	@SuppressWarnings("deprecation")
	public String getContactName(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return AppConstant.EMPTYSTR;
		}
		ContentResolver resolver = getContext().getContentResolver();
		Uri lookupUri = null;
		Cursor cursor = null;
		String[] projection = new String[] {PhoneLookup._ID,
				PhoneLookup.DISPLAY_NAME };
		try {
			lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phone));
			cursor = resolver.query(lookupUri, projection, null, null, null);
		} catch (Exception ex) {
			try {
				lookupUri = Uri.withAppendedPath(
						android.provider.Contacts.Phones.CONTENT_FILTER_URL,
						Uri.encode(phone));
				cursor = resolver
						.query(lookupUri, projection, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		StringBuilder names = new StringBuilder();
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					do {
						names.append(StringUtils.convertString(cursor.getString(1)) + ",");
					} while (cursor.moveToNext());
				}
				//去掉最后一个逗号
				if (names.length() > 0) {
					names.deleteCharAt(names.length() - 1);
				}
			} finally {
				cursor.close();
			}
		}
		return String.valueOf(names);
	}
	
	/**
	 * 加载当前ID所对应的联系人头像
	 */
	public Bitmap loadContactPhoto(Long id) {
		InputStream localInputStream = null;
		try {
			Uri localUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
			localInputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), localUri);
			return BitmapFactory.decodeStream(localInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(localInputStream!=null){
				try {
					localInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					localInputStream=null;
				}
			}
		}
		return null;
	}
	
}