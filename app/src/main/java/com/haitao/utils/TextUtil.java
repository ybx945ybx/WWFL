package com.haitao.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.activity.WebActivity;
import com.haitao.common.HtApplication;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    /**
     * 复制文字
     *
     * @param txt
     */
    public static void Copy(String txt) {
        ClipboardManager clip = (ClipboardManager) HtApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(txt);
    }


    /**
     * 设置文本中超链接的点击事件
     *
     * @param mContext
     * @param clickableHtmlBuilder
     * @param urlSpan
     */
    public static void setLinkClickable(final Context mContext, final SpannableStringBuilder clickableHtmlBuilder,
                                        final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end   = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        //文字颜色
        ForegroundColorSpan blueColor = new ForegroundColorSpan(Color.BLUE);
        clickableHtmlBuilder.setSpan(blueColor, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                //Do something with URL here.
                Logger.d("weblink = " + urlSpan.getURL());
                String regApp = "#app_type=([a-zA-Z0-9]+)&app_val=([0-9]+)";
                String url    = urlSpan.getURL();
                if (url.contains("55haitao")) {
                    if (url.contains("deals")) {
                        regApp = "deals/([0-9]+)";
                        Pattern patApp = Pattern.compile(regApp);
                        Matcher matApp = patApp.matcher(url);
                        if (matApp.find()) {
                            DiscountDetailActivity.launch(mContext, matApp.group(1));
                        }
                    } else if (url.contains("thread")) {
                        regApp = "thread-([0-9]+)";
                        Pattern patApp = Pattern.compile(regApp);
                        Matcher matApp = patApp.matcher(url);
                        if (matApp.find()) {
                            TopicDetailActivity.launch(mContext, matApp.group(1));
                        }
                    }
                } else {
                    WebActivity.launch(mContext, mContext.getResources().getString(R.string.app_name), url);
                }

				/*if(matApp.find()){
                    String rstType = matApp.group(1);
					String rstValue = matApp.group(2);
					if(rstType.equals("deal")){
						DiscountDetailActivity.launch(mContext, rstValue);
					}else if(rstType.equals("store")){
						StoreDetailActivity.launch(mContext, rstValue);
					}else if(rstType.equals("thread")){
						TopicDetailActivity.launch(mContext, rstValue);
					}else if(rstType.equals("forum")){
						BoardDetailActivity.launch(mContext, rstValue);
					}else if(rstType.equals("cate")){
						TagObject tagObject = new TagObject();
						tagObject.type = CategoryConstant.TAG_CATEGORY;
						tagObject.id = rstValue;
						String str = "&app_name=([^&]*)";
						Pattern pat = Pattern.compile(str);
						Matcher mat = pat.matcher(url);
						if(mat.find()){
							tagObject.name = mat.group(1);
						}

						DiscountActivity.launch(mContext,tagObject);
					}else if(rstType.equals("tag")){
						TagObject tagObject = new TagObject();
						tagObject.type = CategoryConstant.TAG_TAG;
						tagObject.id = rstValue;
						String str = "&app_name=([^&]*)";
						Pattern pat = Pattern.compile(str);
						Matcher mat = pat.matcher(url);
						if(mat.find()){
							tagObject.name = mat.group(1);
						}
						DiscountActivity.launch(mContext,tagObject);
					}
				}else {
					WebActivity.launch(mContext,mContext.getResources().getString(R.string.app_name),url);
				}*/
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLUE);
                //ds.setUnderlineText(true); //设置下划线
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    /**
     * 组装文本中可点击的超链接
     *
     * @param mContext
     * @param html
     * @return
     */
	/*public static CharSequence getClickableHtml(Context mContext, String html) {
		Spanned spannedHtml = Html.fromHtml(html);
		SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
		URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
		for(final URLSpan span : urls) {
			setLinkClickable(mContext,clickableHtmlBuilder, span);
		}
		return clickableHtmlBuilder;
	}*/
    public static CharSequence getClickableHtml(Context mContext, String html) {
        Pattern pattern = Pattern
                .compile("((http://|https://){1}[\\w\\.\\-/:]+)|(#(.+?)#)|(@[\\u4e00-\\u9fa5\\w\\-]+)");
        String       temp    = html;
        Matcher      matcher = pattern.matcher(temp);
        List<String> list    = new LinkedList<String>();
        while (matcher.find()) {
            if (!list.contains(matcher.group())) {
                temp = temp.replace(
                        matcher.group(),
                        "<a href=\"" + matcher.group() + "\">"
                                + matcher.group() + "</a>");
            }
            list.add(matcher.group());
        }
        Spanned                spannedHtml          = Html.fromHtml(temp);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[]              urls                 = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(mContext, clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }


    public static void parseAndJump(String url) {

    }


}
