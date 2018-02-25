package com.haitao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haitao.activity.PreviewActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.PhotoPickParameterObject;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Adapter基类
 *
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context        mContext;
    protected LayoutInflater mInflater;

    protected List<T> mList;

    public BaseListAdapter(Context context, List<T> data) {
        mContext = context;
        mList = data;
        mInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    /**
     * 通过泛型来简化findViewById
     */
    protected final <E extends View> E getView(View v, int id) {
        try {
            return (E) v.findViewById(id);
        } catch (ClassCastException ex) {
            Logger.e(ex, "Could not cast View to concrete class.");
            throw ex;
        }
    }

    //图片预览
    protected void openImagePreview(PhotoPickParameterObject mPhotoPickParameterInfo, int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);
        mContext.startActivity(intent);
    }

}
