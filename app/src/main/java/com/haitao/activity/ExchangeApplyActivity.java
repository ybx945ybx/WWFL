package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.db.CityDB;
import com.haitao.db.ProvinceDB;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.ExchangeApplyObject;
import com.haitao.model.ExchangeObject;
import com.haitao.model.ProvinceObject;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ConfirmDialogUtils;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.view.wheel.ChangeAddressDialog;

import java.util.ArrayList;


/**
 * 兑换申请
 */
public class ExchangeApplyActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName, etPhone, etDetailAddress;
    private TextView tvSubmit, etAddress;
    private TextView tvPrice;

    private String name, phone, detailAddress, province = "", city = "";
    private ViewGroup layoutAddress;

    ChangeAddressDialog mChangeAddressDialog;
    ExchangeObject      sampleObject;

    protected IViewContext<ExchangeApplyObject, IEntityService<ExchangeApplyObject>> commandViewContext = VF.<ExchangeApplyObject>getDefault(ExchangeApplyObject.class);
    protected IViewContext<ProvinceObject, IEntityService<ProvinceObject>>           areaViewContext    = VF.<ProvinceObject>getDefault(ProvinceObject.class);

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, ExchangeObject object) {
        Intent intent = new Intent(context, ExchangeApplyActivity.class);
        intent.putExtra(TransConstant.OBJECT, object);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_apply);
        if (null != getIntent() && getIntent().hasExtra(TransConstant.OBJECT)) {
            sampleObject = (ExchangeObject) getIntent().getSerializableExtra(TransConstant.OBJECT);
        }
        TAG = "兑换申请";
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.exchange_title);
        etName = getView(R.id.etName);
        etPhone = getView(R.id.etPhone);
        etDetailAddress = getView(R.id.etDetailAddress);
        tvSubmit = getView(R.id.tvSubmit);
        tvSubmit.setEnabled(false);
        etAddress = getView(R.id.etAddress);
        layoutAddress = getView(R.id.layoutAddress);
        tvPrice = getView(R.id.tvPrice);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvSubmit.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        layoutAddress.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim();
                initEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString().trim();
                initEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDetailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detailAddress = s.toString().trim();
                initEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initEmpty() {
        tvSubmit.setEnabled(!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(phone) &&
                !TextUtils.isEmpty(province) &&
                !TextUtils.isEmpty(city) &&
                !TextUtils.isEmpty(detailAddress));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String       price      = String.format("您当前还有 {%s} 金币，本次出价 {%s} 金币，确定出价吗？", UserManager.getInstance().getUser().gold, sampleObject.prices);
        CharSequence charsPrice = ColorPhrase.from(price).withSeparator("{}").innerColor(mContext.getResources().getColor(R.color.midOrange)).outerColor(mContext.getResources().getColor(R.color.midGrey)).format();
        tvPrice.setText(charsPrice);
        loadAreaData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                upload();
                break;
            case R.id.etAddress:
            case R.id.layoutAddress:
                mChangeAddressDialog = new ChangeAddressDialog(mContext);
                mChangeAddressDialog.setAddress(province, city);
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String provinceStr, String cityStr) {
                                province = provinceStr;
                                city = cityStr;
                                etAddress.setText(province + " " + city);
                                initEmpty();
                            }
                        });
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = mChangeAddressDialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                mChangeAddressDialog.getWindow().setAttributes(lp);
                break;
        }
    }

    private void upload() {
        commandViewContext.getEntity().tid = sampleObject.tid;
        commandViewContext.getEntity().revicename = name;
        commandViewContext.getEntity().mobile = phone;
        commandViewContext.getEntity().address = province + city;
        commandViewContext.getEntity().info = detailAddress;
        ProgressDialogUtils.show(mContext, R.string.operationg);
        commandViewContext.getService().asynFunction(MethodConstant.EXCHANGE_APPLY, commandViewContext.getEntity(), new IAsynServiceHandler<ExchangeApplyObject>() {
            @Override
            public void onSuccess(ExchangeApplyObject entity) throws Exception {
                ProgressDialogUtils.dismiss();
                ConfirmDialogUtils dialogUtils = new ConfirmDialogUtils(mContext);
                TextView           textView    = new TextView(mContext);
                Drawable           drawable    = mContext.getResources().getDrawable(R.mipmap.ic_check);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
                textView.setText("出价成功，系统将冻结您相应的金币");
                textView.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.px15));
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.shape_white_solid_rectangle);
                textView.setTextColor(mContext.getResources().getColor(R.color.midGrey));
                textView.setPadding(15, 15, 15, 15);
                textView.setTextSize(14);
                dialogUtils.setView(textView);
                dialogUtils.setBtnVisibility(false);
                dialogUtils.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                            setResult(TransConstant.REFRESH);
                            ((Activity) mContext).finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

            }

            @Override
            public void onSuccessPage(PageResult<ExchangeApplyObject> entity) throws Exception {

            }

            @Override
            public void onFailed(String error) {
                ToastUtils.show(mContext, error);
                ProgressDialogUtils.dismiss();
            }
        });
    }

    private void loadAreaData() {
        ArrayList<ProvinceObject> proList = ProvinceDB.getList();
        if (null == proList || proList.size() <= 0) {
            areaViewContext.asynQuery(MethodConstant.AREA, areaViewContext.getEntity(), new IAsynServiceHandler<ProvinceObject>() {
                @Override
                public void onSuccess(ProvinceObject entity) throws Exception {

                }

                @Override
                public void onSuccessPage(PageResult<ProvinceObject> entity) throws Exception {
                    if (null == entity || null == entity.entityList)
                        return;
                    ProvinceDB.add(entity.entityList);
                    for (ProvinceObject obj : entity.entityList) {
                        CityDB.add(obj.city);
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }

}
