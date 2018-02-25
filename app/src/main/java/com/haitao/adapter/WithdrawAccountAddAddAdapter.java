package com.haitao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.WithdrawingModeModel;

/**
 * 新增提现方式列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class WithdrawAccountAddAddAdapter extends BaseListAdapter<WithdrawingModeModel> {
    //    public TagObject currentObj = null;
    //    public String    mSelectId   = "";

    public WithdrawAccountAddAddAdapter(Context context, List<WithdrawingModeModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pay_style_add, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WithdrawingModeModel obj = mList.get(position);
        if (null != obj) {
            holder.mTvName.setText(obj.getModeName());
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.mIvImage);
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)    CustomImageView mIvImage;
        @BindView(R.id.tvName)     TextView        mTvName;
        @BindView(R.id.divider)    View            mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
