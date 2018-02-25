package com.haitao.framework.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class DeviceUtil {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getHardWareMode() {
        return Build.MODEL;
    }

    /**
     * 获取android版本号
     *
     * @return
     */
    public static String getHardWareVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取应用的版本名称
     *
     * @param context
     * @return
     */
    public static String getSoftWareVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            Log.v("-----", context.getPackageName());
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null != packInfo ? packInfo.versionName : "";
    }

    /**
     * 获取系统SDK版本
     *
     * @return 系统版本号
     */
    public static int getSDKVersionNumber() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机的EMEI
     *
     * @param context mContext
     * @return IMEI号
     */
    public static String getImei(Context context) {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return imei = null != tm.getDeviceId() ? tm.getDeviceId() : "";
    }

    /**
     * 获取设备Id
     *
     * @param context mContext
     * @return 设备Id
     */
    public static String getDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(deviceId) ? "" : deviceId;
    }

    /**
     * 是否有sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

}
