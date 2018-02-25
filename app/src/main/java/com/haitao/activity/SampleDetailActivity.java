package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.SampleApplyFragment;
import com.haitao.fragment.SampleDetailFragment;
import com.haitao.fragment.SampleReportFragment;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.SampleObject;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;


/**
 * 试用详情
 */
@MLinkRouter(keys = {"trialKey"})
public class SampleDetailActivity extends BaseActivity {
    private TabLayout               tabLayout;
    private ViewPager               viewpager;
    private String[]                tabs;
    private ArrayList<BaseFragment> fragments;
    private BasePagerAdapter        mPagerAdapter;

    private ViewGroup       layoutContent;
    private TextView        tvName;
    private CustomImageView ivImage;
    private TextView        tvViewCount;
    private TextView        tvSize;
    private TextView        tvNumber;
    private TextView        tvCondition;
    private TextView        tvTime;
    private TextView        tvProvider;
    private TextView        tvButtom;


    private SampleObject sampleObject;
    private   String                                                   id                 = "";
    protected IViewContext<SampleObject, IEntityService<SampleObject>> commandViewContext = VF.<SampleObject>getDefault(SampleObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, SampleObject obj) {
        Intent intent = new Intent(context, SampleDetailActivity.class);
        intent.putExtra(TransConstant.OBJECT, obj);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, SampleDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_detail);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "试用详情";
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.OBJECT)) {
                sampleObject = (SampleObject) intent.getSerializableExtra(TransConstant.OBJECT);
            }
            if (intent.hasExtra(TransConstant.ID)) {
                id = intent.getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {
                // 魔窗字段
                id = intent.getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "trialKey");
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
        tvTitle.setText(R.string.sample_detail_title);
        tabLayout = getView(R.id.tab);
        viewpager = getView(R.id.id_stickynavlayout_viewpager);
        layoutContent = getView(R.id.layoutContent);
        layoutContent.setVisibility(View.GONE);
        tvName = getView(R.id.tvName);
        ivImage = getView(R.id.ivImage);
        int                       screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth() - 2 * 10;
        LinearLayout.LayoutParams lp          = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth / (5f / 2f)));
        ivImage.setLayoutParams(lp);
        tvViewCount = getView(R.id.tvViewCount);
        tvSize = getView(R.id.tvSize);
        tvNumber = getView(R.id.tvNumber);
        tvCondition = getView(R.id.tvCondition);
        tvTime = getView(R.id.tvTime);
        tvProvider = getView(R.id.tvProvider);
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

            new AlertDialog.Builder(mContext)
                    .setMessage(String.format("您申请【%s】试用后，系统将会扣除%s金币", sampleObject.title, sampleObject.condition))
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        SampleApplyActivity.launch(mContext, sampleObject);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();


            /*final ConfirmDialogUtils confirmDialogUtils = new ConfirmDialogUtils(mContext);
            confirmDialogUtils.show(String.format("您申请【%s】试用后，系统将会扣除%s金币", sampleObject.title, sampleObject.condition));
            confirmDialogUtils.setOnItemClickLitener(new ConfirmDialogUtils.OnItemClickLitener() {
                @Override
                public void onLeftClick() {
                    confirmDialogUtils.dismiss();
                }

                @Override
                public void onRightClick() {
                    confirmDialogUtils.dismiss();
                    SampleApplyActivity.launch(mContext, sampleObject);
                }
            });*/
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tabs = new String[]{mContext.getResources().getString(R.string.sample_detail_title),
                mContext.getResources().getString(R.string.sample_detail_apply),
                mContext.getResources().getString(R.string.sample_report)
        };
        getData();
    }

    private void getData() {
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        commandViewContext.getEntity().id = null != sampleObject ? sampleObject.id : id;
        commandViewContext.getService().asynFunction(MethodConstant.SAMPLE_DETAIL, commandViewContext.getEntity(), new IAsynServiceHandler<SampleObject>() {
            @Override
            public void onSuccess(SampleObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                if (null != entity) {
                    sampleObject = entity;
                    renderView();
                    layoutContent.setVisibility(View.VISIBLE);
                    fragments = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TransConstant.OBJECT, sampleObject);
                    bundle.putString(TransConstant.TYPE, "smapleDetail");

                    SampleDetailFragment sampleDetailFragment = new SampleDetailFragment();
                    sampleDetailFragment.setArguments(bundle);
                    fragments.add(sampleDetailFragment);

                    SampleApplyFragment sampleApplyFragment = new SampleApplyFragment();
                    sampleApplyFragment.setArguments(bundle);
                    fragments.add(sampleApplyFragment);

                    SampleReportFragment sampleReportFragment = new SampleReportFragment();
                    sampleReportFragment.setArguments(bundle);
                    fragments.add(sampleReportFragment);

                    mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
                    viewpager.setAdapter(mPagerAdapter);
                    tabLayout.setupWithViewPager(viewpager);
                } else {
                    ToastUtils.show(mContext, "该试用不存在");
                    finish();
                }
            }

            @Override
            public void onSuccessPage(PageResult<SampleObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ProgressDialogUtils.dismiss();
            }
        });
    }

    private void renderView() {
        tvName.setText(sampleObject.title);
        ImageLoaderUtils.showOnlineImage(sampleObject.image, ivImage);
        tvViewCount.setText(String.format(mContext.getResources().getString(R.string.sample_detail_view), sampleObject.view_count));
        tvSize.setText(String.format(mContext.getResources().getString(R.string.sample_detail_size), sampleObject.size));
        String       patternNumber = String.format("数量：{%s} 份", sampleObject.number);
        CharSequence charsNumber   = ColorPhrase.from(patternNumber).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
        tvNumber.setText(charsNumber);
        String       patternCondition = String.format("条件：{%s} 金币", sampleObject.condition);
        CharSequence charsCondition   = ColorPhrase.from(patternCondition).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
        tvCondition.setText(charsCondition);
        tvCondition.setVisibility("0".equals(sampleObject.condition) ? View.GONE : View.VISIBLE);
        String       patternTime = String.format("时间：{%s - %s}", sampleObject.start_time, sampleObject.end_time);
        CharSequence charsTime   = ColorPhrase.from(patternTime).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.darkOrange)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
        tvTime.setText(charsTime);
        String       patternProvider = String.format("本商品由{ %s }提供", sampleObject.provider);
        CharSequence charsProvider   = ColorPhrase.from(patternProvider).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.black)).outerColor(mContext.getResources().getColor(R.color.middleGrey)).format();
        tvProvider.setText(charsProvider);
        tvButtom.setVisibility(View.VISIBLE);
        tvButtom.setText("3".equals(sampleObject.type) ? "已结束" : "2".equals(sampleObject.type) ? "申请试用" : "即将开始");
        if ("1".equals(sampleObject.apply) && "2".equals(sampleObject.type)) {
            tvButtom.setText("已申请");
        }
        tvButtom.setBackgroundResource("2".equals(sampleObject.type) && "0".equals(sampleObject.apply) ? R.color.tab_color : R.color.grey);
        tvButtom.setEnabled("2".equals(sampleObject.type) && "0".equals(sampleObject.apply));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            tvButtom.setText("已申请");
            tvButtom.setEnabled(false);
            tvButtom.setBackgroundResource(R.color.grey);
            ((SampleApplyFragment) fragments.get(1)).initData();
        }
    }
}
