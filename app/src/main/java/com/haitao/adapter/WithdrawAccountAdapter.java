package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.UserWithdrawingModeModel;

/**
 * 提现账户列表 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class WithdrawAccountAdapter extends BaseListAdapter<UserWithdrawingModeModel> {
    public String selectId = "";
    private int                 mFirstIndex;
    //    private boolean mShowSelect;
    private OnMoreClickListener mOnMoreClickListener;

    public WithdrawAccountAdapter(Context context, List<UserWithdrawingModeModel> data) {
        super(context, data);
        mFirstIndex = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_account, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserWithdrawingModeModel obj = mList.get(position);
        if (null != obj) {
            holder.mTvName.setText(obj.getModeName());
            holder.mTvDesc.setText(obj.getAccount());
            holder.mTvPending.setVisibility("0".equals(obj.getStatus()) ? View.VISIBLE : View.GONE);
            holder.mLlCheckPending.setVisibility(mFirstIndex >= 0 && mFirstIndex == position ? View.VISIBLE : View.GONE);
            ImageLoaderUtils.showOnlineImage(obj.getIcon(), holder.mIvImage);
            // 隐藏最后一条分割线
            holder.mViewDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
            // 点击事件
            holder.mImgMore.setOnClickListener(v -> {
                if (mOnMoreClickListener != null) {
                    mOnMoreClickListener.onMoreClick(obj.getAccountId());
                }
            });
        }
        return convertView;
    }

    /**
     * 设置第一个待审核的位置
     *
     * @param firstIndex 位置
     */
    public void setFirstCheckingIndex(int firstIndex) {
        mFirstIndex = firstIndex;
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)          CustomImageView mIvImage;
        @BindView(R.id.tvName)           TextView        mTvName;
        @BindView(R.id.tvPending)        TextView        mTvPending;
        @BindView(R.id.tvDesc)           TextView        mTvDesc;
        @BindView(R.id.tv_check_pending) TextView        mTvCheckPending;
        @BindView(R.id.ll_check_pending) LinearLayout    mLlCheckPending;
        @BindView(R.id.img_more)         ImageView       mImgMore;
        @BindView(R.id.view_divider)     View            mViewDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 条目点击回调
     */
    public interface OnMoreClickListener {
        void onMoreClick(String id);
    }

    /**
     * 设置点击回调
     *
     * @param onMoreClickListener 回调
     */
    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        mOnMoreClickListener = onMoreClickListener;
    }
}
