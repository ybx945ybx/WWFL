package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.model.CityObject;
import com.haitao.view.wheel.adapters.AbstractWheelTextAdapter;

import java.util.ArrayList;

/**
 * Created by tqy on 2015/11/20.
 */
public class CityAdapter extends AbstractWheelTextAdapter {

    ArrayList<CityObject> list;
    public int currentIndex = 0;

    public CityAdapter(Context context, ArrayList<CityObject> list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.item_area, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index).name;
    }


}
