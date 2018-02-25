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
import io.swagger.client.model.BanksIfModelData;

/**
 * 银行列表 - Adapter
 *
 * @author 陶声
 * @since 2018-01-30
 */

public class BankSelectAdapter extends BaseListAdapter<BanksIfModelData> {
    public String mSelectId;

    public BankSelectAdapter(Context context, List<BanksIfModelData> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_bank_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        BanksIfModelData obj = mList.get(position);
        if (null != obj) {
            // 银行名称
            holder.mTvBank.setText(obj.getBankName());
            // 选中状态
            holder.mImgRadio.setSelected(TextUtils.equals(obj.getBankId(), mSelectId));
            // 隐藏最后一条分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    /**
     * 设置选中的条目id
     *
     * @param selectId id
     */
    public void setSelectId(String selectId) {
        mSelectId = selectId;
    }

    static class ViewHolder {
        @BindView(R.id.tv_bank)   TextView  mTvBank;
        @BindView(R.id.img_radio) ImageView mImgRadio;
        @BindView(R.id.divider)   View      mDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
