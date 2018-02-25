package com.haitao.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DiscountExchangeActivity;
import com.haitao.connection.api.ForumApi;
import com.haitao.utils.ImageLoaderUtils;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.CurrenciesIfModelData;
import io.swagger.client.model.ExchangeRateModel;
import io.swagger.client.model.ExchangesRatesIfModelData;

/**
 * 汇率换算器
 */
public class DiscountExchangeDlg extends BottomSheetDialog {

    @BindView(R.id.ivLogo1)     CustomImageView mIvLogo1;
    @BindView(R.id.tvName1)     TextView        mTvName1;
    @BindView(R.id.tv_amount_1) TextView        mTvAmount1;
    @BindView(R.id.tvDesc1)     TextView        mTvDesc1;
    @BindView(R.id.rl_amount_1) RelativeLayout  mRlAmount1;
    @BindView(R.id.ivLogo2)     CustomImageView mIvLogo2;
    @BindView(R.id.tvName2)     TextView        mTvName2;
    @BindView(R.id.tv_amount_2) TextView        mTvAmount2;
    @BindView(R.id.tvDesc2)     TextView        mTvDesc2;
    @BindView(R.id.rl_amount_2) RelativeLayout  mRlAmount2;
    @BindView(R.id.ivLogo3)     CustomImageView mIvLogo3;
    @BindView(R.id.tvName3)     TextView        mTvName3;
    @BindView(R.id.tv_amount_3) TextView        mTvAmount3;
    @BindView(R.id.tvDesc3)     TextView        mTvDesc3;
    @BindView(R.id.rl_amount_3) RelativeLayout  mRlAmount3;

    @BindColor(R.color.orangeFF804D) int mOrange;
    @BindColor(R.color.grey1D1D1F)     int mGrey;

    private Context mContext;
    //    private EditText mTvAmount1, mTvAmount2, mTvAmount3;
    private String currency1 = "CNY", currency2 = "USD", currency3 = "EUR";
    private String from = currency1;
    private String to   = currency2 + "," + currency3;

    private ArrayList<ExchangeRateModel> exchangeList;
    //    private TextView                     mTvSource;
    //    private RelativeLayout               mRlAmount1, mRlAmount2, mRlAmount3;

    private String amount  = "1";
    private int    currPos = 0;
    private TextView[]    mInputTexts;
    private DecimalFormat mDf;

    public DiscountExchangeDlg(Context context) {
        super(context);
        this.mContext = context;
        initVars();
        initView();
        initData();
    }

    private void initVars() {
        // ##表示2位小数
        mDf = new DecimalFormat("0.####");
    }


