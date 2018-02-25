package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.GoldRecordAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.GoldRecordObject;
import com.haitao.model.UserObject;
import com.haitao.view.HtHeadView;
import com.haitao.view.MultipleStatusView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 我的金币
 */
public class GoldActivity extends BaseActivity {
    @BindView(R.id.hv_title)     HtHeadView         mHvTitle; // 标题
    @BindView(R.id.content_view) XListView          mLvContent;
    @BindView(R.id.msv)          MultipleStatusView mMsv;

    //    private XListView                   lvList;
    private ArrayList<GoldRecordObject> mList;
    private GoldRecordAdapter           mAdapter;

    protected IViewContext<GoldRecordObject, IEntityService<GoldRecordObject>> commandViewContext  = VF.<GoldRecordObject>getDefault(GoldRecordObject.class);
    protected IViewContext<UserObject, IEntityService<UserObject>>             userInfoViewContext = VF.<UserObject>getDefault(UserObject.class);
    private TextView mTvGold;
    private TextView mTvScore;
    //    private TextView mTvRebateDesc;    // 返利说明


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, GoldActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launchForResult(Context context) {
        Intent intent = new Intent(context, GoldActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.CHECK_GOLD);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_new);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }

        initData();
    }

    private void initVars() {
        TAG = "我的金币";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        mHvTitle.setOnRightClickListener(view -> ExchangeActivity.launch(mContext));
        View layoutTop = View.inflate(mContext, R.layout.layout_gold, null);
        mLvContent.addHeaderView(layoutTop);
        mTvGold = getView(layoutTop, R.id.tvGold);
        mTvGold.setTextColor(ContextCompat.getColor(mContext, R.color.orangeFF804D));
        mTvScore = getView(layoutTop, R.id.tvScore);
        //        mTvRebateDesc = getView(layoutTop, R.id.tv_rebate_desc);
        //        mTvRebateDesc.setVisibility(View.GONE);
        mMsv.setOnRetryClickListener(v -> initData());
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mLvContent.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadNext();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (TextUtils.isEmpty(UserManager.getInstance().getUser().gold) || TextUtils.isEmpty(UserManager.getInstance().getUser().credits)) {
            // 获取用户信息
            getUserInfo();
        } else {
            mTvGold.setText(UserManager.getInstance().getUser().gold);
            mTvScore.setText(UserManager.getInstance().getUser().credits);
        }

        mList = new ArrayList<GoldRecordObject>();
        mAdapter = new GoldRecordAdapter(mContext, mList);
        mLvContent.setAdapter(mAdapter);
        mLvContent.setAutoLoadEnable(true);
        mLvContent.setPullLoadEnable(false);
        mMsv.showLoading();
        loadData();
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

    private void loadData() {
        commandViewContext.getPage().page = 1;
        commandViewContext.asynQuery(MethodConstant.GOLD_LIST, commandViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        commandViewContext.asynQueryNext(MethodConstant.GOLD_LIST, commandViewContext.getEntity(), new responseHandler());
    }


    class responseHandler implements IAsynServiceHandler<GoldRecordObject> {

        @Override
        public void onSuccess(GoldRecordObject entity) throws Exception {
        }

        @Override
        public void onSuccessPage(PageResult<GoldRecordObject> entity) throws Exception {
            //            ProgressDialogUtils.dismiss();
            mMsv.showContent();
            mLvContent.stopRefresh();
            mLvContent.stopLoadMore();
            if (1 == commandViewContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == commandViewContext.getPage().page)
                    mLvContent.setRefreshTime();
                if (entity.pageCount <= commandViewContext.getPage().page) {
                    mLvContent.setPullLoadEnable(false);
                } else {
                    mLvContent.setPullLoadEnable(true);
                }
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailed(String error) {
            //            ProgressDialogUtils.dismiss();
            if (mMsv == null)
                return;
            mMsv.showError();
            mLvContent.stopRefresh();
            mLvContent.stopLoadMore();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        userInfoViewContext.getService().asynFunction(MethodConstant.ACCOUNT_INFO, userInfoViewContext.getEntity(), new IAsynServiceHandler<UserObject>() {
            @Override
            public void onSuccess(UserObject entity) throws Exception {
                if (null != entity) {
                    //                    ImageLoaderUtils.clear();
                    entity.uid = UserManager.getInstance().getUserId();
                    entity.ht_token = UserManager.getInstance().getHtToken();
                    entity.st_token = UserManager.getInstance().getStToken();
                    //entity.avatar = HtApplication.userObject.avatar;
                    if (!UserManager.getInstance().getUser().newpm.equals(entity.newpm)) {
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_NOTICE);
                        mContext.sendBroadcast(mIntent);
                    }
                    entity.refresh_token = UserManager.getInstance().getUser().refresh_token;
                    UserManager.getInstance().setUser(entity);
                    // 金币
                    mTvGold.setText(entity.gold);
                    // 积分
                    mTvScore.setText(entity.credits);
                }
            }

            @Override
            public void onSuccessPage(PageResult<UserObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    /**
     * 如何赚金币
     */
    @OnClick(R.id.tv_how_to_earn_gold)
    public void onClickHowToEarnGold() {
        // TODO: 2017/8/17 待替换
        TopicDetailActivity.launch(mContext, "1792281");
    }

    /**
     * 金币兑换规则
     */
    @OnClick(R.id.tv_gold_exchange_rule)
    public void onClickGoldExchangeRule() {
        // TODO: 2017/8/17 待替换
        TopicDetailActivity.launch(mContext, "1841492");
    }

    /**
     * 兑换奖品
     */
    @OnClick(R.id.tv_exchange_award)
    public void onClickExchangeAward() {
        ExchangeActivity.launch(mContext);
    }

    @Override
    public void finish() {
        if (HtApplication.isLogin())
            setResult(RESULT_OK);
        super.finish();
    }
}
