package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.fragment.StoreFragment;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.StoreIndexIfModelDataAreasBriefs;

/**
 * 商家{@link StoreFragment} 国家图标列表
 *
 * @author 陶声
 * @since 2017-12-05
 */
public class CountryIconAdapter extends BaseListAdapter<StoreIndexIfModelDataAreasBriefs> {

    public CountryIconAdapter(Context context, List<StoreIndexIfModelDataAreasBriefs> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_country_icon, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreIndexIfModelDataAreasBriefs obj = mList.get(position);
        if (obj != null) {
            holder.mTvCountryName.setText(obj.getAreaName());
            // 国旗图标
            ImageLoaderUtils.showOnlineImage(obj.getAreaFlagPic(), holder.mImgCountry);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_country)     CustomImageView mImgCountry;
        @BindView(R.id.tv_country_name) TextView        mTvCountryName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
