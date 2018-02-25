package com.haitao.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.CountryCodeChooseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.swagger.client.model.AreaModel;

/**
 * 选择国家/地区 dialog
 *
 * @author 陶声
 * @since 2017-11-16
 */

public class CountryCodeChooseDlg extends BottomSheetDialog {

    @BindView(R.id.ib_close)   ImageButton mIbClose;    // 关闭
    @BindView(R.id.lv_content) ListView    mLvContent;  // 国家区号列表

    private List<AreaModel>          mCountryList; // 国家区号列表数据
    private CountryCodeChooseAdapter mAdapter;
    private String                   mSelectId;

    public CountryCodeChooseDlg(@NonNull Context context, List<AreaModel> countryList) {
        super(context);
        mCountryList = countryList;
        initDlg(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDlg(final Context context) {
        View layout = View.inflate(context, R.layout.dlg_country_code_choose, null);
        setContentView(layout);
        ButterKnife.bind(this, layout);
        mAdapter = new CountryCodeChooseAdapter(context, mCountryList);
        /*mLvContent.setAutoLoadEnable(false);
        mLvContent.setPullLoadEnable(false);
        mLvContent.setPullRefreshEnable(false);*/
        mLvContent.setAdapter(mAdapter);
        mLvContent.setOnTouchListener((v, event) -> {
            if (!mLvContent.canScrollVertically(-1)) {
                mLvContent.requestDisallowInterceptTouchEvent(false);
            } else {
                mLvContent.requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
    }

    /**
     * 关闭选择框
     */
    @OnClick(R.id.ib_close)
    public void onClickClose() {
        //        dismiss();
        hide();
    }

    public interface OnCountryCodeSelectedListener {
        void onSelect(AreaModel areaModel, CountryCodeChooseDlg dialog);
    }

    public void setOnCountryCodeSelectedListener(OnCountryCodeSelectedListener listener) {
        if (listener != null) {
            mLvContent.setOnItemClickListener((parent, view, position, id) -> {
                int index = position - mLvContent.getHeaderViewsCount();
                if (index >= 0 ) {
                    AreaModel areaModel = mCountryList.get(index);
                    if (areaModel != null) {
                        listener.onSelect(areaModel, this);
                        mAdapter.selectId = areaModel.getAreaId();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void setSelectId(String selectId) {
        mSelectId = selectId;
        mAdapter.selectId = mSelectId;
        mAdapter.notifyDataSetChanged();
    }
}
