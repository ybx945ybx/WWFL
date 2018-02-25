package com.haitao.view.wheel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.view.wheel.adapters.AbstractWheelTextAdapter;
import com.haitao.view.wheel.views.OnWheelChangedListener;
import com.haitao.view.wheel.views.OnWheelScrollListener;
import com.haitao.view.wheel.views.WheelView;

import java.util.ArrayList;


/**
 * 分页选择对话框
 * 
 * @author ywl
 *
 */
public class ChangePageDialog extends Dialog implements View.OnClickListener {
	private ViewGroup layoutParent,layoutChild;
	private Context context;
	private WheelView wvPage;

	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> array_page = new ArrayList<String>();
	private PageTextAdapter mPageAdapter;


	private int currentPage = 1;
	private int totalPage = 1;

	private int maxTextSize = 24;
	private int minTextSize = 14;

	private String selectPage = "1";

	private OnPageListener onPageListener;

	public ChangePageDialog(Context context) {
		super(context, R.style.MyDialogStyleBottom);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_page);
		wvPage = (WheelView) findViewById(R.id.wv_page);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		layoutParent = (ViewGroup) findViewById(R.id.layoutParent);
		layoutChild = (ViewGroup) findViewById(R.id.layoutChild);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		layoutParent.setOnClickListener(this);
		layoutChild.setOnClickListener(this);

		initDatas();
		int currentPosition = getPagePosition(String.format("第%d页",currentPage));
		mPageAdapter = new PageTextAdapter(context, array_page, currentPosition, maxTextSize, minTextSize);
		mPageAdapter.normalColor = context.getResources().getColor(R.color.lightGrey);
		mPageAdapter.currentColor = context.getResources().getColor(R.color.darkGrey);
		wvPage.setVisibleItems(5);
		wvPage.setViewAdapter(mPageAdapter);
		wvPage.setCurrentItem(currentPosition);
		selectPage = String.valueOf(wvPage.getCurrentItem()+1);

		wvPage.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mPageAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mPageAdapter);
				selectPage = String.valueOf(wheel.getCurrentItem()+1);;
			}
		});

		wvPage.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mPageAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mPageAdapter);
			}
		});

	}

	public int getPagePosition(String page){
		int index = 0;
		if(null != array_page && array_page.size() > 0){
			int size = array_page.size();
			for(int i = 0; i < size; i++){
				if (array_page.get(i).equals(page)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}


	private class PageTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected PageTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setPageListener(OnPageListener onPageListener) {
		this.onPageListener = onPageListener;
	}

	@Override
	public void onClick(View v) {
		if (v == btnSure) {
			if (onPageListener != null) {
				onPageListener.onClick(Integer.parseInt(selectPage));
			}
		}else if (v == btnCancel) {

		} else {
			dismiss();
		}
		dismiss();

	}

	public interface OnPageListener {
		public void onClick(int page);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, PageTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
				textvew.setTextColor(context.getResources().getColor(R.color.darkGrey));
			} else {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(context.getResources().getColor(R.color.lightGrey));
			}

		}
	}


	public void initDatas() {
		for (int i = 1; i <= totalPage; i++) {
			array_page.add(String.format("第%d页",i));
		}
	}


	public void setData(int page,int totalPage){
		this.currentPage = page;
		this.totalPage = totalPage;
	}

}