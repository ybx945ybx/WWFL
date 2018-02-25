package com.haitao.common.Constant;

/**
 * Created by a55 on 2017/11/16.
 */

public class CodeConstant {

    /*
    2000账户名非法
    2001密码非法
    2002邀请码非法
    2003未同意注册条款
    2004手机号码不合法
    2005国家代码不合法
    2100账户名或密码错误
    2101使用第三方平台登录成功，但需要绑定55帐号
    2102短信登录验证成功，但是未注册，进行后续注册操作
    2103第三方平台登录验证失败
    2104该手机号没有绑定55帐号
    2105该手机号绑定了多个账号
    2106账户被禁用
    2107该用户已经绑定过此第三方身份验证平台帐号
    2108该第三方身份验证平台帐号已被绑定到其它55帐号
    2109该第三方身份验证平台尚未支持
    2110绑定手机号操作的授权码不正确
    2111该手机号已绑定其它帐号
    2112绑定的新手机号和原手机号相同
    2121短信验证码不匹配
    2122图片验证码不匹配
    2123极验验证码不匹配
    2124短信验证码已过期
    2125手机号码验证失败：用户尚未绑定手机*/

    public static final String PHONE_NOT_BIND_55ACCOUNT          = "2104";   // 该手机号没有绑定55帐号
    public static final String PHONE_NOT_REGISTER                = "2102";   // 短信登录验证成功，但是未注册，进行后续注册操作
    public static final String THIRD_NOT_BIND_55ACCOUNT          = "2101";   // 使用第三方平台登录成功，但需要绑定55帐号
    public static final String PHONE_BIND_MULTI                  = "2105";   // 该手机号绑定了多个账号
    public static final String NOTE_BIND_PHONE                   = "2125";   // 手机号码验证失败：用户尚未绑定手机
    public static final String THIRD_ACCOUNT_HAVE_BIND_55ACCOUNT = "2108";   // 该第三方身份验证平台帐号已被绑定到其它55帐号
    public static final String CODE_NOT_MATCH = "2121";   // 验证码不匹配



}
