package com.haitao.model;

public class UserObject extends BaseObject {
    /**
     * 用户
     */
    private static final long   serialVersionUID = 1L;
    /**
     * 海淘token
     */
    public               String ht_token         = "";
    /**
     * 用户ID
     */
    public               String uid              = "";
    /**
     * 闪淘token
     */
    public               String st_token         = "";
    /**
     * 可用金额
     */
    public               String current_money    = "";
    /**
     * 待审金额
     */
    public               String waitcashback     = "";
    /**
     * 金币
     */
    public               String gold             = "";
    /**
     * 积分
     */
    public               String credits          = "";
    /**
     * 用户组
     */
    public               String group            = "";
    /**
     * 用户名
     */
    public               String username         = "";
    /**
     * 头像
     */
    public               String avatar           = "";
    /**
     * 性别
     */
    public               String gender           = "";
    /**
     * 是否是VIP
     */
    public               String is_vip           = "";
    /**
     * 用户级别
     */
    public               String userLevel        = "";
    /**
     * 居住地（省）
     */
    public               String province         = "";
    /**
     * 居住地（市）
     */
    public               String city             = "";
    /**
     * 居住地（区）
     */
    public               String district         = "";
    /**
     * 电话号码
     */
    public               String mobile           = "";
    /**
     * 邮箱
     */
    public               String email            = "";
    /**
     * 手机号是否唯一
     */
    public               String mobile_unique    = "0";
    /**
     * 是否设置提现密码
     */
    public               String funds_pwd        = "0";
    /**
     * 区号
     */
    public               String area             = "+86";

    public String hasSetWithdrawPwd;    // 已设置提现密码
    public String hasWithdrawAccount;   // 有提现账户
    public String hasBindedPhone;       // 已绑定手机

    public String cart_count = "0";

    public String newpm      = "";
    public String new_pm     = "";
    public String new_notice = "";

    //上传参数
    public String account      = "";
    public String password     = "";
    public String invite       = "";
    public String checked      = "1";
    //第三方登录的用户信息
    public String type         = "";
    public String open_id      = "";
    public String open_token   = "";
    public String open_unionid = "";
    public String actionToken  = "";
    public String platformName = "";

    /**
     * 验证码
     */
    public String code         = "";
    //旧密码
    public String old_password = "";
    //新密码
    public String new_password = "";
    //确认密码
    public String confirmpwd   = "";

    //登录返回的刷新token
    public String refresh_token = "";
    //注册时返回的token,也是ht_token
    public String token         = "";

    @Override
    public String toString() {
        return "UserObject{" +
                "ht_token='" + ht_token + '\'' +
                ", uid='" + uid + '\'' +
                ", st_token='" + st_token + '\'' +
                ", current_money='" + current_money + '\'' +
                ", waitcashback='" + waitcashback + '\'' +
                ", gold='" + gold + '\'' +
                ", credits='" + credits + '\'' +
                ", group='" + group + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", is_vip='" + is_vip + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", mobile_unique='" + mobile_unique + '\'' +
                ", cart_count='" + cart_count + '\'' +
                ", newpm='" + newpm + '\'' +
                ", new_pm='" + new_pm + '\'' +
                ", new_notice='" + new_notice + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", invite='" + invite + '\'' +
                ", checked='" + checked + '\'' +
                ", type='" + type + '\'' +
                ", open_id='" + open_id + '\'' +
                ", open_token='" + open_token + '\'' +
                ", open_unionid='" + open_unionid + '\'' +
                ", code='" + code + '\'' +
                ", old_password='" + old_password + '\'' +
                ", new_password='" + new_password + '\'' +
                ", confirmpwd='" + confirmpwd + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}