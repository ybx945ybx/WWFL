package com.haitao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.view.FullGirdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.swagger.client.model.MyReplyModel;

/**
 * 我的帖子 - 评论 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class PostCommentAdapter extends BaseListAdapter<MyReplyModel> {


    public PostCommentAdapter(Context context, List<MyReplyModel> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_post_comment, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyReplyModel obj = mList.get(position);
        if (null != obj) {
            // 内容
            if (!TextUtils.isEmpty(obj.getContent())) {
                holder.mTvReplyContent.setText(obj.getContent());
            }
            // 时间
            holder.mTvTime.setText(obj.getReplyTime());
            // 帖子作者
            holder.mTvUsername.setText("@" + obj.getSource().getAuthorName());
            // 评论帖子
            holder.mTvPostContent.setText(obj.getSource().getTitle());
            // 跳转帖子详情
            holder.mLlContainer.setOnClickListener(v -> TopicDetailActivity.launch(mContext, obj.getSource().getTid()));
            // 回复内容图片
            if (null != obj.getPics() && obj.getPics().size() > 0) {
                holder.mGvPics.setVisibility(View.VISIBLE);
                CommentImageAdapter mAdapter = new CommentImageAdapter(mContext, obj.getPics());
                holder.mGvPics.setAdapter(mAdapter);
                PhotoPickParameterObject photoPickObject = new PhotoPickParameterObject();
                photoPickObject.image_list = (ArrayList<String>) obj.getPics();
                holder.mGvPics.setOnItemClickListener((parent1, view, position1, id) -> openImagePreview(photoPickObject, position1));
            } else {
                holder.mGvPics.setVisibility(View.GONE);
            }
            // 最后一条隐藏分割线
            holder.mDivider.setVisibility(mList.indexOf(obj) == mList.size() - 1 ? View.GONE : View.VISIBLE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_reply_content) TextView     mTvReplyContent;
        @BindView(R.id.gv_pics)          FullGirdView mGvPics;
        @BindView(R.id.tv_time)          TextView     mTvTime;
        @BindView(R.id.tv_username)      TextView     mTvUsername;
        @BindView(R.id.tv_post_content)  TextView     mTvPostContent;
        @BindView(R.id.ll_container)     LinearLayout mLlContainer;
        @BindView(R.id.divider)          View         mDivider;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
