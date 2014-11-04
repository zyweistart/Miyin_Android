package com.ancun.bean.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

import com.ancun.bean.ContactModel;
import com.ancun.core.DBManageDao;

public class ContactDaoImpl extends DBManageDao {

	public ContactDaoImpl(Context mContext){
		super(mContext);
	}
	
	public List<ContactModel> loadAllContact() {
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.Contacts.CONTENT_URI, projection, null, null, sortOrder);
        List<ContactModel> contactListData=new ArrayList<ContactModel>();
		try{
			if (cursor.moveToFirst()) {
				do {
					ContactModel mContactInfo = new ContactModel();
					mContactInfo.setId(cursor.getLong(0));
					mContactInfo.setName(cursor.getString(1));
					mContactInfo.setPhotoID(cursor.getLong(2));
					mContactInfo.setLookupKey(cursor.getString(3));
					contactListData.add(mContactInfo);
				} while (cursor.moveToNext());
			}
		}finally{
			if(null!=cursor){
				cursor.close();
				
			}
		}
		return contactListData;
	}
	
	/**
	 * 根据唯一键获取当前用户的手机号码列表
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
					phones.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return phones;
	}
	
	/**
	 * 根据号码获取联系人信息
	 */
	public ContactModel getContactModelByPhone(String phone){
		String[] projection = {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.Data.DATA1, 
				ContactsContract.Contacts.LOOKUP_KEY,
				ContactsContract.Contacts.PHOTO_ID };
		// 获得所有的联系人
		Cursor cursor=null;
		ContactModel mContactInfo=null;
		try{
			 cursor = getContext().getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, ContactsContract.Contacts.Data.DATA1+" like ?",
					new String[]{phone},null);
			if(cursor.moveToFirst()) {
				do{
					String tmpPhone=cursor.getString(2);
					if(StringUtils.phoneFormat(tmpPhone).equals(phone)){
						mContactInfo = new ContactModel();
						mContactInfo.setId(cursor.getLong(0));
						mContactInfo.setName(cursor.getString(1));
						mContactInfo.setPhone(tmpPhone);
						mContactInfo.setLookupKey(cursor.getString(3));
						mContactInfo.setPhotoID(cursor.getLong(4));
						break;
					}
				}while(cursor.moveToNext());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(null!=cursor){
				cursor.close();
			}
		}
		return mContactInfo;
	}
	/**
	 * 加载当前联系人头像
	 */
	public Bitmap loadContactPhoto(Long id) {
		InputStream localInputStream = null;
		try {
			Uri localUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
			localInputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), localUri);
			return BitmapFactory.decodeStream(localInputStream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(localInputStream!=null){
				try {
					localInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}finally{
					localInputStream=null;
				}
			}
		}
	}
	
	public Set<String> findAllContactPhone() {
		Set<String> phones=new LinkedHashSet<String>();
		String[] projection = new String[] {
        		ContactsContract.Contacts.Data.DATA1
        };
        String sortOrder = ContactsContract.Contacts.Data.DATA1+ " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
        		projection,null,null, sortOrder);
		try{
			if (cursor.moveToFirst()) {
				do {
					phones.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return phones;
	}
	
	@SuppressWarnings("deprecation")
	public String getContactName(String number) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        final ContentResolver resolver =getContext().getContentResolver();
        Uri lookupUri = null;
        String[] projection = new String[] { PhoneLookup._ID, PhoneLookup.DISPLAY_NAME };
        Cursor cursor = null;
        try {
            lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            cursor =resolver.query(lookupUri, projection, null, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                lookupUri = Uri.withAppendedPath(android.provider.Contacts.Phones.CONTENT_FILTER_URL,
                        Uri.encode(number));
                cursor = resolver.query(lookupUri, projection, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuilder names=new StringBuilder();
        if (cursor.moveToFirst()) {
			do {
				names.append(cursor.getString(1)+",");
			} while (cursor.moveToNext());
		}
        if(names.length()>0){
        	names.deleteCharAt(names.length()-1);
        }
        cursor.close();
        return String.valueOf(names);
    }
	
}