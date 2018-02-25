package com.haitao.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;

/**
 * 跟网络相关的工具类
 */
public class NetUtils {
	
	private NetUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity) {

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				
				if (info.getState() == NetworkInfo.State.CONNECTED) { // 判断网络是否已经连接
					
					switch (info.getType()) { // 判断网络类型
					
					case ConnectivityManager.TYPE_WIFI:
						Constant.NET_TYPE = "wifi";
						break;
					case ConnectivityManager.TYPE_MOBILE:
						
						switch (info.getSubtype()) {
						case TelephonyManager.NETWORK_TYPE_GPRS:
							Constant.NET_TYPE = "2G";
							break;
						case TelephonyManager.NETWORK_TYPE_CDMA:
							Constant.NET_TYPE = "2G";
							break;
						case TelephonyManager.NETWORK_TYPE_EDGE:
							Constant.NET_TYPE = "2G";
							break;
						case TelephonyManager.NETWORK_TYPE_1xRTT:
							Constant.NET_TYPE = "2G";
							break;
						case TelephonyManager.NETWORK_TYPE_IDEN:
							Constant.NET_TYPE = "2G";
							break;
						case TelephonyManager.NETWORK_TYPE_UMTS:
							Constant.NET_TYPE = "2G";
							break;
						
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_EHRPD:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_B:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_HSDPA:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_HSPA:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_HSPAP:
							Constant.NET_TYPE = "3G";
							break;
						case TelephonyManager.NETWORK_TYPE_HSUPA:
							Constant.NET_TYPE = "3G";
							break;
							
						case TelephonyManager.NETWORK_TYPE_LTE:
							Constant.NET_TYPE = "4G";
							break;
						case TelephonyManager.NETWORK_TYPE_UNKNOWN:
							Constant.NET_TYPE = "3G";
							break;
						default:
							Constant.NET_TYPE = "3G";
							break;
						}
					default:
						break;
					}
					TransConstant.NET_TYPE = Constant.NET_TYPE;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null == cm || null == cm.getActiveNetworkInfo())
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

}
