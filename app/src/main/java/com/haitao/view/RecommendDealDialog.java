package com.haitao.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.DiscountDetailActivity;
import com.haitao.adapter.ProductRecommendAdapter;

import java.util.ArrayList;

import io.swagger.client.model.DealModel;

/**
 * 去购买页面 相关优惠弹窗
 */
public class RecommendDealDialog extends BottomSheetDialog {
    private Context              mContext;
    private ArrayList<DealModel> mList;

    public RecommendDealDialog(Context context, ArrayList<DealModel> mList) {
        super(context);
        mContext = context;
        this.mList = mList;
        initDlg();
    }

    private void initDlg() {
        View                 parentView  = View.inflate(mContext, R.layout.dialog_recommend_deal, null);
        ImageView            ivClose     = parentView.findViewById(R.id.iv_close);
        HorizontalScrollView horDiscount = parentView.findViewById(R.id.layoutDiscountScroll);
        GridView             gvList      = parentView.findViewById(R.id.gv_order_pics);
        TextView             tvEmpty     = parentView.findViewById(R.id.tv_empty);

        if (mList != null && mList.size() > 0) {
            horDiscount.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            ProductRecommendAdapter mAdapter = new ProductRecommendAdapter(mContext, mList);

            int            size          = mList.size();
            int            length        = 150;
            DisplayMetrics dm            = mContext.getResources().getDisplayMetrics();
            float          density       = dm.density;
            int            gridviewWidth = (int) ((size * (length)) * density);
            int            itemWidth     = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            gvList.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            gvList.setColumnWidth(itemWidth); // 设置列表项宽
            gvList.setHorizontalSpacing(0); // 设置列表项水平间距
            gvList.setStretchMode(GridView.NO_STRETCH);

            gvList.setNumColumns(size); // 设置列数量=列表集合数
            gvList.setAdapter(mAdapter);

            gvList.setOnItemClickListener((adapterView, view, i, l) -> DiscountDetailActivity.launch(mContext, mList.get(i).getDealId()));
        } else {
            horDiscount.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }

        // 关闭按钮
        ivClose.setOnClickListener(v -> dismiss());

        setContentView(parentView);
    }

}
