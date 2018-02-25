package com.haitao.view.wheel;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.CityAdapter;
import com.haitao.adapter.ProvinceAdapter;
import com.haitao.db.CityDB;
import com.haitao.db.ProvinceDB;
import com.haitao.model.CityObject;
import com.haitao.model.ProvinceObject;
import com.haitao.view.wheel.adapters.AbstractWheelTextAdapter;
import com.haitao.view.wheel.views.OnWheelChangedListener;
import com.haitao.view.wheel.views.OnWheelScrollListener;
import com.haitao.view.wheel.views.WheelView;

import java.util.ArrayList;


/**
 * 更改封面对话框
 *
 * @author ywl
 */
public class ChangeAddressDialog extends Dialog implements View.OnClickListener {
    private Context   mContext;
    private WheelView wvProvince;
    private WheelView wvCitys;
    private View      lyChangeAddress;
    private View      lyChangeAddressChild;
    private TextView  btnSure;
    private TextView  btnCancel;

    private Context context;

    private ArrayList<ProvinceObject> arrProvinces = new ArrayList<ProvinceObject>();
    private ArrayList<CityObject>     arrCitys     = new ArrayList<CityObject>();
    private ProvinceAdapter provinceAdapter;
    private CityAdapter     cityAdapter;

    private String strProvince = "北京";
    private String strCity     = "北京";
    private OnAddressCListener onAddressCListener;

    private int maxsize = 16;
    private int minsize = 12;

    public ChangeAddressDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_changeaddress);

        wvProvince = (WheelView) findViewById(R.id.wv_address_province);
        wvCitys = (WheelView) findViewById(R.id.wv_address_city);
        lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
        lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

        lyChangeAddress.setOnClickListener(this);
        lyChangeAddressChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        initDatas();
        initProvinces();
        int currentProvince = getProvinceItem(strProvince);
        provinceAdapter = new ProvinceAdapter(context, arrProvinces, currentProvince, maxsize, minsize);
        provinceAdapter.normalColor = context.getResources().getColor(R.color.lightGrey);
        provinceAdapter.currentColor = context.getResources().getColor(R.color.darkGrey);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(currentProvince);

        initCitys(null != arrProvinces && arrProvinces.size() > 0 ? arrProvinces.get(currentProvince).id : "");
        int currentCity = getCityItem(strCity);
        cityAdapter = new CityAdapter(context, arrCitys, currentCity, maxsize, minsize);
        cityAdapter.normalColor = context.getResources().getColor(R.color.lightGrey);
        cityAdapter.currentColor = context.getResources().getColor(R.color.darkGrey);
        wvCitys.setVisibleItems(5);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(currentCity);

        wvProvince.addChangingListener((wheel, oldValue, newValue) -> {
            String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
            strProvince = currentText;
            setTextviewSize(currentText, provinceAdapter);
            initCitys(arrProvinces.get(wheel.getCurrentItem()).id);
            cityAdapter = new CityAdapter(context, arrCitys, 0, maxsize, minsize);
            cityAdapter.normalColor = context.getResources().getColor(R.color.lightGrey);
            cityAdapter.currentColor = context.getResources().getColor(R.color.darkGrey);
            wvCitys.setVisibleItems(5);
            wvCitys.setViewAdapter(cityAdapter);
            wvCitys.setCurrentItem(0);
            strCity = arrCitys.get(0).name;
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvCitys.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setTextviewSize(currentText, cityAdapter);
            }
        });

        wvCitys.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });
    }


    /**
     * 设置字体大小
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void setTextviewSize(String curriteItemText, AbstractWheelTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int             size      = arrayList.size();
        String          currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                textvew.setTextColor(context.getResources().getColor(R.color.darkGrey));
            } else {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                textvew.setTextColor(context.getResources().getColor(R.color.lightGrey));
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onAddressCListener != null) {
                if (TextUtils.isEmpty(strProvince) && null != arrProvinces && arrProvinces.size() > 0)
                    strProvince = arrProvinces.get(0).province;
                if (TextUtils.isEmpty(strCity) && null != arrCitys && arrCitys.size() > 0)
                    strCity = arrCitys.get(0).name;
                onAddressCListener.onClick(strProvince, strCity);
            }
        } else if (v == btnCancel) {

        } else if (v == lyChangeAddressChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnAddressCListener {
        public void onClick(String province, String city);
    }


    /**
     * 解析数据
     */
    private void initDatas() {
        arrProvinces = ProvinceDB.getList();
        if (null != arrProvinces && arrProvinces.size() > 0)
            arrCitys = CityDB.getList(arrProvinces.get(0).id);
    }

    /**
     * 初始化省会
     */
    public void initProvinces() {
        arrProvinces = ProvinceDB.getList();
    }

    /**
     * 根据省会，生成该省会的所有城市
     */
    public void initCitys(String pid) {
        arrCitys = CityDB.getList(pid);
    }

    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city) {
        this.strProvince = province;
        this.strCity = city;
    }

    /**
     * 返回省会索引，没有就返回默认“四川”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {

        int provinceIndex = 0;
        if (null != arrProvinces && arrProvinces.size() > 0) {
            int size = arrProvinces.size();
            for (int i = 0; i < size; i++) {
                if (arrProvinces.get(i).province.equals(province)) {
                    provinceIndex = i;
                    break;
                }
            }
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int cityIndex = 0;
        if (null != arrCitys && arrCitys.size() > 0) {
            int size = arrCitys.size();
            for (int i = 0; i < size; i++) {
                if (arrCitys.get(i).name.equals(city)) {
                    cityIndex = i;
                    break;
                }
            }
        }
        return cityIndex;
    }

}