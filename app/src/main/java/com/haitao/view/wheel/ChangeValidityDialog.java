package com.haitao.view.wheel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.view.HtHeadView;
import com.haitao.view.wheel.adapters.AbstractWheelTextAdapter;
import com.haitao.view.wheel.views.OnWheelScrollListener;
import com.haitao.view.wheel.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * 日期选择对话框
 *
 * @author ywl
 */
public class ChangeValidityDialog extends Dialog {

    private Context   context;
    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;

    private TextView btnSure;
    private TextView btnCancel;

    private ArrayList<String> arry_years  = new ArrayList<String>();
    private ArrayList<String> arry_months = new ArrayList<String>();
    private ArrayList<String> arry_days   = new ArrayList<String>();
    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDaydapter;

    private int month;
    private int day;

    private int currentYear  = getYear();
    private int currentMonth = 1;
    private int currentDay   = 1;

    private int maxTextSize = 20;
    private int minTextSize = 20;

    private boolean issetdata = false;

    private String selectYear;
    private String selectMonth;
    private String selectDay;

    private OnBirthListener mOnBirthListener;
    private HtHeadView      htHeadView;
    private LinearLayout    mLlYear;
    private LinearLayout    mLlMonth;
    private LinearLayout    mLlDay;

    public ChangeValidityDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_validity);
        mLlYear = findViewById(R.id.ll_date_year);
        mLlMonth = findViewById(R.id.ll_date_month);
        mLlDay = findViewById(R.id.ll_date_day);
        wvYear = findViewById(R.id.wv_date_year);
        wvMonth = findViewById(R.id.wv_date_month);
        wvDay = findViewById(R.id.wv_date_day);
        htHeadView = findViewById(R.id.ht_headview);
        // 点击事件
        htHeadView.setOnRightClickListener(view -> {
            if (mOnBirthListener != null) {
                mOnBirthListener.onClick(selectYear, selectMonth, selectDay);
                dismiss();
            }
        });
        htHeadView.setOnLeftClickListener(view -> dismiss());

        //        btnSure = findViewById(R.id.btn_myinfo_sure);
        //        btnCancel = findViewById(R.id.btn_myinfo_cancel);

        //        btnSure.setOnClickListener(this);
        //        btnCancel.setOnClickListener(this);

        if (!issetdata) {
            initData();
        }
        initYears();
        mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(setYear(currentYear));

        initMonths(month);
        mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(setMonth(currentMonth));

        initDays(day);
        mDaydapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(currentDay - 1);

        wvYear.addChangingListener((wheel, oldValue, newValue) -> {
            String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
            selectYear = currentText;
            setTextviewSize(currentText, mYearAdapter);
            currentYear = Integer.parseInt(currentText);
            setYear(currentYear);
            initMonths(currentYear == getYear() ? getMonth() : 1);
            mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
            wvMonth.setVisibleItems(5);
            wvMonth.setViewAdapter(mMonthAdapter);
            wvMonth.setCurrentItem(0);
            selectMonth = arry_months.get(0);
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });

        wvMonth.addChangingListener((wheel, oldValue, newValue) -> {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            selectMonth = currentText;
            setTextviewSize(currentText, mMonthAdapter);
            setMonth(Integer.parseInt(currentText));
            initDays(day);
            mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
            wvDay.setVisibleItems(5);
            wvDay.setViewAdapter(mDaydapter);
            wvDay.setCurrentItem(0);
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
            }
        });

        wvDay.addChangingListener((wheel, oldValue, newValue) -> {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mDaydapter);
            selectDay = currentText;
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
            }
        });

    }

    public void initYears() {
        for (int i = getYear(); i < getYear() + 50; i++) {
            arry_years.add(i + "");
        }
    }

    public void initMonths(int months) {
        arry_months.clear();
        for (int j = months; j <= 12; j++) {
            arry_months.add(j + "");
        }
    }

    public void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= days; i++) {
            arry_days.add(i + "");
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_area, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    public void setBirthdayListener(OnBirthListener onBirthListener) {
        this.mOnBirthListener = onBirthListener;
    }

    public interface OnBirthListener {
        void onClick(String year, String month, String day);
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int             size      = arrayList.size();
        String          currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public void initData() {
        setDate(getYear(), getMonth(), getDay());
        this.currentDay = 1;
        this.currentMonth = 1;
    }

    /**
     * 设置年月日
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        selectYear = year + "";
        selectMonth = month + "";
        selectDay = day + "";
        issetdata = true;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
        if (year == getYear()) {
            this.month = getMonth();
        } else {
            this.month = 12;
        }
        calDays(year, month);
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public int setYear(int year) {
        int yearIndex = 0;
        if (year != getYear()) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }
        for (int i = getYear(); i > 1950; i--) {
            if (i == year) {
                return yearIndex;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    /**
     * 设置月份
     *
     * @param month
     * @return
     */
    public int setMonth(int month) {
        int monthIndex = 0;
        calDays(currentYear, month);
        for (int i = 0; i < this.arry_months.size(); i++) {
            if (month == Integer.parseInt(this.arry_months.get(i))) {
                return i;
            }
        }
        return monthIndex;
    }

    /**
     * 计算每月多少天
     *
     * @param month
     */
    public void calDays(int year, int month) {
        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29;
                    } else {
                        this.day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30;
                    break;
            }
        }
        if (year == getYear() && month == getMonth()) {
            this.day = getDay();
        }
    }
}