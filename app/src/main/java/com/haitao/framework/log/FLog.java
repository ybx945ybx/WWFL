package com.haitao.framework.log;

import android.os.Build;
import android.util.Log;

import com.haitao.common.Constant.Constant;


public class FLog {

	private static FLogLevel logLevel = FLogLevel.V;

	protected static FileLogger flogger;

	public static void setFileLogger(FileLogger logger) {
		flogger = logger;
	}

	public static void setLogLevel(FLogLevel level) {
		logLevel = level;
	}
	
	protected static boolean checkLevel(FLogLevel level) {
		return logLevel.allows(level);
	}

	public static void v(String tag, String message) {
		if (checkLevel(FLogLevel.V) && isDebugEnable()) {
			
			Log.v(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().v(tag, FLogComFunc.getFileLineMethod() + ":" + message);
			} 

		}
	}

	public static void v(String tag, String message, Throwable tr) {
		if (checkLevel(FLogLevel.V) && isDebugEnable()) {			
			Log.v(tag, FLogComFunc.getFileLineMethod() + ":"  + message, tr);
			if (!isEmulator()) {
				FileLogger.getInstance().v(tag, FLogComFunc.getFileLineMethod() + ":"  + message, tr);
			} 
		}
	}

	public static void d(String tag, String message) {
		if (checkLevel(FLogLevel.D) && isDebugEnable()) {
			
			Log.d(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().d(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			}
			
		}
	}

	public static void d(String tag, String message, Throwable tr) {
		if (checkLevel(FLogLevel.D) && isDebugEnable()) {	
			
			Log.d(tag, FLogComFunc.getFileLineMethod() + ":"  + message,tr);
			if (!isEmulator()) {
				FileLogger.getInstance().d(tag, FLogComFunc.getFileLineMethod() + ":"  + message, tr);
			}
		}
	}

	public static void i(String tag, String message) {
		if (checkLevel(FLogLevel.I) && isDebugEnable()) {
			
			Log.i(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().i(tag, FLogComFunc.getFileLineMethod() + ":" + message);
			}
			
		}
	}

	public static void i(String tag, String message, Throwable tr) {
		if (checkLevel(FLogLevel.I) && isDebugEnable()) {
			
			Log.i(tag, FLogComFunc.getFileLineMethod() + ":"  + message,tr);
			if (!isEmulator()) {
				FileLogger.getInstance().i(tag, FLogComFunc.getFileLineMethod() + ":" + message, tr);
			}
			
		}
	}

	public static void w(String tag, String message) {
		if (checkLevel(FLogLevel.W) && isDebugEnable()) { 
			
			Log.w(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().w(tag, FLogComFunc.getFileLineMethod() + ":" + message);
			}
			
		}
	}

	public static void w(String tag, String message, Throwable tr) {
		if (checkLevel(FLogLevel.W) && isDebugEnable()) { 
			
			Log.w(tag, FLogComFunc.getFileLineMethod() + ":"  + message,tr);
			if (!isEmulator()) {
				FileLogger.getInstance().w(tag, FLogComFunc.getFileLineMethod() + ":" + message, tr);
			}
			
		}
	}

	public static void e(String tag, String message) {
		if (checkLevel(FLogLevel.E) && isDebugEnable()) {
			
			Log.e(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().e(tag, FLogComFunc.getFileLineMethod() + ":" + message);
			}
		}
	}
	public static void e(String tag, Object message) {
		if (checkLevel(FLogLevel.E) && isDebugEnable()) {
			
			Log.e(tag, FLogComFunc.getFileLineMethod() + ":"  + message);
			if (!isEmulator()) {
				FileLogger.getInstance().e(tag, FLogComFunc.getFileLineMethod() + ":" + message);
			}
		}
	}

	public static void e(String tag, String message, Throwable tr) {
		if (checkLevel(FLogLevel.E) && isDebugEnable()) {
			
			Log.e(tag, FLogComFunc.getFileLineMethod() + ":"  + message,tr);
			if (!isEmulator()) {
				FileLogger.getInstance().e(tag, FLogComFunc.getFileLineMethod() + ":" + message, tr);
			}
		}
	}

	public static boolean isDebugEnable() {
//		return flogger!=null && BuildConfig.DEBUG;
		return Constant.IS_DEBUG;
	}
	
	public static boolean isEmulator() {
        return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
  }
}
