package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.model.SectionObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class SectionDB {
    private static final String TAG = "SectionDB";

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public static void add(SectionObject obj) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.SECTION_TABLE, null, DBConstant.ID + "=?", new String[]{obj.fid}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.SECTION_ID, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ID)));
            cv.put(DBConstant.SECTION_P_ID, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_P_ID)));
            cv.put(DBConstant.SECTION_NAME, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_NAME)));
            cv.put(DBConstant.SECTION_POSTS, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_POSTS)));
            cv.put(DBConstant.SECTION_THREADS, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_THREADS)));
            cv.put(DBConstant.SECTION_TODAYPOSTS, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_TODAYPOSTS)));
            cv.put(DBConstant.SECTION_ICON, cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ICON)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.SECTION_TABLE, cv, DBConstant.ID + "=?", new String[]{obj.fid});
            Logger.d("update");
        } else {
            cv.put(DBConstant.ID, obj.fid);
            cv.put(DBConstant.SECTION_ID, obj.fid);
            cv.put(DBConstant.SECTION_P_ID, obj.pid);
            cv.put(DBConstant.SECTION_NAME, obj.name);
            cv.put(DBConstant.SECTION_POSTS, obj.posts);
            cv.put(DBConstant.SECTION_THREADS, obj.threads);
            cv.put(DBConstant.SECTION_TODAYPOSTS, obj.todayposts);
            cv.put(DBConstant.SECTION_ICON, obj.icon);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.SECTION_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<SectionObject> list) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.beginTransaction();
        for (SectionObject obj : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.ID, obj.fid);
            cv.put(DBConstant.SECTION_ID, obj.fid);
            cv.put(DBConstant.SECTION_P_ID, obj.pid);
            cv.put(DBConstant.SECTION_NAME, obj.name);
            cv.put(DBConstant.SECTION_POSTS, obj.posts);
            cv.put(DBConstant.SECTION_THREADS, obj.threads);
            cv.put(DBConstant.SECTION_TODAYPOSTS, obj.todayposts);
            cv.put(DBConstant.SECTION_ICON, obj.icon);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            //			FLog.e(TAG,cv.toString());
            db.insert(DBConstant.SECTION_TABLE, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.SECTION_TABLE, null, cv);
    }

    /**
     * 查找所有版块
     *
     * @return
     */
    public static ArrayList<SectionObject> getList() {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.SECTION_TABLE, null,
                null, null, null, null, DBConstant.SECTION_P_ID + " asc");
        ArrayList<SectionObject> list = new ArrayList<SectionObject>();
        while (cursor.moveToNext()) {
            SectionObject obj = new SectionObject();
            obj.fid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ID));
            obj.name = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_NAME));
            obj.pid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_P_ID));
            obj.posts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_POSTS));
            obj.threads = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_THREADS));
            obj.todayposts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_TODAYPOSTS));
            obj.icon = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ICON));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    /**
     * 根据版块名称查找所有版块
     *
     * @param name
     * @return
     */
    public static ArrayList<SectionObject> getListByName(String name) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.SECTION_TABLE, null,
                DBConstant.SECTION_NAME + " like ? ", new String[]{"%" + name + "%"}, null, null, DBConstant.SECTION_P_ID + " asc");
        ArrayList<SectionObject> list = new ArrayList<SectionObject>();
        while (cursor.moveToNext()) {
            SectionObject obj = new SectionObject();
            obj.fid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ID));
            obj.name = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_NAME));
            obj.pid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_P_ID));
            obj.posts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_POSTS));
            obj.threads = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_THREADS));
            obj.todayposts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_TODAYPOSTS));
            obj.icon = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ICON));
            //			FLog.e(TAG,obj.name);
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    /**
     * 根据父版块ID查找版块ID
     *
     * @param pid
     * @return
     */
    public static ArrayList<SectionObject> getList(String pid) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.SECTION_TABLE, null,
                DBConstant.SECTION_P_ID + "=?", new String[]{pid}, null, null, null);
        ArrayList<SectionObject> list = new ArrayList<SectionObject>();
        while (cursor.moveToNext()) {
            SectionObject obj = new SectionObject();
            obj.fid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ID));
            obj.name = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_NAME));
            obj.pid = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_P_ID));
            obj.posts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_POSTS));
            obj.threads = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_THREADS));
            obj.todayposts = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_TODAYPOSTS));
            obj.icon = cursor.getString(cursor.getColumnIndex(DBConstant.SECTION_ICON));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    public static void clear() {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.SECTION_TABLE, null, null);
    }
}
