package com.haitao.connection.api;

import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.framework.utils.DeviceUtil;
import com.haitao.framework.utils.Md5Util;

import io.swagger.client.api.DefaultApi;


/**
 * Created by apple on 17/3/13.
 */

public class ForumApi extends DefaultApi {
    private static ForumApi forumApi;

    public static ForumApi getInstance() {
        if (null == forumApi) {
            forumApi = new ForumApi();
        }
        String time = String.valueOf(System.currentTimeMillis());
        //        forumApi.addHeader("n", DeviceUtil.getImei(HtApplication.getInstance()));
        forumApi.addHeader("n", DeviceUtil.getDeviceId(HtApplication.getInstance()));
        forumApi.addHeader("p", TransConstant.PLATFORM);
        forumApi.addHeader("v", TransConstant.SWAGGER_API_VERSION);
        forumApi.addHeader("w", TransConstant.WIDTH);
        forumApi.addHeader("h", TransConstant.HEIGHT);
        forumApi.addHeader("model", TransConstant.DEVICE);
        forumApi.addHeader("client_v", DeviceUtil.getSoftWareVersion(HtApplication.getInstance()));
        forumApi.addHeader("os", DeviceUtil.getHardWareVersion());
        if (HtApplication.isLogin()) {
            forumApi.addHeader("token", UserManager.getInstance().getHtToken());
        } else {
            forumApi.addHeader("token", "");
        }
        forumApi.addHeader("t", time);
        String token = String.format(TransConstant.KEY, time);
        forumApi.addHeader("api_token",
                Md5Util.getMd5Value(token));
        forumApi.addHeader("net_type", TransConstant.NET_TYPE);
        return forumApi;
    }

    public void setBaseUrl(String url) {
        setBasePath(url);
    }


}
