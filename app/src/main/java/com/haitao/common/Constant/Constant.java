package com.haitao.common.Constant;

/**
 * 常量
 * Created by tqy on 15/11/18.
 */
public class Constant {
    public static final  boolean IS_DEBUG          = true;
    // TODO: 2018/1/31  
    // 埋点是否上传
    public static final  boolean ACTIVITYANALY     = true;
    // bugly开关
    public static final  boolean BUGLY             = false;
    //测试环境地址
    public static final  String  DEBUG_URL         = "http://app.dev.55haitao.com/" + TransConstant.API_VERSION;
    //QA环境地址
    public static final  String  QA_URL            = "http://app.qa.55haitao.com/" + TransConstant.API_VERSION;
    //正式环境地址
    public static final  String  RELEASE_URL       = "http://app.55haitao.com/" + TransConstant.API_VERSION;
    //URL地址
    public static final  String  URL               = RELEASE_URL;
    //注册时的服务条款
    public static final  String  AGREEMENT_URL     = URL + "/?m=index&c=agreement_app";
    //关于我们服务条款URL
    public static final  String  TERM_URL          = AGREEMENT_URL;
    //swagger测式地址
    public static final  String  SWAGGER_DEBUG_URL = "http://appv6.dev.55haitao.com";
    //swaggerQA地址
    public static final  String  SWAGGER_QA_URL    = "http://appv6.qa.55haitao.com";
    //swagger正式地址
    public static final  String  SWAGGER_PROD_URL  = "https://appv6.55haitao.com";
    // 带环境判断的swagger地址
    public static final  String  SWAGGER_URL       = IS_DEBUG ? SWAGGER_DEBUG_URL : SWAGGER_PROD_URL;
    // VIP专享权益
    private static final String  VIP_RIGHTS_DEBUG  = "http://m.dev.55haitao.com/uservip/privilege";
    private static final String  VIP_RIGHTS_PROD   = "https://m.55haitao.com/uservip/privilege";
    public static final  String  VIP_RIGHTS        = IS_DEBUG ? VIP_RIGHTS_DEBUG : VIP_RIGHTS_PROD;
    // 跳转页url前缀
    public static final  String  JUMPING_PAGE_URL  = "/template/go/go.htm?info=";
    // 降价提醒url前缀
    public static final  String  DEPRECIATE_URL    = SWAGGER_URL + "/template/deal/remind.htm";

    //日志目录
    public static final String LOG_PATH    = "/.haitao/log/";
    public static final String PIC_PATH    = "/.haitao/image/";
    public static final String SHEAR_PATH  = "/.haitao/image/share/";
    public static final String CAMERA_PATH = "/.haitao/image/Camera/";
    public static final String DCIM_PATH   = "/haitao/pic/";

    public static String NET_TYPE = "4g";

}
