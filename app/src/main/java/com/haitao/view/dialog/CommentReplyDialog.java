package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;

/**
 * 点击评论 回复/复制 弹出dialog
 *
 * @author 陶声
 * @since 2017-11-02
 */

public class CommentReplyDialog extends BottomSheetDialog {
    private CommentClickListener mCommentClickListener;

    public CommentReplyDialog(@NonNull Context context, boolean showComment) {
        super(context);
        initDlg(context, showComment);
    }

    private void initDlg(final Context context, boolean showComment) {
        View layout        = View.inflate(context, R.layout.dlg_comment_reply, null);
        View tvReply       = layout.findViewById(R.id.tv_reply);
        View tvCopyContent = layout.findViewById(R.id.tv_copy_content);
        // 不显示评论
        if (!showComment) {
            tvReply.setVisibility(View.GONE);
        } else {
            // 回复评论
            tvReply.setOnClickListener(v -> {
                if (mCommentClickListener != null)
                    mCommentClickListener.onReply(CommentReplyDialog.this);
            });
        }

        // 复制内容
        tvCopyContent.setOnClickListener(v -> {
            if (mCommentClickListener != null)
                mCommentClickListener.onCopyContent(CommentReplyDialog.this);
        });
        setContentView(layout);
    }

    public CommentReplyDialog setCommentClickListener(CommentClickListener commentClickListener) {
        mCommentClickListener = commentClickListener;
        return this;
    }


    public interface CommentClickListener {
        void onReply(CommentReplyDialog dialog);

        void onCopyContent(CommentReplyDialog dialog);
    }
}
