package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.MessageAdapter;
import com.haitao.common.Constant.PageConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.event.FriendStatChangeEvent;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.NoticeObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.HtHeadView;
import com.haitao.view.RoundedImageView;
import com.haitao.view.refresh.XListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.client.model.ChatMsgModel;

/**
 * 私信
 * Created by penley on 16/3/8.
 */
public class NoticeMessageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout          layoutComment;
    private XListView               lvList;
    private ArrayList<ChatMsgModel> mList;
    private HtHeadView              mHtHeadView;
    private MessageAdapter          mAdapter;
    private TextView                btnCommint;
    private EditText                edCommit;
    private View                    friendRequestView;
    private RoundedImageView        ivAvator;
    private TextView                tvRequestType;
    private TextView                tvDesc;
    private String id     = "";
    private String title  = "";
    private String lastId = "";
    private int    page   = 1;
    private String isFriend;
    private String ivAvatorUrl;

    protected IViewContext<NoticeObject, IEntityService<NoticeObject>> commandViewContext = VF.<NoticeObject>getDefault(NoticeObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, String title) {
        Intent intent = new Intent(context, NoticeMessageActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String isFriend, String ivAvatorUrl, String id, String title) {
        Intent intent = new Intent(context, NoticeMessageActivity.class);
        intent.putExtra(TransConstant.TITLE, title);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra(TransConstant.STATUS, isFriend);
        intent.putExtra(TransConstant.VALUE, ivAvatorUrl);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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
        TAG = "私信";
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra(TransConstant.TITLE);
            id = intent.getStringExtra(TransConstant.ID);
            isFriend = intent.getStringExtra(TransConstant.STATUS);
            ivAvatorUrl = intent.getStringExtra(TransConstant.VALUE);
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initTop();
        initError();
        addRequestHeadView();
        lvList = getView(R.id.lvList);
        lvList.setPullLoadEnable(false);
        layoutComment = getView(R.id.layoutComment);
        btnCommint = getView(R.id.btnCommint);
        edCommit = getView(R.id.edCommit);
        mHtHeadView = getView(R.id.ht_headview);
        mHtHeadView.setCenterText(title);

        layoutComment.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnCommint.setOnClickListener(this);
        lvList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page++;
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        lvList.setOnItemClickListener((parent, view, position, id1) -> {

        });

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
        btnRefresh.setOnClickListener(v -> getData());
    }

    private void checkSubmitEnable(String content) {
        btnCommint.setEnabled(!TextUtils.isEmpty(content));
        btnCommint.setTextColor(getResources().getColor(btnCommint.isEnabled() ? R.color.orangeFF804D : R.color.lightGrey));
    }


    /**
     * 初始化数据
     */
    private void initData() {
        checkSubmitEnable("");
        mList = new ArrayList<ChatMsgModel>();
        lvList.setAutoLoadEnable(true);
        lvList.setPullLoadEnable(false);
        mAdapter = new MessageAdapter(mContext, mList);
        lvList.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        ForumApi.getInstance().userInteractionUserIdMsgsListGet(id, String.valueOf(page), String.valueOf(PageConstant.pageSize),
                response -> {
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                    if ("0".equals(response.getCode())) {
                        if (1 == page) {
                            mList.clear();
                        }
                        int lastPostion = 0;
                        if (null != response.getData().getRows() && response.getData().getRows().size() > 0) {
                            List<ChatMsgModel> list = response.getData().getRows();
                            lastPostion = list.size();
                            Collections.reverse(list);
                            mList.addAll(0, list);
                            lvList.setPullRefreshEnable(response.getData().getRows().size() >= PageConstant.pageSize);
                        } else {
                            lvList.setPullRefreshEnable(false);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (1 == page) {
                            lvList.setSelection(lvList.getBottom());
                        } else {
                            lvList.setSelection(lastPostion);
                        }
                    }
                    if (mList.isEmpty()) {
                        ll_common_error.setVisibility(View.VISIBLE);
                        setErrorType(0);
                    } else {
                        ll_common_error.setVisibility(View.GONE);
                    }
                }, error -> {
                    lvList.stopRefresh();
                    lvList.stopLoadMore();
                });
        /*commandViewContext.getPage().page = 1;
        commandViewContext.getEntity().type = noticeObject.type;
        commandViewContext.getEntity().type_id = noticeObject.type_id;
        commandViewContext.asynQuery(MethodConstant.PM_BYTYPE, commandViewContext.getEntity(), new responseHandler());*/
    }

    private void addRequestHeadView() {
        if (!TextUtils.isEmpty(isFriend)) {
            friendRequestView = getView(R.id.friend_request);
            ivAvator = getView(R.id.iv_avator);
            tvRequestType = getView(R.id.tv_request_type);
            tvDesc = getView(R.id.tv_desc);
            tvRequestType.setOnClickListener(this);

            switch (isFriend) {
                case "1":
                case "2":
                    friendRequestView.setVisibility(View.GONE);
                    break;
                case "0":   // 显示添加对方为好友
                    friendRequestView.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.showOnlineImage(ivAvatorUrl, ivAvator, R.mipmap.ic_default_avator);
                    tvDesc.setText(R.string.not_friend);
                    tvRequestType.setText(R.string.add_friend);
                    break;
                case "3":   // 等待对方验证
                    friendRequestView.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.showOnlineImage(ivAvatorUrl, ivAvator, R.mipmap.ic_default_avator);
                    tvDesc.setText(R.string.added_friend);
                    tvRequestType.setText(R.string.pending_accept_friend);
                    tvRequestType.setEnabled(false);
                    break;
                case "4":   // 同意对方的好友请求
                    friendRequestView.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.showOnlineImage(ivAvatorUrl, ivAvator, R.mipmap.ic_default_avator);
                    tvDesc.setText(R.string.add_you_as_friend);
                    tvRequestType.setText(" 同 意 ");
                    break;
            }
        }
    }

    private void loadNext() {
        /*commandViewContext.getEntity().type = noticeObject.type;
        commandViewContext.getEntity().type_id = noticeObject.type_id;
        commandViewContext.asynQueryNext(MethodConstant.PM_BYTYPE, commandViewContext.getEntity(), new responseHandler());*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCommint://回复
                if (TextUtils.isEmpty(edCommit.getText().toString().trim())) {
                    ToastUtils.show(mContext, R.string.post_content_tips);
                    return;
                }
                InputMethodManager imm = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            0);
                }
                updateMessage();
                break;
            case R.id.tv_request_type:
                if ("0".equals(isFriend)) {   // 加对方好友
                    addFriend();
                } else if ("4".equals(isFriend)) {   // 对方加我好友
                    operateRequest();
                }
                break;
        }
    }

    /**
     * 请求添加对方为好友
     */
    private void addFriend() {
        ForumApi.getInstance().userFriendFriendUidPost(id,
                response -> {
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "好友请求已发送，请等待对方验证");
                        EventBus.getDefault().post(new FriendStatChangeEvent("3"));

                        tvRequestType.setText(R.string.pending_accept_friend);
                        tvDesc.setText(R.string.added_friend);
                        tvRequestType.setEnabled(false);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {

                });
    }

    /**
     * 接收对方的好友请求
     */
    private void operateRequest() {
        ForumApi.getInstance().userFriendFriendUidRequestingPut(id, "1",
                response -> {
                    if ("0".equals(response.getCode())) {
                        friendRequestView.setVisibility(View.GONE);
                        EventBus.getDefault().post(new FriendStatChangeEvent("1"));

                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {

                });
    }

    /*class responseHandler implements IAsynServiceHandler<NoticeObject> {

        @Override
        public void onSuccess(NoticeObject entity) throws Exception {

        }

        @Override
        public void onSuccessPage(PageResult<NoticeObject> entity) throws Exception {
            lvList.stopRefresh();
            lvList.stopLoadMore();
            lvList.setVisibility(View.VISIBLE);
            FLog.e("HTTPUtil",entity.page+"===");
            if (1 == commandViewContext.getPage().page)
                mList.clear();
            if (null != entity && null != entity.entityList) {
                mList.addAll(0, entity.entityList);
                mAdapter.notifyDataSetChanged();
               *//* if (isFirst) {
                    lvList.setSelection(lvList.getBottom());
                    isFirst = false;
                }else {
                    lvList.setSelection(entity.entityList.size());
                }*//*

                if (1 == commandViewContext.getPage().page) {
                    lvList.setRefreshTime();
                    lvList.setSelection(lvList.getBottom());
                }else {
                    lvList.setSelection(entity.entityList.size());
                }
                if (entity.pageCount > commandViewContext.getPage().page) {
                    lvList.setPullRefreshEnable(true);
                }else {
                    lvList.setPullRefreshEnable(false);
                }
                layoutComment.setVisibility(View.VISIBLE);
                if (mList.isEmpty()) {
                    ll_common_error.setVisibility(View.VISIBLE);
                    setErrorType(0);
                } else {
                    ll_common_error.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailed(String error) {
            lvList.stopRefresh();
            lvList.stopLoadMore();
            if (mList.isEmpty()) {
                ll_common_error.setVisibility(View.VISIBLE);
                layoutComment.setVisibility(View.GONE);
                setErrorType(1);
            } else {
                ll_common_error.setVisibility(View.GONE);
                layoutComment.setVisibility(View.VISIBLE);
            }
        }
    }
*/
    private void updateMessage() {
        ProgressDialogUtils.show(mContext, "发送中……");
        ForumApi.getInstance().userUserIdMessageSendingPost(id, edCommit.getText().toString().trim(),
                response -> {
                    ProgressDialogUtils.dismiss();
                    edCommit.setText("");
                    if ("0".equals(response.getCode())) {
                        page = 1;
                        getData();
                    }
                }, error -> ProgressDialogUtils.dismiss());
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
