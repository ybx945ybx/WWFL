package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.CalendarDateAdapter;
import com.haitao.common.Constant.Constant;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.Enum.CalanderType;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.CalanderCellObject;
import com.haitao.model.SignListObject;
import com.haitao.model.SignObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.calendar.CustomDate;
import com.haitao.utils.calendar.DateUtil;
import com.haitao.view.CustomImageView;
import com.haitao.view.FullGirdView;
import com.haitao.view.RoundedImageView;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 签到
 */
public class SignActivity extends BaseActivity implements View.OnClickListener {
    private TextView        monthText;
    private TextView        tvNum;
    private TextView        tvNotice;//话题
    private TextView        btnCommint;
    private FullGirdView    gvList;
    private EditText        edCommit;
    private CustomImageView mImgAd;

    private LinearLayout layoutTopic;

    private RoundedImageView ivAvator;//评论头像
    private LinearLayout     layoutComment;//评论布局

    private LinearLayout     layoutReply;
    private TextView         tvContent;
    private RoundedImageView ivImage;

    private static CustomDate mShowDate; // 自定义的日期，包括year,month,day
    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 6; // 6行


    private Row rows[] = new Row[TOTAL_ROW]; // 行数组，每个元素代表一行
    ArrayList<CalanderCellObject> mList = new ArrayList<CalanderCellObject>();
    CalendarDateAdapter mAdapter;
    protected IViewContext<SignListObject, IEntityService<SignListObject>> commandViewContext = VF.<SignListObject>getDefault(SignListObject.class);
    ArrayList<SignObject> signList        = null;
    //上个月的天数
    int                   currentMontyDay = 0;

    int todayPosition = 0;
    //话题帖子的id
    private String tid = "";

