package com.haitao.framework.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.haitao.common.Constant.Constant;
import com.haitao.framework.utils.DeviceUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.magicwindow.Session;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private static final String LOG_PATH_MEMORY_DIR = Environment.getDataDirectory().getAbsolutePath() + "/data/com.haitao/log/crash/";
    private static final String LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.LOG_PATH + "crash/";

    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public String getDir() {
        return DeviceUtil.hasSdcard() ? LOG_PATH_SDCARD_DIR : LOG_PATH_MEMORY_DIR;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        Log.e(TAG, "error : ", ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);

        } else {
//            Logger.e("exception,app will exit");
            //			ActivityManager.getActivityManager().finishAllActivity();
            //退出程序
            // 保证魔窗Session正确
            Session.onKillProcess();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        int random = (int) (Math.random() * 10);//随机用于区分错误
        if (ex.getMessage() != null) {

            Logger.e("错误:" + random, ex.getMessage());
        }
        for (StackTraceElement stack : ex.getStackTrace()) {
//            Logger.e("错误:" + random, stack.toString());

        }

        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo    pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
//            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                //				Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
//                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key   = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer      writer      = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        //		if ((Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"))) {
        Logger.e("Crash", FLogComFunc.getFileLineMethod() + ":" + sb.toString());
        //			return null;
        //		}

        try {
            long   timestamp = System.currentTimeMillis();
            String time      = formatter.format(new Date());
            String fileName  = "crash-" + time + "-" + timestamp + ".txt";
            String path;
            path = DeviceUtil.hasSdcard() ? LOG_PATH_SDCARD_DIR : LOG_PATH_MEMORY_DIR;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);

            fos.write(sb.toString().getBytes());
            fos.close();

            return fileName;
        } catch (Exception e) {
//            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }


    /**
     * @param _context
     * @param filePath
     */
    @SuppressWarnings("unused")
    private void sendErrorMail(Context _context, String filePath) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject    = "Error Description";
        String body = "Sorry for your inconvenience .\nWe assure you that we will solve this problem as soon possible."
                + "\n\nThanks for using app.";

        sendIntent.setType("plain/text");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@gmail.com"});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        sendIntent.setType("message/rfc822");
        _context.startActivity(Intent.createChooser(sendIntent, "App_name"));
    }

}
