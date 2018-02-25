package com.haitao.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.haitao.R;

/**
 * 降价提醒长按弹出底部编辑删除
 *
 * Created by a55 on 2017/12/28.
 */

public class DepreciateEditDialog extends BottomSheetDialog {

    private ItemClickListener mItemClickListener;

    public DepreciateEditDialog(@NonNull Context context) {
        super(context);
        initDlg(context);
    }

    private void initDlg(Context context) {
        View layout   = View.inflate(context, R.layout.dlg_depreciate_edit, null);
        View tvDelete = layout.findViewById(R.id.tv_delete);
        View tvEdit   = layout.findViewById(R.id.tv_edit);
        // 删除
        tvDelete.setOnClickListener(v -> {
            if (mItemClickListener != null)
                mItemClickListener.onDelete(DepreciateEditDialog.this);
        });
        // 编辑
        tvEdit.setOnClickListener(v -> {
            if (mItemClickListener != null)
                mItemClickListener.onEdit(DepreciateEditDialog.this);
        });
        setContentView(layout);
    }

    public DepreciateEditDialog setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onEdit(DepreciateEditDialog dialog);

        void onDelete(DepreciateEditDialog dialog);
    }
}
