package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BoardTopicPagerAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.fragment.BoardTopicFragment;
import com.haitao.model.SectionObject;
import com.haitao.model.TagObject;
import com.haitao.model.forum.BoardObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.PopWindowUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.CustomImageView;
import com.haitao.view.SectionFilterPopView;

import java.util.ArrayList;

import io.swagger.client.model.ForumBoardIndexModelData;
import io.swagger.client.model.ForumBoardModel;
import io.swagger.client.model.ForumSubBoardModel;

/**
 * 版块详情
 */
public class BoardDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup layoutSearch, layoutHead;
    //版块信息
    private CustomImageView ivImage;
    private TextView        tvName;
    private TextView        tvPostNum;
    private TextView        tvNewNum;
    private TextView        tvFav;

    //悬浮栏
    private ImageButton                   btnMore;
    private TabLayout                     tabLayout;
    private ViewPager                     pager;
    private BoardTopicPagerAdapter        adapter;
    private ArrayList<ForumSubBoardModel> tabs;
    ArrayList<BoardTopicFragment> fragments = null;

    private ViewGroup layoutContent;
    private ViewGroup layoutProgress;

    private String id = "";

    private SectionFilterPopView sectionFilterPopView;
    private SectionObject        sectionObject;
    private int    categoryPosition = 0;
    private String typeId           = "";
    ViewGroup contentView = null;


    private ImageView ivPublish;

    private ArrayList<ForumSubBoardModel> orerList = null;
    private boolean                       order    = false;

    private ForumBoardIndexModelData forumBoardIndexModel = null;


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, BoardDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);
        if (null != getIntent() && getIntent().hasExtra(TransConstant.ID)) {
            id = getIntent().getStringExtra(TransConstant.ID);
        }
        TAG = "版块详情";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        layoutHead = getView(R.id.layoutHead);
        btnLeft = getView(R.id.btnLeft);
        layoutSearch = getView(R.id.layoutSearch);
        //版块信息
        ivImage = getView(R.id.ivImage);
        tvName = getView(R.id.tvName);
        tvPostNum = getView(R.id.tvPostNum);
        tvNewNum = getView(R.id.tvNewNum);
        tvFav = getView(R.id.tvFav);
        layoutContent = getView(R.id.layoutContent);
        layoutContent.setVisibility(View.GONE);

        btnMore = getView(R.id.btnMore);
        tabLayout = getView(R.id.tab);
        pager = getView(R.id.id_stickynavlayout_viewpager);
        pager.setOffscreenPageLimit(1);

        layoutProgress = getView(R.id.layoutProgress);
        layoutProgress.setVisibility(View.VISIBLE);

        ivPublish = getView(R.id.ivPublish);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnLeft.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        tvFav.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getData();
    }


    private void getData() {
        ForumApi.getInstance().forumBoardBoardIdIndexGet(id, "0",
                response -> {
                    layoutProgress.setVisibility(View.GONE);
                    layoutContent.setVisibility(View.VISIBLE);
                    if ("0".equals(response.getCode())) {
                        if (null != response.getData()) {
                            forumBoardIndexModel = response.getData();
                            if (null == tabs || tabs.size() <= 0) {
                                tabs = new ArrayList<ForumSubBoardModel>();
                                ForumSubBoardModel forumSubBoardModel = new ForumSubBoardModel();
                                forumSubBoardModel.setSubBoardName("全部");
                                forumSubBoardModel.setSubBoardId(id);
                                tabs.add(forumSubBoardModel);
                                if (null != response.getData().getSubBoards() && response.getData().getSubBoards().size() > 0) {
                                    tabs.addAll(response.getData().getSubBoards());
                                }
                                fragments = new ArrayList<BoardTopicFragment>();
                                for (int i = 0; i < tabs.size(); i++) {
                                    BoardTopicFragment fragment = new BoardTopicFragment();
                                    Bundle             bundle   = new Bundle();
                                    bundle.putString(TransConstant.ID, id);
                                    bundle.putSerializable(TransConstant.VALUE, tabs.get(i).getSubBoardId());
                                    fragment.setArguments(bundle);
                                    fragments.add(fragment);
                                }
                                adapter = new BoardTopicPagerAdapter(getSupportFragmentManager(), fragments, tabs);
                                pager.setAdapter(adapter);
                                tabLayout.setupWithViewPager(pager);
                                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            }
                            if (null != response.getData().getBoardInfo()) {
                                renderView(response.getData().getBoardInfo());
                            }
                            ivPublish.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        finish();
                    }
                }, error -> {
                    layoutProgress.setVisibility(View.GONE);
                    showErrorToast(error);
                });
    }

    private void renderView(ForumBoardModel boardModel) {
        ImageLoaderUtils.showOnlineImage(boardModel.getIcon(), ivImage);
        tvName.setText(boardModel.getBoardName());
        tvPostNum.setText(String.format("帖子：%s", boardModel.getTopicsCount()));
        tvNewNum.setText(String.format("新帖：%s", boardModel.getTodayPostsCount()));
        tvFav.setSelected("1".equals(boardModel.getIsFavorite()));
        tvFav.setText(tvFav.isSelected() ? "已收藏" : "收藏");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                finish();
                break;
            case R.id.layoutSearch:
                BoardTopicSearchActivity.launch(mContext, id);
                break;
            case R.id.btnMore:
                if (null == orerList || orerList.size() <= 0) {
                    orerList = new ArrayList<ForumSubBoardModel>();
                    ForumSubBoardModel cateAllObject = new ForumSubBoardModel();
                    cateAllObject.setSubBoardName(getResources().getString(R.string.forum_order_time));
                    ForumSubBoardModel cateAllObject2 = new ForumSubBoardModel();
                    cateAllObject2.setSubBoardName(getResources().getString(R.string.forum_order_reply));
                    orerList.add(cateAllObject2);
                    orerList.add(cateAllObject);
                }
                if (null == sectionFilterPopView) {
                    sectionFilterPopView = new SectionFilterPopView(mContext);
                    sectionFilterPopView.setCategoryData(tabs);
                    sectionFilterPopView.setStatusData(orerList);
                    sectionFilterPopView.setOnCallbackLitener(new SectionFilterPopView.OnCallbackLitener() {
                        @Override
                        public void onConfirm(int catPosition, int statusPosition) {
                            categoryPosition = catPosition;
                            order = 0 == statusPosition ? false : true;
                            typeId = tabs.get(catPosition).getSubBoardId();
                            PopWindowUtils.dismiss();
                        }

                        @Override
                        public void onChange(int catPosition, int statusPosition) {
                            categoryPosition = catPosition;
                            order = 0 == statusPosition ? false : true;
                            typeId = tabs.get(catPosition).getSubBoardId();
                        }

                        @Override
                        public void onClose() {
                            PopWindowUtils.dismiss();
                        }
                    });
                }
                sectionFilterPopView.setOnClickListener(v1 -> PopWindowUtils.dismiss());
                PopWindowUtils.show(mContext, layoutHead, sectionFilterPopView);
                PopWindowUtils.setOnDismissListener(() -> {
                    pager.setCurrentItem(categoryPosition);
                    for (BoardTopicFragment fragment : fragments) {
                        fragment.order = order;
                    }
                    fragments.get(categoryPosition).refresh();
                });
                break;
            case R.id.tvFav:
                if (!UserManager.getInstance().isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                } else {
                    tvFav.setEnabled(false);
                    if (tvFav.isSelected()) {
                        delFav();
                    } else {
                        addFav();
                    }
                }
                break;
            case R.id.ivPublish:
                BoardObject boardObject = new BoardObject();
                boardObject.id = forumBoardIndexModel.getBoardInfo().getBoardId();
                boardObject.name = forumBoardIndexModel.getBoardInfo().getBoardName();
                ArrayList<TagObject> tagObjects = new ArrayList<TagObject>();
                for (ForumSubBoardModel forumSubBoardModel : forumBoardIndexModel.getSubBoards()) {
                    if ("1".equals(forumSubBoardModel.getAllowedToPost())) {
                        TagObject tagObject = new TagObject();
                        tagObject.id = forumSubBoardModel.getSubBoardId();
                        tagObject.text = forumSubBoardModel.getSubBoardName();
                        tagObjects.add(tagObject);
                    }
                }
                boardObject.subBoardModels = tagObjects;
                TopicSendActivity.launch(mContext, boardObject);
                break;
            default:
                break;
        }
    }

    private void addFav() {
        ForumApi.getInstance().userCollectionPost(TransConstant.favType.BOARD, id, "",
                response -> {
                    tvFav.setEnabled(true);
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "收藏成功");
                        tvFav.setSelected(true);
                        tvFav.setText(tvFav.isSelected() ? "已收藏" : "收藏");
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_SECTION_FAV);
                        mContext.sendBroadcast(mIntent);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, this::showErrorToast);

    }

    private void delFav() {
        ForumApi.getInstance().userCollectionDelete(TransConstant.favType.BOARD, id,
                response -> {
                    tvFav.setEnabled(true);
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "取消收藏");
                        tvFav.setSelected(false);
                        tvFav.setText(tvFav.isSelected() ? "已收藏" : "收藏");
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_SECTION_FAV);
                        mContext.sendBroadcast(mIntent);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, this::showErrorToast);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            getData();
        } else if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            getData();
        }
    }
}
