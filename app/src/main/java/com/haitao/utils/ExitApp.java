package com.haitao.utils;

import android.app.Activity;

import java.util.ArrayList;

public class ExitApp  {
	
	private ArrayList<Activity> mActivityList = new ArrayList<Activity>();
	private static ExitApp mInstance;

	public static ExitApp getInstance() {
		if (mInstance == null) {
			mInstance = new ExitApp();
		}
		return mInstance;

	}

	public void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public void exit() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
		
	public void finishAll() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
	}

	public Activity getLatestActivity(){
		return mActivityList.get(mActivityList.size()-1);
	}
	
	public void clearActivityList() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
		mActivityList.clear();
	}
	
	public ArrayList<Activity> getActivityList() {
		return mActivityList;
	}
}
