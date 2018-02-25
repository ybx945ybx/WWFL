package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;

/**
 * 首页 - 签到底部弹框
 *
 * @author 陶声
 * @since 2017-06-09
 */

public class SignBottomSheetDlg extends BottomSheetDialog {
    private SignListener mSignListener;

    public SignBottomSheetDlg(@NonNull Context context) {
        super(context);
        initDlg(context);
    }

    private void initDlg(final Context context) {
        View        layout  = View.inflate(context, R.layout.layout_sign, null);
        ImageButton ibClose = layout.findViewById(R.id.ib_close); // 关闭
        //        TextView    tvCheckCoin   = layout.findViewById(R.id.tv_check_coin); // 检查金币
        TextView tvSignGetCoin = layout.findViewById(R.id.tv_sign_get_coin); // 签到并获取金币
        // 关闭
        ibClose.setOnClickListener(v -> dismiss());
        // 检查金币
        /*tvCheckCoin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCheckCoin.setOnClickListener(v -> {
            if (mSignListener != null)
                mSignListener.checkGold(SignBottomSheetDlg.this);
        });*/
        // 签到并获取金币
        tvSignGetCoin.setOnClickListener(v -> {
            if (mSignListener != null)
                mSignListener.sign(SignBottomSheetDlg.this);
        });
        setContentView(layout);
    }

    public SignBottomSheetDlg setSignListener(SignListener signListener) {
        mSignListener = signListener;
        return this;
    }

    /*public SignBottomSheetDlg setDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        return this;
    }*/

    public interface SignListener {
        void sign(SignBottomSheetDlg dialog);

        void checkGold(SignBottomSheetDlg dialog);
    }

}
