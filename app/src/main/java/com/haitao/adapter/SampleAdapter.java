package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TopicSendActivity;
import com.haitao.model.SampleObject;
import com.haitao.model.forum.BoardObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

/**
 * 试用 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class SampleAdapter extends BaseListAdapter<SampleObject> {
    public boolean isOver            = false;
    public int     overFirstPosition = 0;
    //是否申请成功
    public boolean isSuccessApply    = false;

    public SampleAdapter(Context context, List<SampleObject> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_sample, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.layoutStatus = getView(convertView, R.id.layoutStatus);
            holder.tvStatus = getView(convertView, R.id.tvStatus);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvAmount = getView(convertView, R.id.tvAmount);
            holder.tvOver = getView(convertView, R.id.tvOver);
            holder.tvPost = getView(convertView, R.id.tvPost);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final SampleObject obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.image, holder.ivImage);
            holder.tvTitle.setText(obj.title);
            holder.tvStatus.setText("1".equals(obj.type) ? "即将开始" : "2".equals(obj.type) ? "正在进行" : "已结束");
            holder.layoutStatus.setBackgroundResource("1".equals(obj.type) ? R.color.half_blue : "2".equals(obj.type) ? R.color.half_orange : R.color.half_transparent);
            String amount = String.format(mContext.getResources().getString(R.string.sample_amount), obj.number, obj.apply_count, obj.time);
            //CharSequence charsProvider = ColorPhrase.from(parseSampleValue(amount)).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
            holder.tvAmount.setText(amount);
            holder.tvPost.setVisibility(isSuccessApply && !"1".equals(obj.report) ? View.VISIBLE : View.GONE);
            if ("3".equals(obj.type)) {
                if (isOver && position == overFirstPosition) {
                    holder.tvOver.setVisibility(View.VISIBLE);
                } else if (!isOver) {
                    holder.tvOver.setVisibility(View.VISIBLE);
                    overFirstPosition = position;
                    isOver = true;
                } else {
                    holder.tvOver.setVisibility(View.GONE);
                }
            } else {
                holder.tvOver.setVisibility(View.GONE);
            }
            holder.tvPost.setOnClickListener(v -> {
                BoardObject sectionObject = new BoardObject();
                sectionObject.id = obj.fid;
                sectionObject.typeid = obj.typeid;
                sectionObject.categoryName = obj.forum_name;
                TopicSendActivity.launch(mContext, sectionObject);
            });
            // 最后一条隐藏分割线
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }

    private class Holder {
        View            viewSeparate;
        CustomImageView ivImage;
        ViewGroup       layoutStatus;
        TextView        tvStatus;
        TextView        tvTitle;
        TextView        tvAmount;
        TextView        tvOver;
        TextView        tvPost;
    }

    /**
     * 重组试用中的数字
     *
     * @param str
     * @return
     */
    public static String parseSampleValue(String str) {
        String regApp = "([0-9\\.]+)";
        str = str.replaceAll(regApp, "{$1}");
        return str;
    }
}
