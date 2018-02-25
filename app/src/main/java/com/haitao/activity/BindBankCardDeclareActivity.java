package com.haitao.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.CardBindChangeEvent;
import com.haitao.view.HtHeadView;
import com.haitao.view.ToastPopuWindow;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.BindBankCardSuccessModelData;

/**
 * 绑定银行卡说明/授权
 */
public class BindBankCardDeclareActivity extends BaseActivity {

    @BindView(R.id.rlyt_bind_agree) RelativeLayout rlytBindAgree;
    @BindView(R.id.ht_headview)     HtHeadView     htHeadView;
    @BindView(R.id.web_view)        WebView        webView;

    private ToastPopuWindow toastPopuWindow;

    private int type;         // 1查看说明不显示同意授权按钮 2授权，显示同意授权按钮

    private String url;
    private String cardNumber;
    private String cardAccount;
    private String imei;

    /**
     * 查看说明跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context, int type, String url) {
        Intent intent = new Intent(context, BindBankCardDeclareActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        intent.putExtra(TransConstant.URL, url);
        context.startActivity(intent);
    }

    /**
     * 授权跳转到本页
     *
     * @param context mContext
     */
    public static void launch(Context context, int type, String cardNumber, String cardAccount, String imei, String url) {
        Intent intent = new Intent(context, BindBankCardDeclareActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        intent.putExtra("cardNumber", cardNumber);
        intent.putExtra("cardAccount", cardAccount);
        intent.putExtra("imei", imei);
        intent.putExtra(TransConstant.URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card_declare);
        ButterKnife.bind(this);

        initVars();
        initView();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(TransConstant.TYPE, 0);
            cardNumber = intent.getStringExtra("cardNumber");
            cardAccount = intent.getStringExtra("cardAccount");
            imei = intent.getStringExtra("imei");
            url = intent.getStringExtra(TransConstant.URL);
        }

    }

    private void initView() {
        rlytBindAgree.setVisibility(type == 2 ? View.VISIBLE : View.GONE);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        webView.getSettings().setSupportZoom(false); //可以缩放
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);//默认缩放模式
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);

        webView.loadUrl(url);
    }

    /**
     * 授权绑卡
     */
    @OnClick(R.id.rlyt_bind_agree)
    public void onClick() {
        ForumApi.getInstance().userAccountBankCardPost(cardNumber, cardAccount, imei,
                response -> {
                    Logger.d(response.toString());
                    if ("0".equals(response.getCode())) {
                        EventBus.getDefault().post(new CardBindChangeEvent());
                        showSuccessWindow(response.getData());
                    } else {
                        toastPopuWindow = ToastPopuWindow.makeText((BaseActivity) mContext, response.getMsg()).parentView(htHeadView);
                        toastPopuWindow.show();
                    }
                },
                error -> showErrorToast(error));

    }

    /**
     * 绑定成功后的弹窗
     * @param data
     */
    private void showSuccessWindow(List<BindBankCardSuccessModelData> data) {
//        if (null != data && data.size() > 0) {
//            if (data.size() > 9) {
//                BindCardSuccessPopupwindow bindCardSuccessPopupwindow = new BindCardSuccessPopupwindow(this, new ArrayList<>(data.subList(0, 9)));
//                bindCardSuccessPopupwindow.showOrDismiss(htHeadView);
//            } else {
//                BindCardSuccessPopupwindow bindCardSuccessPopupwindow = new BindCardSuccessPopupwindow(this, new ArrayList<>(data));
//                bindCardSuccessPopupwindow.showOrDismiss(htHeadView);
//            }
//        }
        Dialog dialog = new Dialog(mContext, R.style.st_loading_dialog);
        Window win    = dialog.getWindow();
        if (win == null) return;
        win.setGravity(Gravity.CENTER);
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setBackgroundDrawableResource(R.color.tranparent_20);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        final LayoutInflater inflater  = LayoutInflater.from(mContext);
        View                 v         = inflater.inflate(R.layout.dlg_bind_card_success, null);// 得到加载view
        TextView             tvConfirm = v.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(v1 -> dialog.dismiss());
        dialog.setContentView(v);// 设置布局
        dialog.setOnDismissListener(dialog1 -> onBackPressed());
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toastPopuWindow != null && toastPopuWindow.isShowing()) {
            toastPopuWindow.dismiss();
        }
    }
}
