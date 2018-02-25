package com.haitao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.List;

import io.swagger.client.model.TalentModel;

/**
 * 达人馆
 * Created by tqy on 2015/11/20.
 */
public class TalentAdapter extends BaseListAdapter<TalentModel> {

    public TalentAdapter(Context context, List<TalentModel> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_talent, null);
            holder.ivImage = getView(convertView, R.id.ivImage);
            holder.tvName = getView(convertView, R.id.tvName);
            holder.tvHonor = getView(convertView, R.id.tvLevel);
            holder.tvThread = getView(convertView, R.id.tvPostNum);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TalentModel obj = mList.get(position);
        if (null != obj) {
            ImageLoaderUtils.showOnlineImage(obj.getAvatar(), holder.ivImage);
            holder.tvName.setText(obj.getUsername());
            holder.tvHonor.setText(obj.getCategory());
            holder.tvThread.setText(String.format("帖子：%s   |   精华：%s", obj.getTopicsCount(), obj.getDigestTopicsCount()));
            holder.viewSeparate.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }

        return convertView;
    }

    private class Holder {
        CustomImageView ivImage;
        TextView        tvName;
        TextView        tvHonor;
        TextView        tvThread;
        View            viewSeparate;
    }
}
