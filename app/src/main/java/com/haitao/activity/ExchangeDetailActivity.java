package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.ExchangeObject;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.view.CustomImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;


/**
 * 兑换详情
 */
@MLinkRouter(keys = {"exchangeKey"})
public class ExchangeDetailActivity extends BaseActivity {

    private ViewGroup layoutContent;
    private TextView  tvName;
    private TextView  tvComment, tvView;
    private CustomImageView ivImage;
    private TextView        tvNumber;
    private TextView        tvTime;
    private WebView         wvBody;
    private TextView        tvButtom;


    private ExchangeObject sampleObject;
    private   String                                                       id                 = "";
    protected IViewContext<ExchangeObject, IEntityService<ExchangeObject>> commandViewContext = VF.<ExchangeObject>getDefault(ExchangeObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, ExchangeObject obj) {
        Intent intent = new Intent(context, ExchangeDetailActivity.class);
        intent.putExtra(TransConstant.OBJECT, obj);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, ExchangeDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "兑换详情";
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.OBJECT)) {
                sampleObject = (ExchangeObject) intent.getSerializableExtra(TransConstant.OBJECT);
            }
            if (intent.hasExtra(TransConstant.ID)) {
                id = intent.getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {
                // 魔窗字段
                id = intent.getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "exchangeKey");
                kv.put(TraceUtils.Event_Kv_Value, id);
                YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.exchange_detail_title);
        layoutContent = getView(R.id.layoutContent);
        layoutContent.setVisibility(View.GONE);
        tvName = getView(R.id.tvName);
        tvComment = getView(R.id.tvComment);
        tvView = getView(R.id.tvView);
        ivImage = getView(R.id.ivImage);
        int                       screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth() - 2 * 10;
        LinearLayout.LayoutParams lp          = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth / (5f / 2f)));
        ivImage.setLayoutParams(lp);
        tvNumber = getView(R.id.tvNumber);
        tvTime = getView(R.id.tvTime);
        wvBody = getView(R.id.wvBody);
        wvBody.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvBody.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        tvButtom = getView(R.id.tvBottom);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvButtom.setOnClickListener(v -> {
            if (!HtApplication.isLogin()) {
                QuickLoginActivity.launch(mContext);
                return;
            }
            int gold  = TextUtils.isEmpty(UserManager.getInstance().getUser().gold) ? 0 : Integer.parseInt(UserManager.getInstance().getUser().gold);
            int price = TextUtils.isEmpty(sampleObject.prices) ? 0 : Integer.parseInt(sampleObject.prices);
            if (gold < price) {
                ToastUtils.show(mContext, "您的金币余额不足！");
                return;
            }
            ExchangeApplyActivity.launch(mContext, sampleObject);
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getData();
    }

    private void getData() {
        if (null == sampleObject && TextUtils.isEmpty(id))
            finish();
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        commandViewContext.getEntity().tid = null != sampleObject ? sampleObject.tid : id;
        commandViewContext.getService().asynFunction(MethodConstant.EXCHANGE_DETAIL, commandViewContext.getEntity(), new IAsynServiceHandler<ExchangeObject>() {
            @Override
            public void onSuccess(ExchangeObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                if (null != entity) {
                    sampleObject = entity;
                    renderView();
                    layoutContent.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.show(mContext, "该兑换不存在");
                    finish();
                }
            }

            @Override
            public void onSuccessPage(PageResult<ExchangeObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
            }
        });
    }

    private void renderView() {
        tvName.setText(sampleObject.subject);
        ImageLoaderUtils.showOnlineImage(sampleObject.thumb, ivImage);
        tvComment.setText(sampleObject.replies);
        tvView.setText(sampleObject.views);
        String       patternNumber = String.format("物品数量：{%s} 件\n兑换价格：{%s} 金币", sampleObject.number, sampleObject.prices);
        CharSequence charsNumber   = ColorPhrase.from(patternNumber).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.lightGrey)).format();
        tvNumber.setText(charsNumber);
        String       patternTime = String.format("开始时间：{%s}\n结束时间：{%s}", sampleObject.starttime, sampleObject.starttimeto);
        CharSequence charsTime   = ColorPhrase.from(patternTime).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.black)).outerColor(mContext.getResources().getColor(R.color.lightGrey)).format();
        tvTime.setText(charsTime);
        tvButtom.setVisibility(View.VISIBLE);
        tvButtom.setText("3".equals(sampleObject.status) ? "已结束" : "2".equals(sampleObject.status) ? "立即兑换" : "即将开始");
        if ("1".equals(sampleObject.is_apply) && "2".equals(sampleObject.status)) {
            tvButtom.setText("已申请");
        }
        tvButtom.setBackgroundResource("2".equals(sampleObject.status) && "0".equals(sampleObject.is_apply) ? R.color.tab_color : R.color.grey);
        tvButtom.setEnabled("2".equals(sampleObject.status) && "0".equals(sampleObject.is_apply));
        Document document = Jsoup.parse(sampleObject.content);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            tvButtom.setText("已申请");
            tvButtom.setEnabled(false);
            tvButtom.setBackgroundResource(R.color.grey);
        }
    }
}
