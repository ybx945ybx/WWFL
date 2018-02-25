package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haitao.R;
import com.haitao.adapter.HelpAdapter;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.HelpObject;

import java.util.ArrayList;

/**
 * 使用帮助
 */
public class HelpActivity extends BaseActivity{
    private ListView lvList;
    private ArrayList<HelpObject> mList;
    private HelpAdapter mAdapter;
    protected IViewContext<HelpObject, IEntityService<HelpObject>> commandViewContext = VF.<HelpObject>getDefault(HelpObject.class);
    /**
     * 跳转到当前页
     * @param context mContext
     */
    public static void launch(Context context){
        Intent intent = new Intent(context,HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "使用帮助";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(R.string.setting_help);
        lvList = getView(R.id.lvList);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WebActivity.launch(mContext,mList.get(position).name,mList.get(position).url);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList = new ArrayList<HelpObject>();
        mAdapter = new HelpAdapter(mContext,mList);
        lvList.setAdapter(mAdapter);
        loadData();
    }

    private void loadData(){
        commandViewContext.asynQuery(MethodConstant.HELP, commandViewContext.getEntity(), new IAsynServiceHandler<HelpObject>() {
            @Override
            public void onSuccess(HelpObject entity) throws Exception {

            }

            @Override
            public void onSuccessPage(PageResult<HelpObject> entity) throws Exception {
                if(null != entity && null != entity.entityList && entity.entityList.size() > 0){
                    mList.addAll(entity.entityList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

}
