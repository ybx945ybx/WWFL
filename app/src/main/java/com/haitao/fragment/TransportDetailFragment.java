package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.model.LogisticsCompanyObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TransportDetailFragment extends BaseFragment {
    private Context                mContext;
    private LogisticsCompanyObject storeObject;
    private WebView                wvBody;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "转运详情 - 评论";
        View messageLayout = initView(inflater);
        return messageLayout;
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_transport_detail, null);
        wvBody = getView(view, R.id.wvBody);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            storeObject = (LogisticsCompanyObject) bundle.getSerializable(TransConstant.OBJECT);
            if (null != storeObject) {
                Document document = Jsoup.parse(storeObject.detail);
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
                wvBody.setWebChromeClient(new WebChromeClient() {
                });
            }
        }
    }


}
