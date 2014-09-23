package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.start.core.CoreService;
import com.start.model.Doctor;

public class DoctorService extends CoreService {

	public DoctorService(Context context) {
		super(context);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	public List<Doctor> findAll(){
		List<Doctor> doctors = new ArrayList<Doctor>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Doctor.TABLE_NAME, 
				new String[]{
					Doctor.COLUMN_NAME_ID,
					Doctor.COLUMN_NAME_NAME,
					Doctor.COLUMN_NAME_SEX,
					Doctor.COLUMN_NAME_TITLE,
					Doctor.COLUMN_NAME_SPECIALTY,
					Doctor.COLUMN_NAME_INTRODUCTION,
					Doctor.COLUMN_NAME_DEPARTMENTID},
					Doctor.COLUMN_NAME_NO+" = ?",
					new String[]{getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Doctor doctor = new Doctor();
					doctor.setId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_ID)));
					doctor.setName(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_NAME)));
					doctor.setSex(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SEX)));
					doctor.setTitle(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_TITLE)));
					doctor.setSpecialty(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SPECIALTY)));
					doctor.setIntroduction(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_INTRODUCTION)));
					doctor.setDepartmentId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_DEPARTMENTID)));
					doctors.add(doctor);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return doctors;
	}
	
	public List<Doctor> findByDepartmentId(String departmentId){
		List<Doctor> doctors = new ArrayList<Doctor>();
		Cursor cursor = getDbHelper().getReadableDatabase().query(Doctor.TABLE_NAME, 
				new String[]{
					Doctor.COLUMN_NAME_ID,
					Doctor.COLUMN_NAME_NAME,
					Doctor.COLUMN_NAME_SEX,
					Doctor.COLUMN_NAME_TITLE,
					Doctor.COLUMN_NAME_SPECIALTY,
					Doctor.COLUMN_NAME_INTRODUCTION,
					Doctor.COLUMN_NAME_DEPARTMENTID},
					Doctor.COLUMN_NAME_DEPARTMENTID+" = ? AND "+Doctor.COLUMN_NAME_NO+" = ?",
					new String[]{departmentId,getCurrentDataNo()}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					Doctor doctor = new Doctor();
					doctor.setId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_ID)));
					doctor.setName(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_NAME)));
					doctor.setSex(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SEX)));
					doctor.setTitle(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_TITLE)));
					doctor.setSpecialty(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SPECIALTY)));
					doctor.setIntroduction(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_INTRODUCTION)));
					doctor.setDepartmentId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_DEPARTMENTID)));
					doctors.add(doctor);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return doctors;
	}
	
	public Doctor findById(String Id){
		Doctor doctor =null;
		Cursor cursor = getDbHelper().getReadableDatabase().query(Doctor.TABLE_NAME, 
				new String[]{
					Doctor.COLUMN_NAME_ID,
					Doctor.COLUMN_NAME_NAME,
					Doctor.COLUMN_NAME_SEX,
					Doctor.COLUMN_NAME_TITLE,
					Doctor.COLUMN_NAME_SPECIALTY,
					Doctor.COLUMN_NAME_INTRODUCTION,
					Doctor.COLUMN_NAME_DEPARTMENTID},
					Doctor.COLUMN_NAME_ID+" = ? AND "+Doctor.COLUMN_NAME_NO+" = ?",
					new String[]{Id,getCurrentDataNo()},null, null, null);
		try{
			if(cursor.moveToFirst()){
				do {
					doctor = new Doctor();
					doctor.setId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_ID)));
					doctor.setName(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_NAME)));
					doctor.setSex(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SEX)));
					doctor.setTitle(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_TITLE)));
					doctor.setSpecialty(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_SPECIALTY)));
					doctor.setIntroduction(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_INTRODUCTION)));
					doctor.setDepartmentId(cursor.getString(cursor.getColumnIndex(Doctor.COLUMN_NAME_DEPARTMENTID)));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return doctor;
	}
	
}
