/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技-版权所有
 * ========================================================
 * 本软件由杭州偶尔科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.haitao.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @描述 数据合法性校验
 */
public class ValidateUtils {

	/**
	 * 是否是email地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (TextUtils.isEmpty(email)) {
			return false;
		}
		
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
	
	
	/**
	 * 是否是手机号码
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return false;
		}
		
		Pattern pattern = Patterns.PHONE;
		Matcher matcher = pattern.matcher(phone);
		return matcher.find();
	}
	
	
	/**
	 * 是否是ip地址
	 * @param address
	 * @return
	 */
	public static boolean isIpAddress(String address) {
		if (TextUtils.isEmpty(address)) {
			return false;
		}
		
		Pattern pattern = Patterns.IP_ADDRESS;
		Matcher matcher = pattern.matcher(address);
		return matcher.find();
	}
	
	
	/**
	 * 是否是URL地址
	 * @param url
	 * @return
	 */
	public static boolean isWebUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		Pattern pattern = Patterns.WEB_URL;
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}
}
