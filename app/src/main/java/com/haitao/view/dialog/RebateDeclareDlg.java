package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;

/**
 * 去购买页面返利说明弹窗
 * Created by a55 on 2018/1/17.
 */

public class RebateDeclareDlg extends BottomSheetDialog {

    private Context                mContext;
    private String                rebateDeclare;

    public RebateDeclareDlg(@NonNull Context context, String rebateDeclare) {
        super(context);
        mContext = context;
        this.rebateDeclare = rebateDeclare;
        initDlg();

    }

    private void initDlg() {
        View      parentView = View.inflate(mContext, R.layout.dialog_rebate_declare, null);
        ImageView    ivClose        = parentView.findViewById(R.id.iv_close);
        TextView  tvDeclare    = parentView.findViewById(R.id.tv_declare);
        TextView  tvEmpty    = parentView.findViewById(R.id.tv_empty);

        // 返利说明
        if (TextUtils.isEmpty(rebateDeclare)){
            tvDeclare.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            tvDeclare.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            rebateDeclare = rebateDeclare.replaceAll("\\*", "• ");
            tvDeclare.setText(rebateDeclare);
        }
        // 关闭按钮
        ivClose.setOnClickListener(v -> dismiss());

        setContentView(parentView);
    }
}