    private void initView() {
        View view = View.inflate(mContext, R.layout.dialog_exchange, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mInputTexts = new TextView[]{mTvAmount1, mTvAmount2, mTvAmount3};

        View                parent   = (View) view.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        view.measure(0, 0);
        behavior.setPeekHeight(view.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
    }

    private void initData() {
        mTvName1.setText(currency1);
        mTvName2.setText(currency2);
        mTvName3.setText(currency3);
        exchangeList = new ArrayList<>();
        getExchangeResult();
    }


    private void getExchangeResult() {
        ForumApi.getInstance().commonExchangesRatesGet(from, to, response -> {
            ExchangesRatesIfModelData data = response.getData();
            if ("0".equals(response.getCode()) && null != data) {
                // 数据来源
                Logger.d(response);
//                mTvSource.setText(data.getSourceView());
                float sourceAmount = Float.parseFloat(amount.trim());
                // 汇率数据
                if (null != data.getExchangesRates()) {
                    exchangeList.clear();
                    exchangeList.addAll(data.getExchangesRates());
                    for (ExchangeRateModel exchangeRateModel : data.getExchangesRates()) {
                        if (currency1.equals(exchangeRateModel.getTo())) {
                            float rate = Float.parseFloat(exchangeRateModel.getRate());
                            mTvAmount1.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                            mTvDesc1.setText(exchangeRateModel.getToView());
                        }

                        if (currency2.equals(exchangeRateModel.getTo())) {
                            float rate = Float.parseFloat(exchangeRateModel.getRate());
                            mTvAmount2.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                            mTvDesc2.setText(exchangeRateModel.getToView());
                        }

                        if (currency3.equals(exchangeRateModel.getTo())) {
                            float rate = Float.parseFloat(exchangeRateModel.getRate());
                            mTvAmount3.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                            mTvDesc3.setText(exchangeRateModel.getToView());
                        }
                    }
                }
            }
        }, error -> {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTvAmount1.getWindowToken(), 0);
        });
    }


    public void show() {
        super.show();
    }


    public void setChooseData(CurrenciesIfModelData currenciesIfModelData, int position) {
        Logger.d("currPos = " + position);
        from = currenciesIfModelData.getCurrencyAbbr();
        switch (position) {
            case 0:
                currency1 = currenciesIfModelData.getCurrencyAbbr();
                mTvName1.setText(currenciesIfModelData.getCurrencyAbbr());
                ImageLoaderUtils.showOnlineImage(currenciesIfModelData.getCountryFlag(), mIvLogo1);
                mTvDesc1.setText(currenciesIfModelData.getCurrencyName());
                break;
            case 1:
                currency2 = currenciesIfModelData.getCurrencyAbbr();
                mTvName2.setText(currenciesIfModelData.getCurrencyAbbr());
                ImageLoaderUtils.showOnlineImage(currenciesIfModelData.getCountryFlag(), mIvLogo2);
                mTvDesc2.setText(currenciesIfModelData.getCurrencyName());
                break;
            case 2:
                currency3 = currenciesIfModelData.getCurrencyAbbr();
                mTvName3.setText(currenciesIfModelData.getCurrencyAbbr());
                ImageLoaderUtils.showOnlineImage(currenciesIfModelData.getCountryFlag(), mIvLogo3);
                mTvDesc3.setText(currenciesIfModelData.getCurrencyName());
                break;
        }
        refreshData();
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        switch (currPos) {
            case 0:
                from = currency1;
                to = currency2 + "," + currency3;
                break;
            case 1:
                from = currency2;
                to = currency1 + "," + currency3;
                break;
            case 2:
                from = currency3;
                to = currency1 + "," + currency2;
                break;
        }
        refreshTextColor();
        getExchangeResult();
    }

    /**
     * 动态设置货币字体颜色
     */
    private void refreshTextColor() {
        for (int i = 0; i < mInputTexts.length; i++) {
            mInputTexts[i].setTextColor(currPos == i ? mOrange : mGrey);
        }
    }

    /**
     * 计算金额
     */
    private void setInputText() {
        if (!TextUtils.isEmpty(amount)) {
            float sourceAmount = Float.parseFloat(amount.trim());
            for (ExchangeRateModel exchangeRateModel : exchangeList) {
                if (currency1.equals(exchangeRateModel.getTo())) {
                    float rate = Float.parseFloat(exchangeRateModel.getRate());
                    mTvAmount1.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                }

                if (currency2.equals(exchangeRateModel.getTo())) {
                    float rate = Float.parseFloat(exchangeRateModel.getRate());
                    mTvAmount2.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                }

                if (currency3.equals(exchangeRateModel.getTo())) {
                    float rate = Float.parseFloat(exchangeRateModel.getRate());
                    mTvAmount3.setText(String.valueOf(mDf.format(rate * sourceAmount)));
                }
            }
        }
        mInputTexts[currPos].setText(amount);
    }


    /**
     * 选择货币条目
     */
    @OnClick({R.id.iv_close, R.id.rl_amount_1, R.id.rl_amount_2, R.id.rl_amount_3})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                return;
            case R.id.rl_amount_1:
                currPos = 0;
                break;
            case R.id.rl_amount_2:
                currPos = 1;
                break;
            case R.id.rl_amount_3:
                currPos = 2;
                break;
            default:
                return;
        }
        amount = "1";
        mInputTexts[currPos].setText("1");
        refreshData();
    }


    /**
     * 更换货币种类
     */
    @OnClick({R.id.tvName1, R.id.tvName2, R.id.tvName3})
    public void onClickName(View view) {
        int    position      = 0;
        String currency_abbr = "";
        switch (view.getId()) {
            case R.id.tvName1:
                position = 0;
                currency_abbr = currency1;
                DiscountExchangeActivity.launch(mContext, currency_abbr, position);
                break;
            case R.id.tvName2:
                position = 1;
                currency_abbr = currency2;
                DiscountExchangeActivity.launch(mContext, currency_abbr, position);
                break;
            case R.id.tvName3:
                position = 2;
                currency_abbr = currency3;
                DiscountExchangeActivity.launch(mContext, currency_abbr, position);
                break;
        }
    }


    /**
     * 键盘输入
     */
    @OnClick({R.id.tv_one, R.id.tv_two, R.id.tv_three, R.id.tv_clear, R.id.tv_four, R.id.tv_five, R.id.tv_six, R.id.tv_seven, R.id.tv_eight, R.id.tv_nine, R.id.img_backspace, R.id.tv_zero, R.id.tv_point})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_zero:
                if (TextUtils.equals(amount, "0"))
                    break;
            case R.id.tv_one:
            case R.id.tv_two:
            case R.id.tv_three:
            case R.id.tv_four:
            case R.id.tv_five:
            case R.id.tv_six:
            case R.id.tv_seven:
            case R.id.tv_eight:
            case R.id.tv_nine:
                if (TextUtils.equals(amount, "0")) {
                    amount = "";
                }
                // 2位小数限制
                if (amount.contains(".")) {
                    String[] split = amount.split("\\.");
                    Logger.d("split data = " + Arrays.toString(split));
                    if (split.length < 2 || split.length >= 2 && split[1].length() < 2) {
                        amount += ((TextView) view).getText().toString();
                    }
                } else if (amount.length() < 9) {
                    amount += ((TextView) view).getText().toString();
                }
                setInputText();
                break;
            case R.id.tv_point:
                if (amount.charAt(amount.length() - 1) != '.' && !amount.contains(".")) {
                    amount += ((TextView) view).getText().toString();
                    setInputText();
                }
                break;
            case R.id.img_backspace:
                if (!TextUtils.isEmpty(amount) && amount.length() != 1) {
                    amount = amount.substring(0, amount.length() - 1);
                } else {
                    amount = "0";
                }
                setInputText();
                break;
            case R.id.tv_clear:
                amount = "0";
                setInputText();
                break;
        }
    }


    public static String formatCashNumber(String amount) {
        String result = amount;
        if (!TextUtils.isEmpty(amount)) {
            if (amount.contains(".")) {
                String[] split = amount.split("\\.");
                if (split.length >= 2 && split[1].length() > 4) {
                    result = split[0] + "." + split[1].substring(0, 4);
                }
            }
        }
        return result;
    }
}
