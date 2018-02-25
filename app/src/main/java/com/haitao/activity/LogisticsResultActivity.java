package com.haitao.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.LogisticsResultAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.LogisticsObject;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

/**
 * 物流查询
 */

public class LogisticsResultActivity extends BaseActivity {
	private TextView tvLogisticsTitle;
	private TextView tvResult;
	private View layoutContent;

	private XListView lvList;
	private ArrayList<LogisticsObject> mList;
	private LogisticsResultAdapter mAdapter;

	private LogisticsObject logisticsObject;
	protected IViewContext<LogisticsObject, IEntityService<LogisticsObject>> commandViewContext = VF.<LogisticsObject>getDefault(LogisticsObject.class);
	/**
	 * 跳转到当前页
	 * @param context mContext
	 */
	public static void launch(Context context,LogisticsObject obj){
		Intent intent = new Intent(context,LogisticsResultActivity.class);
		intent.putExtra(TransConstant.OBJECT,obj);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics_result);
		TAG = "查询结果";
		mContext = LogisticsResultActivity.this;
		initView();
		initEvent();
		if(null != getIntent() && getIntent().hasExtra(TransConstant.OBJECT)){
			logisticsObject = (LogisticsObject) getIntent().getSerializableExtra(TransConstant.OBJECT);
		}
		if (!HtApplication.isLogin()) {
			QuickLoginActivity.launch(mContext);
			return;
		}
		initData();
	}
	
	private void initView(){
		initTop();
		lvList = getView(R.id.lvList);
		lvList.setPullLoadEnable(false);
		lvList.setPullRefreshEnable(false);
		lvList.setAutoLoadEnable(true);
		layoutContent = View.inflate(mContext,R.layout.layout_logistics_result,null);
		lvList.addHeaderView(layoutContent);
		tvTitle.setText(R.string.logistics_result_title);
		tvResult = getView(layoutContent,R.id.tvResult);
		tvLogisticsTitle = getView(layoutContent,R.id.tvLogisticsTitle);

	}
	
	private void initEvent(){
		lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}

	private void initData(){
		tvResult.setText(String.format(mContext.getResources().getString(R.string.logistics_result),logisticsObject.tname,logisticsObject.order_id));
		mList = new ArrayList<LogisticsObject>();
		mAdapter = new LogisticsResultAdapter(mContext,mList);
		lvList.setAdapter(mAdapter);
		getData();
	}

	private void getData(){
		commandViewContext.getEntity().tid = logisticsObject.tid;
		commandViewContext.getEntity().order_id = logisticsObject.order_id;
		commandViewContext.getEntity().tracking_no = logisticsObject.tracking_no;
		commandViewContext.asynQuery(MethodConstant.LOGISTICS_SEARCH, commandViewContext.getEntity(), new IAsynServiceHandler<LogisticsObject>() {
			@Override
			public void onSuccess(LogisticsObject entity) throws Exception {

			}

			@Override
			public void onSuccessPage(PageResult<LogisticsObject> entity) throws Exception {
				if(null != entity && null != entity.entityList){
					mList.addAll(entity.entityList);
					mAdapter.notifyDataSetChanged();
					tvLogisticsTitle.setVisibility(View.VISIBLE);
				}else {
					tvLogisticsTitle.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailed(String error) {

			}
		});
	}
	

}
