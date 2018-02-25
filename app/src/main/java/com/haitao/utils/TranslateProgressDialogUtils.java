package com.haitao.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.haitao.R;

public class TranslateProgressDialogUtils {
	
	private static ProgressDialog mDialog;

	public static void show(Context ctx, String text) {
		try{
			if(mDialog==null||!mDialog.isShowing()){
				mDialog = new ProgressDialog(ctx, R.style.translate_dialog_style);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.setCancelable(false);
				mDialog.setTitle(null);
			}
			mDialog.setMessage(text);
			if (!((Activity)ctx).isFinishing())
				mDialog.show();
		}catch(Exception ex){
		}
	}
	
	public static void show(Context ctx, int resId) {
		try {
			if(mDialog==null||!mDialog.isShowing()){
				mDialog = new ProgressDialog(ctx, R.style.translate_dialog_style);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.setCancelable(false);
				mDialog.setTitle(null);
			}

			mDialog.setMessage(ctx.getResources().getString(resId));
			if (!((Activity)ctx).isFinishing())
				mDialog.show();
		}catch(Exception ex){
		}

	}

	public static boolean isShowing() {
		try{
			if (null != mDialog && mDialog.isShowing()) {
				return true;
			}
		}catch(Exception ex){
		}

		return false;
	}

	public static void dismiss() {
		try{
			if (null != mDialog && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}catch(Exception ex){
		}
	}
}