    private WebView wvBody;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, SignActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceCalanderType) {
        super.onCreate(savedInstanceCalanderType);
        setContentView(R.layout.activity_sign);
        TAG = "签到";
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.sign_submit);
        monthText = getView(R.id.tvCurrentMonth);
        gvList = getView(R.id.gv_order_pics);
        tvNotice = getView(R.id.tvNotice);
        ivAvator = getView(R.id.img_avatar);
        layoutTopic = getView(R.id.layoutTopic);
        mImgAd = getView(R.id.img_ad);

        layoutComment = getView(R.id.layoutComment);
        edCommit = getView(R.id.edCommit);
        //        tvSignDays = getView(R.id.tvSignDays);
        btnCommint = getView(R.id.btnCommint);
        //        btnSubmit.setText(R.string.sign_over);
        tvNum = getView(R.id.tvNum);

        layoutReply = getView(R.id.layoutReply);
        tvContent = getView(R.id.tvContent);
        ivImage = getView(R.id.ivImage);
        wvBody = getView(R.id.wvBody);
        wvBody.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnCommint.setOnClickListener(this);
        edCommit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                checkSubmitEnable(s.toString());
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        layoutTopic.setOnClickListener(this);

    }

    private void checkSubmitEnable(String content) {
        btnCommint.setEnabled(!TextUtils.isEmpty(content));
        btnCommint.setTextColor(getResources().getColor(btnCommint.isEnabled() ? R.color.midBlue : R.color.lightGrey));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        SPUtils.put(mContext, SPConstant.LAST_SIGN_POP_TIME + "_" + UserManager.getInstance().getUserId(), System.currentTimeMillis());
        checkSubmitEnable("");
        loadData();

    }

    public void loadData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        commandViewContext.getService().asynFunction(MethodConstant.SIGN_LIST, commandViewContext.getEntity(), new IAsynServiceHandler<SignListObject>() {
            @Override
            public void onSuccess(SignListObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                if (null != entity) {
                    // 广告位
                    if (entity.sign_ad != null) {
                        SignListObject.SignAdObject ad = entity.sign_ad;
                        if (!TextUtils.isEmpty(ad.url) && !TextUtils.isEmpty(ad.img)) {
                            mImgAd.setVisibility(View.VISIBLE);
                            // 广告图
                            ImageLoaderUtils.showOnlineImage(ad.img, mImgAd);
                            // 点击跳转外部浏览器
                            mImgAd.setOnClickListener(v -> {
                                Uri    uri    = Uri.parse(ad.url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            });
                        }
                    }

                    if (TextUtils.isEmpty(entity.now_time)) {
                        String[] stringArr = entity.now_time.split("-");
                        mShowDate = new CustomDate(Integer.parseInt(stringArr[0]), Integer.parseInt(stringArr[1]), Integer.parseInt(stringArr[2]));
                    }
                    String signTips = String.format(getResources().getString(R.string.sign_days), TextUtils.isEmpty(entity.continue_sign_num) ? "0" : entity.continue_sign_num);
                    tvNum.setText(signTips);


                    if (null == mShowDate)
                        mShowDate = new CustomDate();
                    monthText.setText(String.format(getResources().getString(R.string.sign_month), mShowDate.year, mShowDate.month));
                    signList = entity.list;
                    fillDate();//
                    currentMontyDay = 0;
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i].cells[0].state == CalanderType.NEXT_MONTH_DAY)
                            break;
                        for (int j = 0; j < rows[i].cells.length; j++) {
                            CalanderCellObject obj = rows[i].cells[j];
                            if (obj.state == CalanderType.CURRENT_MONTH_DAY) {
                                //取出当月的签到情况
                                if (signList.size() > currentMontyDay) {
                                    obj.state = "1".equals(signList.get(currentMontyDay).sign) ? CalanderType.SIGN_DAY : CalanderType.CURRENT_MONTH_DAY;
                                    //判断是否是今天，可否允许签到
                                    if (entity.now_time.equals(signList.get(currentMontyDay).date)) {
                                        todayPosition = mList.size();
                                        if (obj.state != CalanderType.SIGN_DAY) {
                                            Logger.e("============" + entity.now_time + " =======");
                                        }
                                    }
                                    currentMontyDay++;
                                }
                            }
                            mList.add(obj);
                        }
                    }
                    mAdapter = new CalendarDateAdapter(mContext, mList);
                    mAdapter.setToday(mShowDate.day);
                    gvList.setAdapter(mAdapter);
                    if (!TextUtils.isEmpty(entity.ad)) {
                        wvBody.setVisibility(View.VISIBLE);
                        Document document = Jsoup.parse(entity.ad);
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
                        wvBody.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                view.getSettings().setJavaScriptEnabled(true);
                                super.onPageFinished(view, url);
                            }

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                view.getSettings().setJavaScriptEnabled(true);
                                super.onPageStarted(view, url, favicon);
                            }

                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                Logger.e(url);
                                String  regEx = "uid=[0-9]{1,}"; //表示a或F
                                Pattern pat   = Pattern.compile(regEx);
                                Matcher mat   = pat.matcher(url);
                                boolean rs    = mat.find();
                                if (rs) {
                                    url = url.replaceFirst("&uid=[0-9]{1,}", "");
                                } else {
                                    if (CommonUtils.isAppUrl(url)) {
                                        CommonUtils.parseUrlAndGo(mContext, url);
                                    } else {
                                        WebActivity.launch(mContext, getResources().getString(R.string.app_name), url);
                                    }
                                }

                                return true;
                            }
                        });
                    }
                    sign();
                    getSignFaceicon();
                }
            }

            @Override
            public void onSuccessPage(PageResult<SignListObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
                ToastUtils.show(mContext, error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCommint://今日话题评论
                //                if (!TextUtils.isEmpty(edCommit.getText().toString().trim())) {
                topicSign();
                //                }
                break;
            case R.id.layoutTopic:
                if (!TextUtils.isEmpty(tid) && !"0".equals(tid)) {
                    TopicDetailActivity.launch(mContext, tid);
                }
                break;
         /*   case R.id.img_ad: // 广告位
                Uri uri = Uri.parse();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;*/
            default:
                break;
        }
    }

    //话题签到
    private void topicSign() {
        ProgressDialogUtils.show(mContext, "数据正在提交……");
        commandViewContext.getEntity().topic = edCommit.getText().toString().trim();
        commandViewContext.getService().asynFunction(MethodConstant.TOPIC_SIGN, commandViewContext.getEntity(), new IAsynServiceHandler<SignListObject>() {
            @Override
            public void onSuccess(SignListObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                layoutComment.setVisibility(View.GONE);
                layoutReply.setVisibility(View.VISIBLE);
                ImageLoaderUtils.showOnlineImage(UserManager.getInstance().getUser().avatar, ivImage, R.mipmap.ic_default_avator);
                tvContent.setText(edCommit.getText().toString().trim());
                ToastUtils.show(mContext, String.format("评论话题成功，+%s金币", entity.topic_gold));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            }

            @Override
            public void onSuccessPage(PageResult<SignListObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
                ToastUtils.show(mContext, error);
            }
        });
    }


    //今日话题
    private void getSignFaceicon() {
        commandViewContext.getService().asynFunction(MethodConstant.SIGN_FACEICON, commandViewContext.getEntity(), new IAsynServiceHandler<SignListObject>() {
            @Override
            public void onSuccess(SignListObject entity) throws Exception {

                if (TextUtils.isEmpty(entity.faceicon)) {
                    layoutTopic.setVisibility(View.GONE);
                    layoutComment.setVisibility(View.GONE);
                    layoutReply.setVisibility(View.GONE);
                    return;
                }
                layoutTopic.setVisibility(View.VISIBLE);
                tvNotice.setText(entity.faceicon);
                tid = entity.tid;
                if ("0".equals(entity.type)) {//未签到话题
                    layoutComment.setVisibility(View.VISIBLE);
                    layoutReply.setVisibility(View.GONE);
                    ImageLoaderUtils.showOnlineImage(UserManager.getInstance().getUser().avatar, ivAvator, R.mipmap.ic_default_avator);
                } else if ("1".equals(entity.type)) {
                    layoutComment.setVisibility(View.GONE);
                    layoutReply.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.showOnlineImage(UserManager.getInstance().getUser().avatar, ivImage, R.mipmap.ic_default_avator);
                    tvContent.setText(entity.topic);
                }
            }

            @Override
            public void onSuccessPage(PageResult<SignListObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);

            }
        });
    }

    //签到
    private void sign() {
        commandViewContext.getService().asynFunction(MethodConstant.SIGN, commandViewContext.getEntity(), new IAsynServiceHandler<SignListObject>() {
            @Override
            public void onSuccess(SignListObject entity) throws Exception {
                ToastUtils.show(mContext, entity.sign_msg);
                CalanderCellObject obj = mList.get(todayPosition);
                obj.state = CalanderType.SIGN_DAY;
                mAdapter.notifyDataSetChanged();
                String signTips = String.format(getResources().getString(R.string.sign_days), TextUtils.isEmpty(entity.continue_sign_num) ? "0" : entity.continue_sign_num);
                tvNum.setText(signTips);
            }

            @Override
            public void onSuccessPage(PageResult<SignListObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {


            }
        });
    }


    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay(); // 今天
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month - 1); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL; // 单元格位置
                // 这个月的
                if (position >= firstDayWeek
                        && position < firstDayWeek + currentMonthDays) {
                    day++;
                    rows[j].cells[i] = new CalanderCellObject(CustomDate.modifiDayForObject(
                            mShowDate, day), CalanderType.CURRENT_MONTH_DAY, i, j);
                    // 今天
                   /* if (isCurrentMonth && day == monthDay ) {
                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
                        rows[j].cells[i] = new CalanderCellObject(date, CalanderType.TODAY, i, j);
                    }

                    if (isCurrentMonth && day > monthDay) { // 如果比这个月的今天要大，表示还没到
                        rows[j].cells[i] = new CalanderCellObject(
                                CustomDate.modifiDayForObject(mShowDate, day),
                                CalanderType.UNREACH_DAY, i, j);
                    }*/

                    // 过去一个月
                } else if (position < firstDayWeek) {
                    rows[j].cells[i] = new CalanderCellObject(new CustomDate(mShowDate.year,
                            mShowDate.month - 1, lastMonthDays
                            - (firstDayWeek - position - 1)),
                            CalanderType.PAST_MONTH_DAY, i, j);
                    // 下个月
                } else if (position >= firstDayWeek + currentMonthDays) {
                    rows[j].cells[i] = new CalanderCellObject((new CustomDate(mShowDate.year,
                            mShowDate.month + 1, position - firstDayWeek
                            - currentMonthDays + 1)),
                            CalanderType.NEXT_MONTH_DAY, i, j);
                }
            }
        }
    }

    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public CalanderCellObject[] cells = new CalanderCellObject[TOTAL_COL];

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                initData();
            }
        }
    }


}
