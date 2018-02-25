package com.haitao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.NoticeAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.NoticeObject;
import com.haitao.model.UserObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.view.refresh.XListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * 消息通知
 */
public class NoticeFragment extends BaseFragment {
    Context mContext;
    private XListView               lvList;
    private ArrayList<NoticeObject> mList;
    private NoticeAdapter           mAdapter;
    public static final int MESSAGE = 0;//消息
    public static final int NOTICE  = 1;//通知
    public              int type    = 0;

    protected IViewContext<NoticeObject, IEntityService<NoticeObject>> noteceViewContext = VF.<NoticeObject>getDefault(NoticeObject.class);

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
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
        View view = inflater.inflate(R.layout.fragment_notice, null);
        if (null != getArguments() && getArguments().containsKey(TransConstant.TYPE)) {
            Bundle bundle = getArguments();
            type = bundle.getInt(TransConstant.TYPE);
        }
        lvList = getView(view, R.id.lvList);
        return view;
    }


    public void initData() {
        mList = new ArrayList<NoticeObject>();
        mAdapter = new NoticeAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        loadData();
    }

    private void initEvent() {
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                noteceViewContext.getPage().page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadNext();
            }
        });
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - lvList.getHeaderViewsCount();
            if (index >= 0) {
                NoticeObject obj = mList.get(index);
                if (obj != null && obj.isnew.equals("1")) {
                    obj.isnew = "0";
                    mList.set(index, obj);
                    TextView tvTpye    = getView(view, R.id.tvTpye);
                    TextView tvName    = getView(view, R.id.tvName);
                    TextView tvContent = getView(view, R.id.tvContent);
                    tvTpye.setVisibility("1".equals(obj.isnew) ? View.VISIBLE : View.GONE);
                    tvContent.setTextColor(mContext.getResources().getColor(!"1".equals(obj.isnew) ? R.color.lightGrey : R.color.darkGrey));
                    tvName.setTextColor(mContext.getResources().getColor(!"1".equals(obj.isnew) ? R.color.lightGrey : R.color.midGrey));
                }
            }
        });
    }


    private void loadData() {
        noteceViewContext.getPage().page = 1;
        ProgressDialogUtils.show(mContext, R.string.xlistview_header_hint_loading);
        Logger.d("type = " + type);
        noteceViewContext.asynQuery(type == MESSAGE ? MethodConstant.MY_MESSAGE : MethodConstant.MY_NOTICE, noteceViewContext.getEntity(), new responseHandler());
    }

    private void loadNext() {
        noteceViewContext.asynQueryNext(type == MESSAGE ? MethodConstant.MY_MESSAGE : MethodConstant.MY_NOTICE, noteceViewContext.getEntity(), new responseHandler());
    }

    class responseHandler implements IAsynServiceHandler<NoticeObject> {

        @Override
        public void onSuccess(NoticeObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<NoticeObject> entity) throws Exception {
            UserObject userObject = UserManager.getInstance().getUser();
            userObject.newpm = "0";
            UserManager.getInstance().setUser(userObject);
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (1 == noteceViewContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                if (1 == noteceViewContext.getPage().page)
                    lvList.setRefreshTime();
                if (entity.pageCount <= noteceViewContext.getPage().page) {
                    lvList.setPullLoadEnable(false);
                } else {
                    lvList.setPullLoadEnable(true);
                }
                mList.addAll(entity.entityList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
            lvList.stopRefresh();
            lvList.stopLoadMore();
        }
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
