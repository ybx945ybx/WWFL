package com.haitao.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * DownloadManager更新Apk
 *
 * @author 陶声
 * @since 2017-06-08
 */

public class UpdateApkReceiver extends BroadcastReceiver {
    private String mFileName;

    public UpdateApkReceiver(String fileName) {
        mFileName = fileName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        File file = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + mFileName);
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) && file.exists() && file.length() > 0) {
            install(context, file.getAbsolutePath());
        }
    }

    private boolean install(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(filePath);
        if (file.length() > 0 && file.exists() && file.isFile()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public String getFileName() {
        return mFileName;
    }
}
