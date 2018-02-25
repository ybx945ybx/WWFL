package com.haitao.common.Constant;

/**
 * 页面与页面之间传输的参数
 * Created by tqy on 15/11/18.
 */
public class TransConstant {
    //版本号
    public static final String API_VERSION = "v5";

    public static final String SWAGGER_API_VERSION = "V1.8";

    //平台 android/ios
    public static final String PLATFORM = "android";

    public static final String KEY = "%s@55haitao";

    public static String NET_TYPE = "4G";
    public static String WIDTH    = "";
    public static String HEIGHT   = "";
    public static String DEVICE   = "";


    // id
    public static final String ID          = "id";
    // 标题
    public static final String TITLE       = "title";
    // 网页地址
    public static final String URL         = "url";
    // 位置
    public static final String POSITION    = "position";
    // 类型
    public static final String TYPE        = "type";
    // 对象
    public static final String OBJECT      = "object";
    // 优惠
    public static final String CODE        = "code";
    // 分页
    public static final String PAGE        = "page";
    // 来源
    public static final String SOURCE      = "source";
    // 值
    public static final String VALUE       = "value";
    // 状态
    public static final String STATUS      = "status";
    // 时间
    public static final String TIME        = "time";
    // 邮箱
    public static final String EMAIL       = "email";
    // 楼层
    public static final String FLOOR       = "floor";
    // 用户名
    public static final String AUTHOR_NAME = "authorName";
    // 楼层
    public static final String TEXT        = "text";
    // 国家区号
    public static final String AREA_CODE   = "area_code";


    //登录
    public static final int IS_LOGIN = 0x1001;
    //刷新
    public static final int REFRESH  = 0x1002;
    // 成功反馈
    public static final int SUCCESS  = 0x1003;
    // 回到上一页
    public static final int DISMISS  = 0x1004;
    // 新增
    public static final int ADD      = 0x1005;

    public static final String CHANGE_BROADCAST           = "change_broadcast";
    public static final int    BROAD_LOGIN                = 0x1011; //登录
    public static final int    BROAD_LOGOUT               = 0x1012; //退出登录
    public static final int    BROAD_DEAL_FAV             = 0x1013; //优惠收藏或者取消收藏
    public static final int    BROAD_POST_FAV             = 0x1014; //贴子收藏或者取消收藏
    public static final int    BROAD_SECTION_FAV          = 0x1015; //版块收藏或者取消收藏
    public static final int    BROAD_STORE_FAV            = 0x1016; //商家收藏或者取消收藏
    public static final int    BROAD_NOTICE               = 0x1017; //消息通知发生变化时
    public static final int    BROAD_NOTICE_UPDATE        = 0x1018; //消息通知发生变化时通知获取未读消息
    public static final int    BROAD_SHARE                = 0x1019; //分享成功的通知
    public static final int    BROAD_DEAL_CATEGORY_UPDATE = 0x1020; //更新了优惠分类
    public static final int    CHECK_GOLD                 = 0x1021; //查看金币

    public static class VerifyType {
        //0绑定手机(注册，三方注册，绑定验证原手机，绑定新手机) 1找回密码 2修改提现密码 3申请提现
        public static final String BIND         = "0";
        public static final String FIND_PWD     = "1";
        public static final String WITHDRAW_PWD = "2";
        public static final String CASH_APPLY   = "3";
        public static final String VERIFY_NOW   = "4";
    }

    public static class AdType {
        //d 优惠,s 商家,b 帖子,w 网页,l 注册
        public static final String DEAL            = "d";
        public static final String STORE           = "s";
        public static final String POST            = "b";
        public static final String WEB             = "w";
        public static final String REGIST          = "l";
        public static final String TRANSPORT       = "trans";           // 转运
        public static final String BOARD           = "section";         // 版块
        public static final String USER            = "user";            // 用户详情
        public static final String TAG             = "tag";             // 标签详情
        public static final String EXCHANGE        = "exchange";        // 兑换详情
        public static final String TRIAL           = "trial";           // 试用详情
        public static final String INVITE          = "invite";          // 邀请好友 未登录时跳转登录
        public static final String APP_STORE       = "appstore";        // appstore / 应用商店
        public static final String OUTAPP          = "outapp";          // 外部浏览器打开
        public static final String SIGN            = "sign";            // 每日签到
        public static final String STORE_LIST      = "store_list";      // 返利商家列表
        public static final String TRANS_LIST      = "trans_list";      // 转运商家列表
        public static final String DEAL_DAILY_LIST = "dealDaily_list";  // 优惠日报列表

    }

    public static class WebType {
        public static final String DEAL            = "deal";            // 优惠详情
        public static final String STORE           = "store";           // 商家详情
        public static final String POST            = "thread";          // 帖子详情
        public static final String TRANSPORT       = "trans";           // 转运详情
        public static final String BOARD           = "section";         // 版块详情
        public static final String USER            = "user";            // 用户详情
        public static final String TAG             = "tag";             // 标签详情
        public static final String EXCHANGE        = "exchange";        // 兑换详情
        public static final String TRIAL           = "trial";           // 试用详情
        public static final String INVITE          = "invite";          // 邀请好友 未登录时跳转登录
        public static final String APP_STORE       = "appstore";        // appstore / 应用商店
        public static final String OUTAPP          = "outapp";          // 外部浏览器打开
        public static final String SIGN            = "sign";            // 每日签到
        public static final String STORE_LIST      = "store_list";      // 返利商家列表
        public static final String TRANS_LIST      = "trans_list";      // 转运商家列表
        public static final String DEAL_DAILY_LIST = "dealDaily_list";  // 优惠日报列表
    }


    public static class favType {
        //- 1：收藏优惠 2：收藏转运 3：收藏商家 4：收藏论坛版块 5：收藏帖子
        public static final String DEAL      = "1";
        public static final String TRANSPORT = "2";
        public static final String STORE     = "3";
        public static final String BOARD     = "4";
        public static final String POST      = "5";
    }

    public static class praiseType {
        //1:优惠点赞 2：评论点赞 3：帖子回复点赞 4：帖子点赞
        public static final String DEAL         = "1";
        public static final String COMMENT      = "2";
        public static final String POST_COMMENT = "3";
        public static final String POST         = "4";
    }

    public static final String NET_ERROR = "网络异常，请稍后再试";

    public static final int TIMEOUT = 120;

    public static class NoticeType {
        //1：系统提醒 2：帖子动态 3：好友申请 4：优惠推荐 5：用户会话
        public static final String SYSTEM         = "1";//系统
        public static final String MY_POST        = "2";//贴子动态
        public static final String FRIEND         = "3";
        public static final String RECOMMEND_DEAL = "4";
        public static final String PM             = "5";//消息
    }


    public static class LogType {
        public static final String DEAL_SEARCH_CLICK = "deal-andriod-searchclick";
        public static final String DEAL_CLICK        = "deal-andriod-click";
        public static final String BBS_SEARCH_CLICK  = "bbs-andriod-searchclick";
        public static final String BBS_CLICK         = "bbs-andriod-click";
        public static final String DEAL_SEARCH       = "andriod-deal-search";
        public static final String BBS_SEARCH        = "andriod-bbs-search";
    }
}
