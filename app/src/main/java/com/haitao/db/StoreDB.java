package com.haitao.db;

import android.database.Cursor;

import com.haitao.db.v2.StoreModel;
import com.haitao.model.StoreFilterObject;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StoreDB {
    private static final String TAG = "StoreDB";


    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<StoreModel> list) {
        DataSupport.deleteAll(StoreModel.class);
        Logger.d("=====delete Over======");
        if (null != list && list.size() > 0) {
            try {
                DataSupport.saveAll(list);
            } catch (Exception e) {

            }
            Logger.d("=====save Over======");
        }

    }

    /**
     * 数量
     *
     * @param
     */
    public static int count() {
        return DataSupport.count(StoreModel.class);

    }


    /**
     * storeSelections
     *
     * @return
     */
    public static ArrayList<StoreFilterObject> getListGroup() {
        Logger.d("=== getListGroup == begin");

        ArrayList<StoreFilterObject> list   = new ArrayList<StoreFilterObject>();
        Cursor                       cursor = DataSupport.findBySQL("select distinct character from StoreModel order by character asc");
        while (cursor.moveToNext()) {
            StoreFilterObject storeFilterObject = new StoreFilterObject();
            storeFilterObject.char_name = cursor.getString(cursor.getColumnIndex("character"));
            storeFilterObject.list = new ArrayList<StoreModel>();
            storeFilterObject.list.addAll(DataSupport.where("character = ?", storeFilterObject.char_name).limit(3).find(StoreModel.class));
            list.add(storeFilterObject);
        }
        Logger.d("===getListGroup end==");
        return list;
    }


    /**
     * 根据条件来判断
     *
     * @return
     */
    public static List<StoreModel> getList(String selection, String selectionArgs) {
        return DataSupport.where(selection, selectionArgs).find(StoreModel.class);
    }

    /**
     * 根据条件来判断
     *
     * @return
     */
    public static ArrayList<StoreFilterObject> getListGroup(String selection, ArrayList<String> selectionArgs) {
        ArrayList<StoreFilterObject> list  = new ArrayList<StoreFilterObject>();
        String                       where = String.format("select distinct character from StoreModel where %s order by character asc", selection);

        ArrayList<String> characterSelections = new ArrayList<String>();
        characterSelections.addAll(selectionArgs);
        characterSelections.add(0, where);
        Cursor cursor     = DataSupport.findBySQL((String[]) characterSelections.toArray(new String[characterSelections.size()]));
        String storeWhere = selection + " and character = ?";
        while (cursor.moveToNext()) {
            StoreFilterObject storeFilterObject = new StoreFilterObject();
            storeFilterObject.char_name = cursor.getString(cursor.getColumnIndex("character"));
            storeFilterObject.list = new ArrayList<StoreModel>();
            ArrayList<String> storeSelections = new ArrayList<String>();
            storeSelections.addAll(selectionArgs);
            storeSelections.add(storeFilterObject.char_name);
            storeSelections.add(0, storeWhere);
            storeFilterObject.list.addAll(DataSupport.where((String[]) storeSelections.toArray(new String[storeSelections.size()])).limit(3).find(StoreModel.class));
            list.add(storeFilterObject);
        }
        return list;
    }

    public static void clear() {
        DataSupport.deleteAll(StoreModel.class);
    }
}
