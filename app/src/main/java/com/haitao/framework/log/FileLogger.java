package com.haitao.framework.log;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.haitao.common.Constant.Constant;
import com.haitao.framework.utils.DeviceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FileLogger {

    private volatile static FileLogger fileLogger;

    private static final String LOG_PATH_MEMORY_DIR = Environment.getDataDirectory().getAbsolutePath() + "/data/com.haitao/log/";
    private static final String LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.LOG_PATH;

    private static final String LOG_NAME = "log.txt";
    private static final String TAG      = "FileLogger";

    private static final int MSG_WRITE = 0; // paired with a LogMessage
    private static final int MSG_OPEN  = 1;


    private final File logFolder;
    private final File logFile;

    private long maxFileSize   = 1024 * 1024;    //1M
    private long maxFileNumber = 5;

    private OutputStreamWriter writer;
    private String             mTag;
    private String             applicationTag;
    private Handler            mSaveStoreHandler;

    private FLogLevel logLevel = FLogLevel.I;

    public static FileLogger getInstance() {
        if (null == fileLogger) {
            synchronized (FileLogger.class) {
                if (null == fileLogger) {
                    try {
                        boolean useSdCard = DeviceUtil.hasSdcard() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
                        File    file      = new File(useSdCard ? LOG_PATH_SDCARD_DIR : LOG_PATH_MEMORY_DIR);

                        if (!file.exists()) {
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdir();
                        }

                        fileLogger = new FileLogger(file);
                    } catch (IOException e) {
                        // failed to create the FileLogger
                        Log.e(TAG, "failed to create the FileLogger");
                        //						Toast.makeText(this, getString(R.string.error_creating_logger, getFilesDir().toString()), Toast.LENGTH_LONG).show();
                        //						finish();
                        System.exit(1);
                    }
                }
            }
        }

        return fileLogger;
    }

    public FileLogger(File logFolder, String tag) throws IOException {
        this(logFolder);
        this.mTag = tag;
    }

    @SuppressLint("HandlerLeak")
    public FileLogger(File logFolder) throws IOException {
        this.logFolder = logFolder;
        if (!logFolder.exists()) logFolder.mkdirs();

        if (!logFolder.isDirectory()) {
            Log.e(TAG, logFolder + " is not a folder");
            throw new IOException("Path is not a directory");
        }

        if (!logFolder.canWrite()) {
            Log.e(TAG, logFolder + " is not a writable");
            throw new IOException("Folder is not writable");
        }

        this.logFile = new File(logFolder, LOG_NAME);
        // Initializing the HandlerThread
        HandlerThread handlerThread = new HandlerThread("FileLogger", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        if (!handlerThread.isAlive()) {
            handlerThread.start();
            mSaveStoreHandler = new Handler(handlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_OPEN:
                            try {
                                closeWriter();
                            } catch (IOException e) {
                            }
                            openWriter();
                            break;

                        case MSG_WRITE:
                            try {
                                LogMessage logmsg = (LogMessage) msg.obj;
                                if (writer == null)
                                    Log.e(TAG, "no writer");
                                else {
                                    writer.append(logmsg.formatLog());
                                    writer.flush();
                                }
                            } catch (IOException e) {
                                Log.e(TAG, e.getClass().getSimpleName() + " : " + e.getMessage());
                            }

                            verifyFileSize();
                            break;

                    }
                }
            };
            if (mSaveStoreHandler == null)
                throw new NullPointerException("Handler is null");

            mSaveStoreHandler.sendEmptyMessage(MSG_OPEN);
        }
    }

    public void setLogLevel(FLogLevel level) {
        logLevel = level;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void d(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.D))
            write('d', tag, msg, tr);
    }

    public void d(String tag, String msg) {
        if (logLevel.allows(FLogLevel.D))
            write('d', tag, msg);
    }

    public void d(String msg) {
        if (logLevel.allows(FLogLevel.D))
            write('d', msg);
    }

    public void e(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.E))
            write('e', tag, msg, tr);
    }

    public void e(String tag, String msg) {
        if (logLevel.allows(FLogLevel.E))
            write('e', tag, msg);
    }

    public void e(String msg) {
        if (logLevel.allows(FLogLevel.E))
            write('e', msg);
    }

    public void wtf(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.WTF))
            write('f', tag, msg, tr);
    }

    public void wtf(String tag, String msg) {
        if (logLevel.allows(FLogLevel.WTF))
            write('f', tag, msg);
    }

    public void wtf(String msg) {
        if (logLevel.allows(FLogLevel.WTF))
            write('f', msg);
    }

    public void i(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.I))
            write('i', tag, msg, tr);
    }

    public void i(String tag, String msg) {
        if (logLevel.allows(FLogLevel.I))
            write('i', tag, msg);
    }

    public void i(String msg) {
        if (logLevel.allows(FLogLevel.I))
            write('i', msg);
    }

    public void v(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.V))
            write('v', tag, msg, tr);
    }

    public void v(String tag, String msg) {
        if (logLevel.allows(FLogLevel.V))
            write('v', tag, msg);
    }

    public void v(String msg) {
        if (logLevel.allows(FLogLevel.V))
            write('v', msg);
    }

    public void w(String tag, String msg, Throwable tr) {
        if (logLevel.allows(FLogLevel.W))
            write('w', tag, msg, tr);
    }

    public void w(String tag, String msg) {
        if (logLevel.allows(FLogLevel.W))
            write('w', tag, msg);
    }

    public void w(String msg) {
        if (logLevel.allows(FLogLevel.W))
            write('w', msg);
    }

    private void write(char lvl, String message) {
        String tag;
        if (mTag == null)
            tag = TAG;
        else
            tag = mTag;
        write(lvl, tag, message);
    }

    private void write(char lvl, String tag, String message) {
        write(lvl, tag, message, null);
    }

    protected void write(char lvl, String tag, String message, Throwable tr) {
        if (tag == null) {
            write(lvl, message);
            return;
        }

        Message htmsg = Message.obtain(mSaveStoreHandler, 0, new LogMessage(lvl, tag, getApplicationLocalTag(), Thread.currentThread().getName(), message, tr));

        mSaveStoreHandler.sendMessage(htmsg);
    }

    private static class LogMessage {
        private static SimpleDateFormat dateFormat; // must always be used in the same thread

        private final long      now;
        private final char      level;
        private final String    tag;
        private final String    appTag;
        private final String    threadName;
        private final String    msg;
        private final Throwable cause;
        private       String    date;

        LogMessage(char lvl, String tag, String appTag, String threadName, String msg, Throwable tr) {
            this.now = System.currentTimeMillis();
            this.level = lvl;
            this.tag = tag;
            this.appTag = appTag;
            this.threadName = threadName;
            this.msg = msg;
            this.cause = tr;

            if (msg == null) {
                Log.e(TAG, "No message");
            }
        }

        private void addLogHeader(final StringBuilder log) {
            if (dateFormat == null)
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            if (date == null && dateFormat != null)
                date = dateFormat.format(new Date(now));

            log.append(date.toString());
            log.append("[" + android.os.Process.myPid() + "|");//process ID
            if (threadName != null)
                log.append(threadName);
            log.append(']');
            log.append("[" + level + "]");
            log.append('[');
            if (appTag != null)
                log.append(appTag);
            log.append('|');
            if (tag != null)
                log.append(tag);
            log.append(']');
        }

        private void addException(final StringBuilder log, Throwable tr) {
            if (tr == null)
                return;
            final StringBuilder sb = new StringBuilder();
            sb.append(cause.getClass());
            sb.append(": ");
            sb.append(cause.getMessage());
            sb.append('\n');

            for (StackTraceElement trace : cause.getStackTrace()) {
                //addLogHeader(log);
                sb.append(" at ");
                sb.append(trace.getClassName());
                sb.append('|');
                sb.append(trace.getMethodName());
                sb.append('(');
                sb.append(trace.getFileName());
                sb.append(':');
                sb.append(trace.getLineNumber());
                sb.append(')');
                sb.append('\n');
            }

            addException(sb, tr.getCause());
            log.append(sb.toString().replace(';', '-').replace(',', '-').replace('"', '\''));
        }

        public CharSequence formatLog() {
            final StringBuilder log = new StringBuilder();
            addLogHeader(log);
            //			log.append(':');
            if (msg != null) log.append(msg.replace(';', '-').replace(',', '-').replace('"', '\''));
            //			log.append('"');
            log.append('\n');
            if (cause != null) {
                //				addLogHeader(log);
                //				log.append('"');
                addException(log, cause);
                //				log.append('"');
                log.append('\n');
            }
            return log.toString();
        }
    }

    private String getApplicationLocalTag() {
        if (applicationTag == null) applicationTag = getApplicationTag();
        return applicationTag;
    }

    @SuppressWarnings("unused")
    private void mergeFile(File otherFile, File finalFile) {
        try {
            InputStream instream = new FileInputStream(otherFile);

            BufferedReader     in  = new BufferedReader(new InputStreamReader(instream));
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(finalFile, true), "UTF-8");

            String line;

            while ((line = in.readLine()) != null) {
                out.append(line);
                out.append('\n');
            }

            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            FLog.e(TAG, "FileNotFoundException: " + e.getMessage(), e);
        } catch (IOException e) {
            FLog.e(TAG, "IOException: " + e.getMessage(), e);
        }

    }

    public String getApplicationTag() {
        return "Haitao";
    }

    private void verifyFileSize() {
        if (logFile != null) {
            long size = logFile.length();
            if (size > maxFileSize) {
                try {
                    closeWriter();
                    rollLogFileNow();
                } catch (IOException e) {
                    FLog.e(TAG, "Can't use file : " + logFile, e);
                } finally {
                    openWriter();
                }
            }
        }
    }

    private void openWriter() {
        if (writer == null)
            try {
                writer = new OutputStreamWriter(new FileOutputStream(logFile, true), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "can't get a writer for " + logFile + " : " + e.getMessage());
            } catch (FileNotFoundException e) {
                Log.e(TAG, "can't get a writer for " + logFile + " : " + e.getMessage());
            }
    }

    private void closeWriter() throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    private void rollLogFileNow() {
        File[] files = logFolder.listFiles();
        Log.i(TAG, "verifyFileSize:" + files.length);
        for (int index = files.length; index > 0; index--) {
            File file = files[index - 1];

            if (file.isFile()) {
                if (index == maxFileNumber) {
                    Log.e(TAG, "delete file:" + file.toString());
                    file.delete();
                } else {
                    String newPath = logFile.getParent() + "/log_" + index + ".txt";
                    Log.e(TAG, "rename file:" + file.toString() + "==>" + newPath);
                    file.renameTo(new File(newPath));
                }
            }
        }
    }
}
