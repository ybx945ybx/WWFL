package com.haitao.utils;

import android.os.Handler;
import android.os.HandlerThread;

public class Exit {

	private boolean mIsExit = false;
	private Runnable mTask = new Runnable() {
		@Override
		public void run() {
			mIsExit = false;
		}
	};

	public void doExitInOneSecond() {
		mIsExit = true;
		HandlerThread thread = new HandlerThread("doTask");
		thread.start();
		new Handler(thread.getLooper()).postDelayed(mTask, 2000);
	}

	public boolean isExit() {
		return mIsExit;
	}

	public void setExit(boolean isExit) {
		this.mIsExit = isExit;
	}
}