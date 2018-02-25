package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.AreaModel;

/**
 * 选择国家区号列表 - Adapter
 *
 * @author 陶声
 * @since 2017-11-16
 */

public class CountryCodeChooseAdapter extends BaseListAdapter<AreaModel> {
    public String selectId;

    public CountryCodeChooseAdapter(Context context, List<AreaModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_choose_country, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        AreaModel obj = mList.get(position);
        if (null != obj) {
            // 国家名
            holder.mTvCountryName.setText(obj.getAreaName());
            // 国家码
            holder.mTvCountryCode.setText(obj.getAreaCode());
            // 选中状态
            holder.mImgRadio.setSelected(TextUtils.equals(obj.getAreaId(), selectId));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_country_name) TextView  mTvCountryName;   // 国家名
        @BindView(R.id.tv_country_code) TextView  mTvCountryCode;   // 国家码
        @BindView(R.id.img_radio)       ImageView mImgRadio;        // radio
        @BindView(R.id.divider)         View      mDivider;         // 分割线

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
