package com.haitao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haitao.R;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.CommentModel;
import io.swagger.client.model.CommentReplyModel;

/**
 * 评论列表adapter
 * Created by a55 on 2017/12/11.
 */

public class NewCommentAdapter extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    private Context mContext;

    public NewCommentAdapter(Context mContext, List<CommentModel> data) {
        super(R.layout.comment_list_item, data);
        this.mContext = mContext;
    }

    public interface OnCallbackLitener {
        void onReplyClick(String commentId, String userName, String content, int groupPostion);

        void onAgreeClick(String commentId, int groupPostion);

        void onAgreeClick(String commentId, int groupPostion, int childPosition);
    }

    private OnCallbackLitener mOnCallbackLitener;

    public void setOnCallbackLitener(OnCallbackLitener onCallbackLitener) {
        mOnCallbackLitener = onCallbackLitener;
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentModel obj) {
        int dataGroupPosition = holder.getLayoutPosition() - 1;//  有一个评论总个数的头布局  因此减1
        // 分割线
        holder.setVisible(R.id.viewSeparate, dataGroupPosition == 0 ? false : true);
        // 头像
        ImageLoaderUtils.showOnlineGifImage(obj.getAvatar(), holder.getView(R.id.img_avatar));
        // 用户名  用户群组 发布时间 发布平台 点赞数  评论内容
        holder.setText(R.id.tv_name, obj.getAuthor())
                .setText(R.id.tv_group, obj.getAuthorGroupname())
                .setText(R.id.tv_time, obj.getPostdate())
                .setText(R.id.tv_source, obj.getSource())
                .setText(R.id.tv_comment_agree, "0".equals(obj.getPraiseCount()) ? "" : obj.getPraiseCount())
                .setText(R.id.tv_comment, obj.getComment());
        // 性别
        holder.getView(R.id.tv_group).setSelected("1".equals(obj.getAuthorSex()));
        // 是否点赞
        holder.getView(R.id.tv_comment_agree).setSelected("1".equals(obj.getIsPraised()));

        holder.getView(R.id.tv_comment_agree).setOnClickListener(v -> {
            if (null != mOnCallbackLitener && !TextUtils.equals(obj.getIsPraised(), "1")) {
                mOnCallbackLitener.onAgreeClick(obj.getCommentId(), dataGroupPosition);
            }
        });
        holder.getView(R.id.img_avatar).setOnClickListener(v -> TalentDetailActivity.launch(mContext, obj.getAuthorid()));
        holder.getView(R.id.tv_comment).setOnClickListener(v -> {
            if (mOnCallbackLitener != null) {
                mOnCallbackLitener.onReplyClick(obj.getCommentId(), obj.getAuthor(), obj.getComment(), dataGroupPosition);
            }
        });

        RecyclerView rycvReplys = holder.getView(R.id.rycv_reply);
        TextView     tvExpand   = holder.getView(R.id.tv_expand);
        if (obj.getReplyLists() != null && obj.getReplyLists().size() > 0) {
            rycvReplys.setVisibility(View.VISIBLE);

            // 个数超过五个则只显示五个，有展开查看更多按钮
            ArrayList<CommentReplyModel> replyList;
            if (obj.getReplyLists().size() > 4 ) {
                if (!obj.isExpand()) {
                    replyList = new ArrayList<>(obj.getReplyLists().subList(0, 4));
                    tvExpand.setVisibility(View.VISIBLE);
                    tvExpand.setText(String.format(mContext.getResources().getString(R.string.comment_expand_more), (obj.getReplyLists().size() - 4)));
                    tvExpand.setOnClickListener(v -> {
                        RVBaseAdapter<CommentReplyModel> mAdapter = (RVBaseAdapter) rycvReplys.getAdapter();
                        mAdapter.changeDatas(new ArrayList<>(obj.getReplyLists()));
                        mAdapter.notifyDataSetChanged();
                        tvExpand.setVisibility(View.GONE);
                        obj.setExpand(true);
                    });
                }else {
                    replyList = new ArrayList<>(obj.getReplyLists());
                    tvExpand.setVisibility(View.GONE);

                }
            } else {
                replyList = new ArrayList<>(obj.getReplyLists());
                tvExpand.setVisibility(View.GONE);

            }
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mContext);
            layoutManager.setScrollEnable(false);
            rycvReplys.setLayoutManager(layoutManager);
            RVBaseAdapter<CommentReplyModel> mReplyAdapter = new RVBaseAdapter<CommentReplyModel>(mContext,
                    replyList, R.layout.comment_list_reply_item) {
                @Override
                public void bindView(RVBaseHolder holder, CommentReplyModel commentReplyModel) {
                    holder.setVisibility(View.INVISIBLE, R.id.img_avatar);
                    holder.setText(R.id.tv_name, commentReplyModel.getAuthor());
                    holder.setText(R.id.tv_group, commentReplyModel.getAuthorGroupname());
                    holder.setText(R.id.tv_time, commentReplyModel.getPostdate());
                    holder.setText(R.id.tv_source, commentReplyModel.getSource());

                    holder.setText(R.id.tv_comment_agree, "0".equals(commentReplyModel.getPraiseCount()) ? "" : commentReplyModel.getPraiseCount());
                    holder.getView(R.id.tv_comment_agree).setSelected("1".equals(commentReplyModel.getIsPraised()));

                    // 是否是二级回复
                    if ("1".equals(commentReplyModel.getIsSubReply())) {

                        String comment = mContext.getResources().getString(R.string.reply) + "{ " + commentReplyModel.getReplyUsername() + " }: "
                                + commentReplyModel.getComment();
                        CharSequence formatted = ColorPhrase.from(comment)
                                .withSeparator("{}")
                                .innerColor(mContext.getResources().getColor(R.color.orangeFF804D))
                                .outerColor(mContext.getResources().getColor(R.color.grey29292C))
                                .format();

                        ((TextView) holder.getView(R.id.tv_comment)).setText(formatted);

                    } else {
                        holder.setText(R.id.tv_comment, commentReplyModel.getComment());
                    }

                    holder.getView(R.id.tv_name).setOnClickListener(v -> TalentDetailActivity.launch(mContext, commentReplyModel.getAuthorid()));

                    holder.getView(R.id.tv_comment_agree).setOnClickListener(v -> {
                        if (null != mOnCallbackLitener && !TextUtils.equals(commentReplyModel.getIsPraised(), "1")) {
                            mOnCallbackLitener.onAgreeClick(commentReplyModel.getCommentId(), dataGroupPosition, holder.getLayoutPosition());
                        }
                    });

                    holder.getView(R.id.tv_comment).setOnClickListener(v -> {
                        if (mOnCallbackLitener != null) {
                            mOnCallbackLitener.onReplyClick(commentReplyModel.getCommentId(), commentReplyModel.getAuthor(), commentReplyModel.getComment(),dataGroupPosition);
                        }
                    });
                }
            };
            rycvReplys.setAdapter(mReplyAdapter);
        } else {
            rycvReplys.setVisibility(View.GONE);
            tvExpand.setVisibility(View.GONE);
        }
    }
}
