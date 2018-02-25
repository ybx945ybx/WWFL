package com.haitao.db;

import android.database.Cursor;

import com.haitao.db.v2.TransportModel;
import com.haitao.model.TransportFilterObject;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class TransportDB {
    private static final String TAG = "TransportDB";


    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<TransportModel> list) {
        DataSupport.deleteAll(TransportModel.class);
        Logger.d(" ====== delete Over ===== ");
        if (null != list && list.size() > 0) {
            DataSupport.saveAll(list);
            Logger.d(" ====== save Over ===== ");
        }
    }


    public static ArrayList<TransportFilterObject> getListGroup() {
        Logger.d(" ====== getListGroup begin ===== ");
        ArrayList<TransportFilterObject> list   = new ArrayList<TransportFilterObject>();
        Cursor                           cursor = DataSupport.findBySQL("select distinct character from TransportModel order by character asc");
        while (cursor.moveToNext()) {
            TransportFilterObject transportFilterObject = new TransportFilterObject();
            transportFilterObject.char_name = cursor.getString(cursor.getColumnIndex("character"));
            transportFilterObject.list = new ArrayList<TransportModel>();
            transportFilterObject.list.addAll(DataSupport.where("character = ?", transportFilterObject.char_name).find(TransportModel.class));
            list.add(transportFilterObject);
        }
        Logger.d(" ====== getListGroup end ===== ");
        return list;

    }


    /**
     * 根据条件来判断
     *
     * @return
     */
    public static ArrayList<TransportFilterObject> getListGroup(String selection, ArrayList<String> selectionArgs) {
        ArrayList<TransportFilterObject> list = new ArrayList<TransportFilterObject>();
        if (selection.contains("1=1")) {
            String where = String.format("select distinct character from TransportModel where %s order by character asc", selection);

            ArrayList<String> characterSelections = new ArrayList<String>();
            characterSelections.addAll(selectionArgs);
            characterSelections.add(0, where);

            Cursor cursor         = DataSupport.findBySQL((String[]) characterSelections.toArray(new String[characterSelections.size()]));
            String transportWhere = selection + " and character = ?";
            while (cursor.moveToNext()) {
                TransportFilterObject transportFilterObject = new TransportFilterObject();
                transportFilterObject.char_name = cursor.getString(cursor.getColumnIndex("character"));
                transportFilterObject.list = new ArrayList<TransportModel>();
                ArrayList<String> transportSelections = new ArrayList<String>();
                transportSelections.addAll(selectionArgs);
                transportSelections.add(transportFilterObject.char_name);
                transportSelections.add(0, transportWhere);

                transportFilterObject.list.addAll(DataSupport.where((String[]) transportSelections.toArray(new String[transportSelections.size()])).find(TransportModel.class));
                list.add(transportFilterObject);
            }
        } else {
            TransportFilterObject transportFilterObject = new TransportFilterObject();
            transportFilterObject.char_name = "";
            transportFilterObject.list = new ArrayList<TransportModel>();
            ArrayList<String> transportSelections = new ArrayList<String>();
            transportSelections.addAll(selectionArgs);
            transportSelections.add(0, selection);

            transportFilterObject.list.addAll(DataSupport.where((String[]) transportSelections.toArray(new String[transportSelections.size()])).find(TransportModel.class));
            list.add(transportFilterObject);
        }
        return list;
    }

    public static void clear() {
        DataSupport.deleteAll(TransportModel.class);
    }
}
