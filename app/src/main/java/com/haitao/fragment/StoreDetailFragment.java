package com.haitao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.swagger.client.model.StoreDetailModel;

public class StoreDetailFragment extends BaseFragment {

    @BindView(R.id.tvDescription) TextView tvDescription;                           // 商家详情

    @BindView(R.id.llyt_pay)          LinearLayout llytPay;                         // 支持的支付
    @BindView(R.id.tv_support_card)   TextView     tvSupportCard;                   // 双币信用卡
    @BindView(R.id.tv_support_paypal) TextView     tvSupportPaypal;                 // PayPal
    @BindView(R.id.tv_support_alipay) TextView     tvSupportAlipay;                 // 支付宝
    @BindView(R.id.tv_support_weixin) TextView     tvSupportWeixin;                 // 微信
    @BindView(R.id.view_pay)          View         viewPay;                         // 分割线

    @BindView(R.id.llyt_deliver)      LinearLayout llytDeliver;                     // 支持的配送
    @BindView(R.id.tv_support_direct) TextView     tvSupportDirect;                 // 直邮
    @BindView(R.id.tv_support_trans)  TextView     tvSupportTrans;                  // 转运
    @BindView(R.id.view_deliver)      View         viewDeliver;                     // 分割线

    @BindView(R.id.llyt_chinese)               LinearLayout llytChinese;            // 支持的中文模式
    @BindView(R.id.tv_support_chinese_address) TextView     tvSupportCnAddress;     // 中文地址
    @BindView(R.id.tv_support_chinese_service) TextView     tvSupportCnService;     // 中文客服
    @BindView(R.id.tv_support_chinese_page)    TextView     tvSupportCnPage;        // 中文页面
    @BindView(R.id.view_chinese)               View         viewCn;                 // 分割线

    @BindView(R.id.llyt_visit)       LinearLayout llytVisit;                        // 支持的访问
    @BindView(R.id.tv_support_visit) TextView     tvSupportvisit;                   // 大陆地区访问受限

    @BindView(R.id.llytRebateContent) LinearLayout llytRebateContent;               // 返利说明版块
    @BindView(R.id.tvRebateContent)   TextView     tvRebateContent;                 // 返利说明

    private Unbinder mUnbinder;

