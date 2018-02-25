package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.model.LogisticsCompanyObject;

import java.util.ArrayList;

public class TransferDB {
	private static final String TAG = "TransferDB";


	/**
	 * 批量添加
	 * @param list
	 */
	public static void add(ArrayList<LogisticsCompanyObject> list){
		SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
		db.beginTransaction();
		for (LogisticsCompanyObject obj : list) {
			ContentValues cv = new ContentValues();
			cv.put(DBConstant.TRANSFER_ID, obj.id);
			cv.put(DBConstant.TRANSFER_NAME, obj.name);
			cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
			db.insert(DBConstant.TRANSFER_TABLE, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public static void add(ContentValues cv){
		SQLiteDatabase db = SQLiteUtil.getInstance()
				.getSqLiteDatabase();
		db.insert(DBConstant.TRANSFER_TABLE, null, cv);
	}
	
	public static ArrayList<LogisticsCompanyObject> getList(String name){
		SQLiteDatabase db = SQLiteUtil.getInstance()
				.getSqLiteDatabase();
		Cursor cursor = db.query(DBConstant.TRANSFER_TABLE, null, DBConstant.TRANSFER_NAME +" like ?", new String[]{"%"+name+"%"},
				null, null, null);
		ArrayList<LogisticsCompanyObject> list = new ArrayList<LogisticsCompanyObject>();
		while (cursor.moveToNext()) {
			LogisticsCompanyObject obj = new LogisticsCompanyObject();
			obj.id = cursor.getString(cursor.getColumnIndex(DBConstant.TRANSFER_ID));
			obj.name = cursor.getString(cursor.getColumnIndex(DBConstant.TRANSFER_NAME));
			list.add(obj);
		}
		if (null != cursor) {
			cursor.close();
		}
		return list;
	}
	
	public static void clear(){
		SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
		db.delete(DBConstant.TRANSFER_TABLE, null, null);
	}
}
