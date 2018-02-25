package com.haitao.framework.utils;


import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.IConverter;
import com.haitao.framework.codec.result.OperationResult;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.http.okhttp.callback.ResultCallback;
import com.haitao.framework.http.okhttp.request.OkHttpRequest;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求网络工具类
 */
public class HTTPUtil {
    public static HashMap<String, String> headers = new HashMap<String, String>();

    public static void postAsyn(final String url, String method, String[] parameters, ResultCallback resultCallback) {
        Map<String, String> params = createRequestParams(parameters, method);
        Logger.t("HTTPUTIl").d(url);
        new OkHttpRequest.Builder().url(url).params(params).post(resultCallback);
    }

    /**
     * post请求一般的数据
     *
     * @param url
     * @param method
     * @param parameters
     * @param decoder
     * @param handler
     * @param <T>
     */
    public static <T> void postAsyn(final String url, String method, String[] parameters, final IConverter<String, T> decoder, final IAsynServiceHandler<T> handler) {
        Map<String, String> params = createRequestParams(parameters, method);
        Logger.t("HTTPUTIl").d(url);
        Logger.t("HTTPUTIl").d(params.toString());
        new OkHttpRequest.Builder().url(url).params(params).post(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                handler.onFailed(TransConstant.NET_ERROR);
            }

            @Override
            public void onResponse(String response) {
                Logger.d("HTTPUTIL--response -- " + response);
                parse(response, decoder, handler, null);
            }
        });
    }

    public static <T> void postAsyn(String url, String method, T entity, IConverter<T, String[]> convertor, final IConverter<String, T> decoder, final IAsynServiceHandler<T> handler) {
        try {
            postAsyn(url, method, convertor.converter(entity), decoder, handler);
        } catch (Exception e) {
            handler.onFailed(e.toString());
        }
    }

    public static void getAsyn(String url, HashMap<String, String> params) {
        if (headers.isEmpty()) {
            headers.put("User-Agent", "Swagger-Codegen/1.0.0/android");
            headers.put("Accept", "application/json");
        }
        new OkHttpRequest.Builder().headers(headers).url(url).params(params).get(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.e(e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Logger.d("HTTPUTIL--response" + response);
            }
        });
    }

    public static <T> void postMultypart(String url, String method, T entity, Pair<String, File>[] files, IConverter<T, String[]> convertor, final IConverter<String, T> decoder, final IAsynServiceHandler<T> handler) {
        try {
            postMultypart(url, method, convertor.converter(entity), files, decoder, handler);
        } catch (Exception e) {
            handler.onFailed(e.getMessage());
        }
    }


    /**
     * post 上传带文件的数据
     *
     * @param url
     * @param parameters
     * @param method
     * @param files
     * @param decoder
     * @param handler
     * @param <T>
     */
    public static <T> void postMultypart(final String url, String method, String[] parameters, Pair<String, File>[] files, final IConverter<String, T> decoder, final IAsynServiceHandler<T> handler) {
        Map<String, String> params = createRequestParams(parameters, method);
        Logger.t("HTTPUTIL---Multy--r").d(params.toString());

        new OkHttpRequest.Builder().url(url).params(params).files(files).upload(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                handler.onFailed(TransConstant.NET_ERROR);
            }

            @Override
            public void onResponse(String response) {
                Logger.t("HTTPUTIL---Multy--response").d(response);
                parse(response, decoder, handler, null);
            }
        });

    }

    /**
     * 分页加载
     *
     * @param url
     * @param method
     * @param parameters
     * @param decoder
     * @param handler
     * @param page
     * @param <T>
     */
    public static <T> void postQuery(String url, String method, String[] parameters, final IConverter<String, PageResult<T>> decoder, final IAsynServiceHandler<T> handler, final PageResult page) {
        Map<String, String> params = createRequestParams(parameters, method);
        params.put(PageConstant.CURRENT_PAGE, String.valueOf(page.page));
        params.put(PageConstant.PAGE_SIZE, String.valueOf(page.pagesize));
        Logger.t("HTTPUTIL---Page").d(url);
        Logger.t("HTTPUTIL---Page").d(params.toString());
        new OkHttpRequest.Builder().url(url).params(params).post(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                handler.onFailed(TransConstant.NET_ERROR);
            }

            @Override
            public void onResponse(String response) {
                try {
                    Logger.t("HTTPUTIL---Page--response").d(response);
                    OperationResult result = JSON.parseObject(response, OperationResult.class);
                    if ("0".equals(result.code)) {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.has("data")) {
                            handler.onSuccess(null);
                            return;
                        }
                        Object obj = new JSONTokener(jsonObject.getString("data")).nextValue();

                        if (obj instanceof JSONArray) {
                            handler.onSuccessPage((PageResult<T>) decoder.converter(jsonObject.getString("data")));
                        } else {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (data.has(PageConstant.COUNT) && data.has(PageConstant.LIST)) {
                                handler.onSuccessPage(decoder.converter(jsonObject.getString("data")));
                            } else {
                                handler.onSuccess((T) decoder.converter(jsonObject.getString("data")));
                            }
                        }

                    } else {
                        handler.onFailed(result.msg);
                    }


                } catch (Exception e) {
                    handler.onFailed(e.toString());
                }

            }
        });
    }

    public static <T> void postQuery(String url, String method, T entity, IConverter<T, String[]> convertor, final IConverter<String, PageResult<T>> decoder, final IAsynServiceHandler<T> handler,
                                     PageResult page) {
        try {
            postQuery(url, method, convertor.converter(entity), decoder, handler, page);
        } catch (Exception e) {
            handler.onFailed(e.toString());
        }
    }


    private static Map<String, String> createRequestParams(String[] parameters, String method) {
        Map<String, String> params = new HashMap<String, String>();
        if (null != parameters) {
            for (int index = 0; index < parameters.length; index += 2) {
                String name  = parameters[index];
                String value = parameters[index + 1];
                if (name != null) {
                    params.put(name, value);
                }
            }
        }
        String time = String.valueOf(System.currentTimeMillis());
        //        params.put("n", DeviceUtil.getImei(HtApplication.getInstance()));
        params.put("n", DeviceUtil.getDeviceId(HtApplication.getInstance()));
        params.put("p", TransConstant.PLATFORM);
        params.put("v", TransConstant.API_VERSION);
        params.put("w", TransConstant.WIDTH);
        params.put("h", TransConstant.HEIGHT);
        params.put("model", TransConstant.DEVICE);
        if (HtApplication.isLogin()) {
            params.put("token", UserManager.getInstance().getHtToken());
        }
        params.put("t", time);
        String token = String.format(TransConstant.KEY, time);
        params.put("api_token",
                Md5Util.getMd5Value(token));
        params.put("net_type", TransConstant.NET_TYPE);
        return params;
    }

    private static <T> void parse(String response, final IConverter<String, T> decoder, final IAsynServiceHandler<T> handler, PageResult page) {
        try {
            OperationResult result = JSON.parseObject(response, OperationResult.class);
            if ("0".equals(result.code)) {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.has("data")) {
                    handler.onSuccess(null);
                    return;
                }
                Object data = new JSONTokener(jsonObject.getString("data")).nextValue();

                if (data instanceof JSONArray) {
                    handler.onSuccessPage((PageResult<T>) decoder.converter(jsonObject.getString("data")));
                } else {
                    handler.onSuccess((T) decoder.converter(jsonObject.getString("data")));
                }

            } else {
                handler.onFailed(result.msg);
            }


        } catch (Exception e) {
            handler.onFailed(e.toString());
        }
    }


}