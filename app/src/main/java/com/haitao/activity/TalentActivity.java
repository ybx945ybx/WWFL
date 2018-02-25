package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.TalentAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TopicLink;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;
import com.haitao.view.SlideCycleView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.SlidePicModel;
import io.swagger.client.model.TalentModel;


/**
 * 更多达人页
 */
public class TalentActivity extends BaseActivity {
    //顶部
    private SlideCycleView           layoutCircle;
    private ArrayList<SlidePicModel> adList;

    private View      headerView;
    private ViewGroup layoutRecommendTalent, layoutNextTalent;
    //    private TextView tvSubTitle;
    private CustomImageView ivImage;
    private TextView        tvName;
    private TextView        tvLevel;
    private TextView        tvPostNum;


    private XListView              lvList;
    private ArrayList<TalentModel> mList;
    private TalentAdapter          mAdapter;

    private ViewGroup layoutProgress;

    private int page = 1;

    private TalentModel nextTalent = null;
    //成为达人的帖子ID
    private String     talentTopicId;
    private HtHeadView mHtHeadView;

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, TalentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        TAG = "全部达人";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initTop();
        //        tvTitle.setText("全部达人");
        lvList = getView(R.id.lvList);
        mHtHeadView = getView(R.id.ht_headview);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(true);
        lvList.setPullLoadEnable(false);
        lvList.setVisibility(View.GONE);
        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);
        mHtHeadView.setCenterText("全部达人");
        initHeaderView();
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.layout_talent, null);
        lvList.addHeaderView(headerView);
        layoutCircle = getView(headerView, R.id.layoutCircle);
        int                       screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams lp          = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenWidth / (16f / 6f)));
        layoutCircle.setLayoutParams(lp);
        layoutRecommendTalent = getView(headerView, R.id.layoutRecommendTalent);
        layoutNextTalent = getView(headerView, R.id.layoutNextTalent);
        //        tvSubTitle = getView(hedaderView,R.id.tvSubTitle);
        ivImage = getView(headerView, R.id.ivImage);
        tvName = getView(headerView, R.id.tvName);
        tvLevel = getView(headerView, R.id.tvLevel);
        tvPostNum = getView(headerView, R.id.tvPostNum);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //        tvRight.setOnClickListener(v -> TopicDetailActivity.launch(mContext, talentTopicId));
        mHtHeadView.setOnRightClickListener(view -> TopicDetailActivity.launch(mContext, talentTopicId));
        ivImage.setOnClickListener(v -> {
            if (null != nextTalent) {
                TalentDetailActivity.launch(mContext, nextTalent.getUid());
            }
        });
        layoutNextTalent.setOnClickListener(view -> {
            if (null != nextTalent) {
                TopicDetailActivity.launch(mContext, nextTalent.getInterviewTid());
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            if (mList.size() > 0) {
                int index = position - lvList.getHeaderViewsCount();
                if (index >= 0) {
                    TalentModel talentModel = mList.get(index);
                    if (null != talentModel) {
                        TalentDetailActivity.launch(mContext, talentModel.getUid());
                    }
                }
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadNext();
            }
        });
    }

    /**
     * 初始化数据
     */

    private void initData() {
        //        tvRight.setText("成为达人");
        mHtHeadView.setRightText("成为达人");
        //焦点图
        adList = new ArrayList<SlidePicModel>();

        mList = new ArrayList<TalentModel>();
        mAdapter = new TalentAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        page = 1;
        getData();

    }

    private void getData() {
        ForumApi.getInstance().forumTalentsIndexGet(String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            talentTopicId = response.getData().getTalentIntro();
                            //                            tvRight.setVisibility(TextUtils.isEmpty(talentTopicId) ? View.GONE : View.VISIBLE);
                            mHtHeadView.setRightTextVisible(!TextUtils.isEmpty(talentTopicId));

                            if (null != response.getData().getSlidePics() && response.getData().getSlidePics().size() > 0) {
                                layoutCircle.setVisibility(View.VISIBLE);
                                layoutCircle.setImageResources(response.getData().getSlidePics(), mAdCycleViewListener);
                            } else {
                                layoutCircle.setVisibility(View.GONE);
                            }

                            if (null != response.getData().getNextTalent()) {
                                layoutRecommendTalent.setVisibility(View.VISIBLE);
                                nextTalent = response.getData().getNextTalent();
                                renderView(response.getData().getNextTalent());
                            } else {
                                layoutRecommendTalent.setVisibility(View.GONE);
                            }

                            if (1 == page) {
                                mList.clear();
                            }
                            if (null != response.getData().getTalents() && response.getData().getTalents().size() > 0) {
                                mList.addAll(response.getData().getTalents());
                                lvList.setPullLoadEnable(response.getData().getTalents().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                            mAdapter.notifyDataSetChanged();

                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                });
    }

    private void loadNext() {
        ForumApi.getInstance().forumTalentsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                                mList.addAll(response.getData().getRows());
                                lvList.setPullLoadEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                            } else {
                                lvList.setPullLoadEnable(false);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    if (lvList == null)
                        return;
                    showErrorToast(error);
                    layoutProgress.setVisibility(View.GONE);
                });
    }

    //加载图片资源
    private SlideCycleView.ImageCycleViewListener mAdCycleViewListener = (position, v) -> TopicLink.jump(mContext, (SlidePicModel) v.getTag(), TopicLink.SOURCE_TYPE.BANNER);

    private void renderView(TalentModel talentModel) {
        //        tvSubTitle.setText("下期达人预告");
        ImageLoaderUtils.showOnlineGifImage(talentModel.getAvatar(), ivImage);
        tvName.setText(talentModel.getUsername());
        tvLevel.setText(talentModel.getCategory());
        tvPostNum.setText(String.format("帖子：%s   |   精华：%s", talentModel.getTopicsCount(), talentModel.getDigestTopicsCount()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != adList && adList.size() > 0) {
            layoutCircle.pushImageCycle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adList && adList.size() > 0) {
            layoutCircle.startImageCycle();
        }
    }
}
