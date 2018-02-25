package com.haitao.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.haitao.R;
import com.haitao.common.Constant.SPConstant;

public class ReturnTopUtils {
    private static Context mContext;
    /**
     * 相对顶部的位置
     */
    private static View locView;

    public static void showTips(Context context, View view){
        mContext = context;
        locView = view;
        if(0 == (int)SPUtils.get(mContext, SPConstant.RETURN_TOP,0)){
            myHandler.sendEmptyMessage(0);
            myHandler.sendEmptyMessageDelayed(1,5000);
        }
    }

    static Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    View view = View.inflate(mContext, R.layout.layout_return_tips,null);
                    PopWindowUtils.showAtLocation(mContext,locView,view);
                    break;
                case 1:
                    PopWindowUtils.dismiss();
                    if(null != mContext)
                        SPUtils.put(mContext,SPConstant.RETURN_TOP,1);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 提示消失
     */
    public static void dismiss(){
        myHandler.sendEmptyMessage(1);
    }


}
