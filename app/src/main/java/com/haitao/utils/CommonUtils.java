package com.haitao.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.haitao.BuildConfig;
import com.haitao.activity.BoardDetailActivity;
import com.haitao.activity.DiscountActivity;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.activity.ExchangeDetailActivity;
import com.haitao.activity.InviteFriendsActivity;
import com.haitao.activity.SampleDetailActivity;
import com.haitao.activity.StoreDetailActivity;
import com.haitao.activity.TagDetailActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.TransportDetailActivity;
import com.haitao.common.Constant.CategoryConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.model.PlatformObject;
import com.haitao.model.TagObject;
import com.mcxiaoke.packer.helper.PackerNg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    /**
     * 判断app是否处于前台
     */
    public static boolean isForeground() {
        return HtApplication.getAppCount() > 0;
    }

    /**
     * 把电话号码中间四位换成****
     *
     * @param phone
     * @return
     */
    public static String transferPhone(String phone) {
        String newPhone = String.format("%s****%s", phone.substring(0, 3), phone.substring(phone.length() - 4, phone.length()));
        return newPhone;
    }

    /**
     * 转换性别文本 "1" -> "男"
     *
     * @param gender UserObject中的性别
     * @return 转换后的性别
     */
    public static String getUserGender(String gender) {
        String result = "保密";
        if ("1".equals(gender)) {
            result = "男";
        } else if ("2".equals(gender)) {
            result = "女";
        }
        return result;
    }

    /**
     * 订单号的格式化
     *
     * @param order
     * @return
     */
    public static String transferOrderNum(String order) {
        String orderNum = "";
        switch (order.length()) {
            case 0:
                break;
            case 1:
                orderNum = order;
                break;
            case 2:
                orderNum = String.format("%s*", order.substring(0, 1));
                break;
            case 3:
                orderNum = String.format("%s*%s", order.substring(0, 1), order.substring(2, 3));
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                orderNum = String.format("%s**%s", order.substring(0, 1), order.substring(3, order.length()));
                break;
            case 8:
                orderNum = String.format("%s***%s", order.substring(0, 1), order.substring(4, order.length()));
                break;
            default:
                orderNum = String.format("%s****%s", order.substring(0, order.length() - 8), order.substring(order.length() - 4, order.length()));
                break;
        }
        return orderNum;
    }

    /**
     * 判断WebView里面的链接是否连接到站内详情
     *
     * @param url
     * @return
     */
    public static boolean isAppUrl(String url) {
        String  regApp = "#app_type=([a-zA-Z0-9]+)&app_val=([0-9]+)";
        Pattern patApp = Pattern.compile(regApp);
        Matcher matApp = patApp.matcher(url);
        return matApp.find();
    }

    /**
     * 判断活动里的链接点击是否需要登录
     *
     * @param url
     * @return
     */
    public static boolean isEventLogin(String url) {
        String  regApp = "is_login=1";
        Pattern patApp = Pattern.compile(regApp);
        Matcher matApp = patApp.matcher(url);
        return matApp.find();
    }

    /**
     * 判断是否为活动
     *
     * @param url
     * @return
     */
    public static boolean isEvent(String url) {
        String  regApp = "m=special";
        Pattern patApp = Pattern.compile(regApp);
        Matcher matApp = patApp.matcher(url);
        return matApp.find();
    }

    /**
     * 获取WebView链接里的类型和值
     *
     * @param url
     * @return
     */
    public static void parseUrlAndGo(Context mContext, String url) {

        String  regApp = "#app_type=([a-zA-Z0-9]+)&app_val=([0-9]+)";
        Pattern patApp = Pattern.compile(regApp);
        Matcher matApp = patApp.matcher(url);
        if (matApp.find()) {
            String rstType  = matApp.group(1);
            String rstValue = matApp.group(2);
            if (rstType.equals(TransConstant.WebType.DEAL)) {
                DiscountDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.STORE)) {
                StoreDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.POST)) {
                TopicDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.BOARD)) {
                BoardDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals("cate")) {
                TagObject tagObject = new TagObject();
                tagObject.type = CategoryConstant.TAG_CATEGORY;
                tagObject.id = rstValue;
                String  str = "&app_name=([^&]*)";
                Pattern pat = Pattern.compile(str);
                Matcher mat = pat.matcher(url);
                if (mat.find()) {
                    tagObject.name = mat.group(1);
                }

                DiscountActivity.launch(mContext, tagObject);
            } else if (rstType.equals(TransConstant.WebType.TAG)) {
                TagObject tagObject = new TagObject();
                tagObject.type = CategoryConstant.TAG_TAG;
                tagObject.id = rstValue;
                String  str = "&app_name=([^&]*)";
                Pattern pat = Pattern.compile(str);
                Matcher mat = pat.matcher(url);
                if (mat.find()) {
                    tagObject.name = mat.group(1);
                }
                TagDetailActivity.launch(mContext, tagObject.name, tagObject.id);
            } else if (rstType.equals(TransConstant.WebType.TRANSPORT)) {
                TransportDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.USER)) {
                TalentDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.EXCHANGE)) {
                ExchangeDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.TRIAL)) {
                SampleDetailActivity.launch(mContext, rstValue);
            } else if (rstType.equals(TransConstant.WebType.INVITE)) {
                InviteFriendsActivity.launch(mContext);
            } else if (rstType.equals(TransConstant.WebType.APP_STORE)) {
                try {
                    Uri    uri           = Uri.parse("market://details?id=" + mContext.getPackageName());
                    Intent commentIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(commentIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    /**
     * 获取下载安装包的大小
     *
     * @param updateURL 安装包地址
     * @return 安装包的大小
     */
    public static String getDownloadSize(String updateURL) {
        URL url = null;
        try {
            url = new URL(updateURL);
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setRequestProperty("Accept-Encoding", "identity");
            hc.connect();
            hc.setConnectTimeout(15000);
            int mTotalSize = hc.getContentLength();
            // 显示文件大小格式：2个小数点显示
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format((float) mTotalSize / 1024 / 1024) + "MB";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取返利里的数字
     *
     * @param str
     * @return
     */
    public static String getRebateValue(String str) {
        String  regApp = "([0-9\\.]+)";
        Pattern patApp = Pattern.compile(regApp);
        Matcher matApp = patApp.matcher(str);
        if (matApp.find()) {
            String value = matApp.group(1);
            return value;
        }
        return "";
    }

    /**
     * 获取渠道名
     *
     * @param context mContext
     * @return 渠道名
     */
    public static String getChannel(Context context) {
        String channel = null;
        /*try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            channel = ai.metaData.getString("UMENG_CHANNEL");
            if (channel == null) {
                channel = String.valueOf(ai.metaData.getInt("UMENG_CHANNEL"));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
        channel = PackerNg.getChannel(context);
        return channel;
    }

    /**
     * 是否强制更新
     *
     * @param platformObject 版本更新
     * @return 是否强制更新
     */
    public static boolean isForceUpdate(PlatformObject platformObject) {
        return BuildConfig.VERSION_CODE < Integer.parseInt(platformObject.low_ver_num);
    }

    /**
     * 是否强制更新
     *
     * @param lowVerNum 最低版本
     * @return 是否强制更新
     */
    public static boolean isForceUpdate(String lowVerNum) {
        return BuildConfig.VERSION_CODE < Integer.parseInt(lowVerNum);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}