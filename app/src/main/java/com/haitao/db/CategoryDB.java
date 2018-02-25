package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.model.TagObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class CategoryDB {
    private static final String TAG = "CategoryDB";

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public static void add(TagObject obj, String type) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.CATEGORY_TABLE, null, DBConstant.CATEGORY_ID + "=? and " + DBConstant.CATEGORY_TYPE + "=?", new String[]{obj.id, type}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.CATEGORY_ID, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_ID)));
            cv.put(DBConstant.CATEGORY_TEXT, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TEXT)));
            cv.put(DBConstant.CATEGORY_BRANCH, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_BRANCH)));
            cv.put(DBConstant.CATEGORY_TYPE, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TYPE)));
            cv.put(DBConstant.CATEGORY_PIC, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_PIC)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.CATEGORY_TABLE, cv, DBConstant.CATEGORY_ID + "=? and " + DBConstant.CATEGORY_TYPE + "=?", new String[]{obj.id, type});
            Logger.d("update");
        } else {
            cv.put(DBConstant.CATEGORY_ID, obj.id);
            cv.put(DBConstant.CATEGORY_TEXT, obj.text);
            cv.put(DBConstant.CATEGORY_BRANCH, obj.branch);
            cv.put(DBConstant.CATEGORY_PIC, obj.pic);
            cv.put(DBConstant.CATEGORY_TYPE, type);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CATEGORY_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<TagObject> list, String type) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.beginTransaction();
        for (TagObject obj : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.CATEGORY_ID, obj.id);
            cv.put(DBConstant.CATEGORY_TEXT, obj.text);
            cv.put(DBConstant.CATEGORY_BRANCH, obj.branch);
            cv.put(DBConstant.CATEGORY_PIC, obj.pic);
            cv.put(DBConstant.CATEGORY_TYPE, type);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CATEGORY_TABLE, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.CATEGORY_TABLE, null, cv);
    }


    public static ArrayList<TagObject> getList(String type) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.CATEGORY_TABLE, null,
                DBConstant.CATEGORY_TYPE + "=?", new String[]{type}, null, null, null);
        ArrayList<TagObject> list = new ArrayList<TagObject>();
        while (cursor.moveToNext()) {
            TagObject obj = new TagObject();
            obj.id = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_ID));
            obj.text = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TEXT));
            obj.branch = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_BRANCH));
            obj.pic = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_PIC));
            obj.type = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TYPE));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    public static void clear() {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.CATEGORY_TABLE, null, null);
    }
}
