package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.framework.utils.DeviceUtil;
import com.haitao.utils.calendar.CustomDate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tvVersionName) TextView mTvVersionName;  // 版本名
    @BindView(R.id.tvCopyRight)   TextView mTvCopyRight;    // 版权说明

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initVars();
        initData();
    }

    private void initVars() {
        TAG = "关于";
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTvVersionName.setText("V" + DeviceUtil.getSoftWareVersion(mContext));
        CustomDate date = new CustomDate();
        mTvCopyRight.setText(String.format(getString(R.string.about_copy), String.valueOf(date.year)) + "\n违法和不良信息举报电话：021-61910511 转 8012");
    }
}
