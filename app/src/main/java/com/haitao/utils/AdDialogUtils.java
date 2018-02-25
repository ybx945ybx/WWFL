package com.haitao.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.haitao.R;
import com.haitao.view.CustomImageView;

public class AdDialogUtils {

    Context mContext;
    private static Dialog dialog = null;
    private CustomImageView ivImage;
    private ImageButton btnClose;

    public AdDialogUtils(Context mContext) {
        this.mContext = mContext;
        initView(null);
    }

    public AdDialogUtils(Context mContext, View v) {
        this.mContext = mContext;
        initView(v);
    }

    private void initView(View v) {
        dialog = new Dialog(mContext, R.style.ad_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_ad_dialog, null);// 得到加载view
        ivImage = (CustomImageView) view.findViewById(R.id.ivImage);
        int screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        int width = screenWidth * 4 / 5;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
        lp.width = width;
        lp.height = width / 4 * 5;
        ivImage.setLayoutParams(lp);
        btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v1 -> dismiss());
        ivImage.setOnClickListener(v12 -> {
            if(null != mOnItemClickLitener){
                mOnItemClickLitener.onConfirmClick();
            }
            dismiss();
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
    }


    public interface OnItemClickLitener {
        void onConfirmClick();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public void show(String imgUrl) {
        ImageLoaderUtils.showLocationImage(imgUrl,ivImage);
        dialog.show();
    }

    /**
     * 广告弹窗是否显示
     * @return
     */
    public boolean isShowing(){
        return null != dialog && dialog.isShowing();
    }

    public void dismiss() {
        if(null != dialog && dialog.isShowing())
            dialog.dismiss();
    }
}
