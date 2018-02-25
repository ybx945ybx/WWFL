package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.SampleObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 试用详情 - 试用详情
 */
public class SampleDetailFragment extends BaseFragment {
    Context mContext;
    WebView wvBody;
    SampleObject sampleObject;
    String type = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View messageLayout = initView(inflater);
        initEvent();
        initError(messageLayout);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView(LayoutInflater inflater) {
        TAG = "试用详情 - 试用详情";
        View view = inflater.inflate(R.layout.fragment_sample_detail, null);
        if(null != getArguments()){
            Bundle bundle = getArguments();
            if(getArguments().containsKey(TransConstant.OBJECT))
                sampleObject = (SampleObject) bundle.getSerializable(TransConstant.OBJECT);
            if(getArguments().containsKey(TransConstant.TYPE))
                type = bundle.getString(TransConstant.TYPE);
        }
        wvBody = getView(view,R.id.wvBody);
        wvBody.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        return view;
    }


    public void initData() {
        if(null == sampleObject)
            return;
        Document document = Jsoup.parse(sampleObject.describe);
        Elements elements = document.getElementsByTag("img");
        if (elements.size() != 0) {
            for (Element e_Img : elements) {
//                    e_Img.attr("style", "width:100%");
            }
        }
        String newHtmlContent = document.toString();
        wvBody.loadDataWithBaseURL(String.format("%s/public/js/lazyload/seajs", Constant.URL), newHtmlContent, "text/html",
                "utf-8", null);
        // 启用javascript
        wvBody.getSettings().setJavaScriptEnabled(true);
        // 添加js交互接口类，并起别名 imagelistner
        wvBody.setWebChromeClient(new WebChromeClient() {});
    }

    private void initEvent() {
    }




    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
