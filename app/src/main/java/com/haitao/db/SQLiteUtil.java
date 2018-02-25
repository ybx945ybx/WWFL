package com.haitao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.common.HtApplication;

/**
 * 数据库操作工具类
 * @author 
 *
 */
public class SQLiteUtil {

	 // 数据库辅助类
    private static DbHelper dbHelper = null;
    // 数据库
    private static SQLiteDatabase db = null;
    private static SQLiteUtil dbUtil = null;

    private SQLiteUtil(Context context) {
    }
    
    public static SQLiteUtil getInstance(){
    	if (null == dbUtil) {
    		dbUtil = new SQLiteUtil(HtApplication.getInstance());
		}
    	if (null == dbHelper) {
    		dbHelper = new DbHelper(HtApplication.getInstance());
		}
    	return dbUtil;
    }

    public SQLiteDatabase getSqLiteDatabase() {
    	if (null == db) {
    		db = dbHelper.getWritableDatabase();
		}
        return db;
    }
    
    public SQLiteDatabase getSqLiteReadableDatabase() {
    	if (null == db) {
    		db = dbHelper.getReadableDatabase();
		}
        return db;
    }
	
}
