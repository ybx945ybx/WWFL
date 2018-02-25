package com.haitao.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.haitao.R;

/**
 * Created by tqy on 2016/1/10.
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    public ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.bg_progress_bar));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.px2), 0, 0));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
       /* LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);*/
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
