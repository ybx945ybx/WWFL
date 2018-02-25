package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.model.LogisticsCompanyObject;

import java.util.List;

/**
 * Created by tqy on 2015/11/20.
 */
public class LogisticsCompanyAdapter extends BaseListAdapter<LogisticsCompanyObject>{
    public LogisticsCompanyObject companyObject;
    public LogisticsCompanyAdapter(Context context, List<LogisticsCompanyObject> data) {
        super(context, data);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_logistics_company, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.tvCompany = getView(convertView,R.id.tvCompany);
            holder.icStatus = getView(convertView,R.id.icStatus);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        LogisticsCompanyObject obj = mList.get(position);
        if(null != obj){
            holder.tvCompany.setText(obj.name);
            holder.icStatus.setVisibility(null != companyObject && companyObject.id.equals(obj.id) ? View.VISIBLE : View.GONE);
        }
        return convertView;
    }

    private class Holder {
        View viewSeparate;
        ImageView icStatus;
        TextView tvCompany;
    }

}
