package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haitao.R;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.adapter.TalentAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.TalentObject;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.TalentModel;

/**
 * 达人馆
 */
public class TalentFragment extends BaseFragment {
    Context mContext;
    private XListView              lvList;
    private ArrayList<TalentModel> mList;
    private TalentAdapter          mAdapter;
    private String                 cateId;
    private ViewGroup              layoutProgress;

    private int page = 1;

    protected IViewContext<TalentObject, IEntityService<TalentObject>> commandViewContext = VF.<TalentObject>getDefault(TalentObject.class);


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        TAG = "达人馆";
        View messageLayout = initView(inflater);
        initEvent();
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_talent, null);
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(TransConstant.VALUE))
                cateId = bundle.getString(TransConstant.VALUE);

        }
        lvList = getView(view, R.id.content_view);
        lvList.setAutoLoadEnable(true);
        lvList.setPullRefreshEnable(false);
        lvList.setPullLoadEnable(false);
        layoutProgress = getView(view, R.id.llProgress_common_progress);
        return view;
    }


    public void initData() {
        mList = new ArrayList<TalentModel>();
        mAdapter = new TalentAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
        page = 1;
        loadData();
    }

    private void loadData() {
        ForumApi.getInstance().forumTalentsListGet(String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    if (lvList == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    lvList.setVisibility(View.VISIBLE);
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if (1 == page) {
                        mList.clear();
                    }
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


    private void initEvent() {
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                TalentModel talentModel = mList.get(index);
                if (talentModel != null) {
                    TalentDetailActivity.launch(mContext, talentModel.getUid());
                }
            }
        });
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
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
