package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.event.ProviceSelectEvent;
import com.haitao.utils.DensityUtil;
import com.haitao.utils.ScreenUtils;
import com.haitao.view.HtHeadView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线下店铺省份筛选界面
 */
public class UnionPayShopAddressFilterActivity extends BaseActivity {

    @BindView(R.id.ht_headview)     HtHeadView    htHeadView;
    @BindView(R.id.tag_flow_layout) TagFlowLayout tagFlowLayout;

    private TagAdapter<String> mAdapter;
    private ArrayList<String>  provinceStrings;      // 省份列表
    private int                selectPos;            // 当前选中位置
    private int                minWidth;
    private String             mStoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay_shop_address_filter);
        ButterKnife.bind(this);

        initVars();
        initView();
        initEvent();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent) {
            mStoreName = getIntent().getStringExtra(TransConstant.TITLE);
            provinceStrings = getIntent().getStringArrayListExtra(TransConstant.VALUE);
            selectPos = getIntent().getIntExtra(TransConstant.POSITION, 0);
        }
        minWidth = (ScreenUtils.getScreenWidth(this) - DensityUtil.dip2px(this, 58)) / 3;

    }

    private void initView() {
        htHeadView.setCenterText(mStoreName);
        mAdapter = new TagAdapter<String>(provinceStrings) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                View     tag    = LayoutInflater.from(mContext).inflate(R.layout.union_pay_area_item, parent, false);
                TextView tvArea = tag.findViewById(R.id.tv_area_name);
                tvArea.setMinWidth(minWidth);
                tvArea.setText(o);
                tvArea.setSelected(position == selectPos);

                return tag;
            }
        };
        tagFlowLayout.setAdapter(mAdapter);
    }

    private void initEvent() {
        tagFlowLayout.setOnTagClickListener((view, position, parent) -> {
            selectPos = position;
            mAdapter.notifyDataChanged();
            EventBus.getDefault().post(new ProviceSelectEvent(position));
            finish();
            return false;
        });
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, ArrayList<String> provincelist, int currentPos, String name) {
        Intent intent = new Intent(context, UnionPayShopAddressFilterActivity.class);
        intent.putStringArrayListExtra(TransConstant.VALUE, provincelist);
        intent.putExtra(TransConstant.POSITION, currentPos);
        intent.putExtra(TransConstant.TITLE, name);
        context.startActivity(intent);
    }
}
