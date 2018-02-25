package com.haitao.framework.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Java实现类似C/C++中的__FILE__、__FUNC__、__LINE__等,主要用于日志等功能中。
 * 
 * 
 */

public abstract class FLogComFunc {

	/**
	 * 打印日志时获取当前的程序文件名、行号、方法名 输出格式为：[FileName|MethodName|LineNumber]
	 * 
	 * @return
	 */
	public static String getFileLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[")
				.append(traceElement.getFileName()).append("|")
				.append(traceElement.getMethodName()).append("|")
				.append(traceElement.getLineNumber()).append("]");
		return toStringBuffer.toString();
	}

	// 当前文件名
	public static String _FILE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getFileName();
	}

	// 当前方法名
	public static String _FUNC_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getMethodName();
	}

	// 当前行号
	public static int _LINE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getLineNumber();
	}

	// 当前时间
	public static String _TIME_() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
				Locale.getDefault());
		return sdf.format(now);
	}

}
