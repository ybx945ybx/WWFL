package com.haitao.utils;

import android.content.Context;
import android.widget.Toast;

import com.haitao.R;

import cn.magicwindow.Session;


public class PressExit {

    private static Exit mExit = new Exit();

    public static void pressAgainExit(Context context) {
        if (mExit.isExit()) {
            if (ExitApp.getInstance().getActivityList() != null) {
                if (ExitApp.getInstance().getActivityList().size() != 0) {
                    ExitApp.getInstance().exit();
                } else {
                    // 保证魔窗Session正确
                    Session.onKillProcess();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            } else {
                // 保证魔窗Session正确
                Session.onKillProcess();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } else {
            Toast.makeText(context, context.getString(R.string.app_back_tips), Toast.LENGTH_SHORT).show();
            mExit.doExitInOneSecond();
        }
    }

}