    private StoreDetailModel storeObject;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail, null);
        mUnbinder = ButterKnife.bind(this, view);
        initVars();
        initView();
        return view;
    }

    private void initVars() {
        TAG = "商家详情 - 详情";
        if (null != getArguments()) {
            Bundle bundle = getArguments();
            storeObject = (StoreDetailModel) bundle.getSerializable(TransConstant.OBJECT);
        }
    }

    private void initView() {
        if (null != storeObject) {
            // 商家详情
            tvDescription.setText(storeObject.getStoreDescription());

            // 支持版块
            // 双币信用卡
            if ("1".equals(storeObject.getCardSupported())) {
                llytPay.setVisibility(View.VISIBLE);
                tvSupportCard.setVisibility(View.VISIBLE);
            }
            // paypal国际版
            if ("1".equals(storeObject.getPaypalSupported())) {
                llytPay.setVisibility(View.VISIBLE);
                tvSupportPaypal.setVisibility(View.VISIBLE);
            }
            // 支付宝
            if ("1".equals(storeObject.getAlipaySupported())) {
                llytPay.setVisibility(View.VISIBLE);
                tvSupportAlipay.setVisibility(View.VISIBLE);
            }
            // 微信
            if ("1".equals(storeObject.getWechatPaymentSupported())) {
                llytPay.setVisibility(View.VISIBLE);
                tvSupportWeixin.setVisibility(View.VISIBLE);
            }

            // 直邮中国
            if ("1".equals(storeObject.getDirectPostSupported())) {
                llytDeliver.setVisibility(View.VISIBLE);
                tvSupportDirect.setVisibility(View.VISIBLE);
            }
            // 转运地址
            if ("1".equals(storeObject.getTransshippingSupported())) {
                llytDeliver.setVisibility(View.VISIBLE);
                tvSupportTrans.setVisibility(View.VISIBLE);
            }

            // 中文收货地址
            if ("1".equals(storeObject.getCnShippingaddrSupported())) {
                llytChinese.setVisibility(View.VISIBLE);
                tvSupportCnAddress.setVisibility(View.VISIBLE);
            }
            // 中文客服
            if ("1".equals(storeObject.getCnServiceSupported())) {
                llytChinese.setVisibility(View.VISIBLE);
                tvSupportCnService.setVisibility(View.VISIBLE);
            }
            // 中文页面
            if ("1".equals(storeObject.getCnWebSupported())) {
                llytChinese.setVisibility(View.VISIBLE);
                tvSupportCnPage.setVisibility(View.VISIBLE);
            }

            // 大陆地区访问受限
            if ("1".equals(storeObject.getBoundedAccessing())) {
                llytVisit.setVisibility(View.VISIBLE);
                tvSupportvisit.setVisibility(View.VISIBLE);
            }

            // 分割线显示
            if (llytVisit.getVisibility() == View.GONE){
                if (llytChinese.getVisibility() == View.VISIBLE){
                    viewCn.setVisibility(View.GONE);
                } else if (llytDeliver.getVisibility() == View.VISIBLE){
                    viewDeliver.setVisibility(View.GONE);
                } else {
                    viewPay.setVisibility(View.GONE);
                }
            }

            // 返利说明
            if ("1".equals(storeObject.getHasRebate())) {
                llytRebateContent.setVisibility(View.VISIBLE);
                String cashDesc = storeObject.getRebateInstruction();
                cashDesc = cashDesc.replaceAll("\\*", "• ");
                tvRebateContent.setText(cashDesc);
            } else {
                llytRebateContent.setVisibility(View.GONE);
            }
        }
    }


    //    public void initData() {
    //        if (null != storeObject) {
    //            tvDescription.setText(storeObject.getStoreDescription());
    //            ArrayList<SupportObject> supportList = new ArrayList<>();
    //            // 以下顺序固定
    //            // 双币信用卡
    //            if ("1".equals(storeObject.getCardSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support1);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_card);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 支付宝
    //            if ("1".equals(storeObject.getAlipaySupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support2);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_alipay);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // paypal国际版
    //            if ("1".equals(storeObject.getPaypalSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support3);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_paypal);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 直邮中国
    //            if ("1".equals(storeObject.getDirectPostSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support4);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_direct_post);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 转运地址
    //            if ("1".equals(storeObject.getTransshippingSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support5);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_transshipping);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 中文页面
    //            if ("1".equals(storeObject.getCnWebSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support6);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_cn_web);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 中文收货地址
    //            if ("1".equals(storeObject.getCnShippingaddrSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support7);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_cn_address);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 中文客服
    //            if ("1".equals(storeObject.getCnServiceSupported())) {
    //                SupportObject supportObject = new SupportObject();
    //                supportObject.support = getResources().getString(R.string.store_support8);
    //                Drawable drawable_n = getResources().getDrawable(R.mipmap.ic_store_cn_service);
    //                drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
    //                supportObject.drawable = drawable_n;
    //                supportList.add(supportObject);
    //            }
    //            // 拿到返利人数
    //            if ("1".equals(storeObject.getHasRebate())) {
    //                llytSupportBottom.setVisibility(View.VISIBLE);
    //                tvObtainRebate.setVisibility(View.VISIBLE);
    //                tvObtainRebate.setText(String.format(getResources().getString(R.string.store_rebate_account), storeObject.getRebateInfluenceView()));
    //
    //            }
    //            // 大陆地区访问受限
    //            if ("1".equals(storeObject.getBoundedAccessing())) {
    //                llytSupportBottom.setVisibility(View.VISIBLE);
    //                tvLimit.setVisibility(View.VISIBLE);
    //
    //            }
    //            if (supportList.size() > 0) {
    //                rycvSupport.setVisibility(View.VISIBLE);
    //                RVBaseAdapter<SupportObject> mAdapter = new RVBaseAdapter<SupportObject>(mContext, supportList, R.layout.store_support_item) {
    //                    @Override
    //                    public void bindView(RVBaseHolder holder, SupportObject supportObject) {
    //                        TextView tvSupport = holder.getView(R.id.tv_support);
    //                        tvSupport.setText(supportObject.support);
    //                        tvSupport.setCompoundDrawables(supportObject.drawable, null, null, null);
    //
    //                    }
    //                };
    //                rycvSupport.setAdapter(mAdapter);
    //            }
    //            if ("1".equals(storeObject.getHasRebate())) {
    //                llytRebateContent.setVisibility(View.VISIBLE);
    //                String cashDesc = storeObject.getRebateInstruction();
    //                cashDesc = cashDesc.replaceAll("\\*", "• ");
    //                //                    cashDesc = cashDesc.replaceAll("\\*", "{• }");
    //                //                    CharSequence formatted = ColorPhrase.from(cashDesc)
    //                //                            .withSeparator("{}")
    //                //                            .innerColor(mContext.getResources().getColor(R.color.grey29292C))
    //                //                            .outerColor(mContext.getResources().getColor(R.color.grey29292C))
    //                //                            .format();
    //                tvRebateContent.setText(cashDesc);
    //            } else {
    //                llytRebateContent.setVisibility(View.GONE);
    //            }
    //        }
    //    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
