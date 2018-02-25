package com.haitao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsCompanyObject;
import com.haitao.model.TransportCommentObject;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;

import io.swagger.client.model.OrderModel;

/**
 * 转运评价
 */
public class TransportCommentActivity extends BaseActivity implements View.OnClickListener {

    private ViewGroup layoutOrder;
    private TextView  tvOrder;
    private EditText  etComment;
    private RatingBar rbStar;

    private LogisticsCompanyObject obj;

    private OrderModel mOrderModel;

    private String content = "", star = "";

    protected IViewContext<TransportCommentObject, IEntityService<TransportCommentObject>> commandViewContext = VF.<TransportCommentObject>getDefault(TransportCommentObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Activity context, LogisticsCompanyObject object) {
        Intent intent = new Intent(context, TransportCommentActivity.class);
        intent.putExtra(TransConstant.OBJECT, object);
        context.startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_comment);
        TAG = "转运评价";
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
        tvTitle.setText("评价转运");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.transport_comment_submit);
        layoutOrder = getView(R.id.layoutOrder);
        tvOrder = getView(R.id.tvOrder);
        etComment = getView(R.id.etComment);
        rbStar = getView(R.id.rbStar);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvRight.setOnClickListener(this);
        layoutOrder.setOnClickListener(this);
        rbStar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating < 1) {
                rbStar.setRating(1f);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (null != getIntent() && getIntent().hasExtra(TransConstant.OBJECT)) {
            obj = (LogisticsCompanyObject) getIntent().getSerializableExtra(TransConstant.OBJECT);
        }
    }

    private void sendComment() {
        commandViewContext.getEntity().id = obj.id;
        commandViewContext.getEntity().score = star;
        commandViewContext.getEntity().orders = mOrderModel.getOrderNumber();
        commandViewContext.getEntity().contents = content;
        ProgressDialogUtils.show(mContext, "正在提交……");
        commandViewContext.getService().asynFunction(MethodConstant.TRANSPORT_COMMENT_ADD, commandViewContext.getEntity(), new IAsynServiceHandler<TransportCommentObject>() {
            @Override
            public void onSuccess(TransportCommentObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                ToastUtils.show(mContext, "评论成功,请等待审核");
                setResult(TransConstant.REFRESH);
                finish();
            }

            @Override
            public void onSuccessPage(PageResult<TransportCommentObject> entity) throws Exception {

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
            case R.id.layoutOrder:
                // 选择订单
                LogisticsOrderActivity.launch(mContext, obj.id);
                break;
            case R.id.tvRight:
                // 提交评价
                content = etComment.getText().toString().trim();
                star = String.valueOf(rbStar.getRating());
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(mContext, "请输入评价内容");
                    return;
                }
                if (null == mOrderModel) {
                    ToastUtils.show(mContext, "请选择订单");
                    return;
                }
                sendComment();
                break;
            default:
                break;
        }
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
        } else if (requestCode == resultCode && resultCode == TransConstant.REFRESH && null != data) {
            mOrderModel = (OrderModel) data.getSerializableExtra(TransConstant.OBJECT);
            tvOrder.setText(mOrderModel.getOrderNumber());
        }
    }
}
