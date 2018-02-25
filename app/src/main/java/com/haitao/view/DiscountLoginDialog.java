package com.haitao.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class DiscountLoginDialog implements View.OnClickListener{

    private static Dialog dialog = null;
    private Context mContext;
    private TextView tvContent,tvUnLogin,tvLogin;
    private ImageView ivClose,ivQQ,ivSina,ivWx;


    public DiscountLoginDialog(Context mContext) {
        this.mContext = mContext;
        initView(null);
        initEvent();
    }


    private void initView(View v) {
        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View           view     = inflater.inflate(R.layout.dialog_login, null);// 得到加载view
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvUnLogin = (TextView) view.findViewById(R.id.tvUnLogin);
        tvLogin = (TextView) view.findViewById(R.id.tvLogin);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);
        ivQQ = (ImageView) view.findViewById(R.id.ivQQ);
        ivSina = (ImageView) view.findViewById(R.id.ivSina);
        ivWx = (ImageView) view.findViewById(R.id.ivWx);
        dialog.setContentView(view);
        dialog.setCancelable(true);
    }


    private void initEvent(){
        tvUnLogin.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        ivSina.setOnClickListener(this);
        ivWx.setOnClickListener(this);
    }

    public void show(String content){
        tvContent.setText(content);
        show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvUnLogin:
                if(null != mOnInnerClickLitener){
                    mOnInnerClickLitener.onUnLogin();
                }
                break;
            case R.id.tvLogin:
                if(null != mOnInnerClickLitener){
                    mOnInnerClickLitener.onLogin();
                }
                break;
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.ivQQ:
                if(null != mOnInnerClickLitener){
                    mOnInnerClickLitener.onThirdLogin(QQ.NAME);
                }
                break;
            case R.id.ivSina:
                if(null != mOnInnerClickLitener){
                    mOnInnerClickLitener.onThirdLogin(SinaWeibo.NAME);
                }
                break;
            case R.id.ivWx:
                if(null != mOnInnerClickLitener){
                    mOnInnerClickLitener.onThirdLogin(Wechat.NAME);
                }
                break;
        }
    }


    public interface OnInnerClickLitener {
        void onLogin();
        void onUnLogin();
        void onThirdLogin(String type);
    }

    private OnInnerClickLitener mOnInnerClickLitener;

    public void setOnItemClickLitener(OnInnerClickLitener mOnItemClickLitener) {
        this.mOnInnerClickLitener = mOnItemClickLitener;
    }



    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public boolean isShowing() {
        if (null == dialog)
            return false;
        return dialog.isShowing();
    }

    public void setCancel(boolean isCancel) {
        dialog.setCancelable(isCancel);
    }
}
