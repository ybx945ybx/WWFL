package com.haitao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.adapter.BasePagerAdapter;
import com.haitao.adapter.SearchAutoFullAdapter;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.Enum.SearchType;
import com.haitao.connection.api.ForumApi;
import com.haitao.fragment.BaseFragment;
import com.haitao.fragment.SearchDiscountFragment;
import com.haitao.fragment.SearchStoreFragment;
import com.haitao.fragment.SearchTopicFragment;
import com.haitao.fragment.SearchTransportFragment;
import com.haitao.view.ClearEditText;
import com.haitao.view.SearchHistoryView;
import com.haitao.view.refresh.XListView;

import java.util.ArrayList;

import io.swagger.client.model.CompletingWordsIfModelData;

/**
 * 搜索页
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton   btnLeft;
    //搜索类型
    private SearchType    searchType;
    private ClearEditText etSearch;
    private String search = "";
    //搜索历史记录(包含热门搜索)
    private ViewGroup layoutHistoryContent;
    private SearchHistoryView historyView = null;
    //搜索自动匹配
    private XListView                             autoFullList;
    private ArrayList<CompletingWordsIfModelData> autoFullWords;
    private SearchAutoFullAdapter                 autoFullAdapter;
    //搜索结果
    private ViewGroup                             layoutContent;
    private TabLayout                             tabLayout;
    private ViewPager                             vpSwitch;
    private BasePagerAdapter                      mPagerAdapter;
    private ArrayList<BaseFragment>               fragments;
    private String[]                              tabs;
    int currentPosition = 0;

    private LayoutInflater mInflater;
    private boolean        initCustomTabView;
    private boolean        needAutoFull;      //  是否需要自动匹配
    private boolean        isAddHot;          //  是否点击的热门标签

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, SearchType type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(TransConstant.TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        searchType = (SearchType) getIntent().getSerializableExtra(TransConstant.TYPE);
        TAG = "搜索";
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mInflater = LayoutInflater.from(mContext);
        etSearch = getView(R.id.etSearch);
        tvRight = getView(R.id.tvRight);
        layoutHistoryContent = getView(R.id.layoutHistoryContent);
        historyView = new SearchHistoryView(mContext);
        layoutHistoryContent.addView(historyView);
        layoutContent = getView(R.id.layoutContent);
        tabLayout = getView(R.id.tab);
        vpSwitch = getView(R.id.vp_order_list);
        btnLeft = getView(R.id.btnLeft);
        autoFullList = getView(R.id.list_auto_fUll);
        autoFullList.setAutoLoadEnable(false);
        autoFullList.setPullLoadEnable(false);
        autoFullList.setPullRefreshEnable(false);
        //tvRight.setVisibility(TextUtils.isEmpty(search) ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvRight.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                search = s.toString().trim();
                tvRight.setText("".equals(search) ? R.string.cancel : R.string.search_txt);
                //tvRight.setVisibility(TextUtils.isEmpty(search) ? View.GONE : View.VISIBLE);
                if ("".equals(search)) {
                    search();
                } else {
                    // needAutoFull为true是在输入框中输入了内容去自动匹配全，为false是点击了标签设置到了edittext中则去搜索并重新设成true
                    if (needAutoFull) {
                        autoFullSearch();
                    } else {
                        needAutoFull = true;
                        search();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    search();
                }
                return false;
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (hasFocus) {//如果有焦点就显示软件盘

                    imm.showSoftInputFromInputMethod(etSearch.getWindowToken(), 0);

                } else {//否则就隐藏
                    try {
                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    } catch (Exception e) {
                        System.out.println(" null！！！！！");
                    }
                }
            }
        });
        //点击空格键隐藏软键盘
        etSearch.setOnKeyListener((v, keyCode, event) -> {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                search();
                return true;
            }
            return false;
        });

        historyView.listSearchHistory.setOnItemClickListener((parent, view, position, id) -> {
            needAutoFull = false;

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            search = historyView.mList.get(position);
            etSearch.setText(search);
            etSearch.setSelection(search.length());
            //            search();
        });

        historyView.tglytHot.setOnTagClickListener((view, position, parent) -> {
            needAutoFull = false;
            isAddHot = true;

            InputMethodManager imm = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            search = historyView.mHotList.get(position);
            etSearch.setText(search);
            etSearch.setSelection(search.length());
            //            SearchActivity.this.search();
            return false;
        });

        autoFullList.setOnItemClickListener((parent, view, position, id) -> {
            needAutoFull = false;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            int index = position - autoFullList.getHeaderViewsCount();
            if (index >= 0) {
                search = autoFullWords.get(index).getKeywords();
                if (search != null) {
                    etSearch.setText(search);
                    etSearch.setSelection(search.length());
                }
            }
            //            search();
        });

        btnLeft.setOnClickListener(this);
        vpSwitch.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                refresh(currentPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 自动匹配全关键字
     */
    private void autoFullSearch() {
        ForumApi.getInstance()
                .searchingKeywordsCompletingWordsGet(search, response -> {
                    if ("0".equals(response.getCode())) {
                        layoutHistoryContent.setVisibility(View.GONE);
                        layoutContent.setVisibility(View.GONE);
                        autoFullList.setVisibility(View.VISIBLE);

                        autoFullWords.clear();
                        autoFullWords.addAll(response.getData());
                        autoFullAdapter.notifyDataSetChanged();

                    }
                }, error -> {

                });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        needAutoFull = true;

        fragments = new ArrayList<>();
        SearchTopicFragment topicFragment = new SearchTopicFragment();
        Bundle              bundleTopic   = new Bundle();
        bundleTopic.putString(TransConstant.TYPE, "1");
        topicFragment.setArguments(bundleTopic);
        if (searchType == SearchType.ALL) {
            etSearch.setHint(R.string.search_hint);
            tabs = new String[]{"优惠", "晒单", "帖子", "商家", "转运"};
            fragments.add(new SearchDiscountFragment());

            SearchTopicFragment shaidanFragment = new SearchTopicFragment();
            Bundle              bundleShaidan   = new Bundle();
            bundleShaidan.putString(TransConstant.TYPE, "2");
            shaidanFragment.setArguments(bundleShaidan);
            fragments.add(shaidanFragment);
            fragments.add(topicFragment);
            fragments.add(new SearchStoreFragment());
            fragments.add(new SearchTransportFragment());
            etSearch.requestFocus();
        } else if (searchType == SearchType.STORE) {
            etSearch.setHint(R.string.search_store_hint);
            tabs = new String[]{"商家"};
            fragments.add(new SearchStoreFragment());
            tabLayout.setVisibility(View.GONE);
        } else if (searchType == SearchType.STORE_DEAL) {
            etSearch.setHint(R.string.search_store_deal_hint);
            tabs = new String[]{"优惠", "商家"};
            fragments.add(new SearchDiscountFragment());
            fragments.add(new SearchStoreFragment());
        } else if (searchType == SearchType.POST) {
            etSearch.setHint(R.string.search_post_hint);
            tabs = new String[]{"帖子"};
            fragments.add(topicFragment);
            tabLayout.setVisibility(View.GONE);
        } else if (searchType == SearchType.DEAL) {
            etSearch.setHint(R.string.search_deal_hint);
            tabs = new String[]{"优惠"};
            fragments.add(new SearchDiscountFragment());
            tabLayout.setVisibility(View.GONE);
        }
        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, tabs);
        vpSwitch.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(vpSwitch);
        layoutContent.setVisibility(View.GONE);
        for (int i = 0; i < tabs.length; i++) {
            TabLayout.Tab tabItem = tabLayout.getTabAt(i);
            tabItem.setCustomView(getTabView(i, tabs[i]));
        }

        autoFullWords = new ArrayList<>();
        autoFullAdapter = new SearchAutoFullAdapter(mContext, autoFullWords);
        autoFullList.setAdapter(autoFullAdapter);
    }

    private View getTabView(int position, String title) {
        View     view     = mInflater.inflate(R.layout.custom_tab_layout, null);
        TextView textView = view.findViewById(R.id.tab_title);
        textView.setText(title);
        if (position == 0 & !initCustomTabView) {
            view.setSelected(true);
            initCustomTabView = true;
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                if (TextUtils.isEmpty(search)) {
                    finish();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    search();
                }
                break;
            case R.id.btnLeft:
                finish();
                break;
            default:
                break;
        }
    }

    private void search() {
        if (!TextUtils.isEmpty(search)) {
            if (!isAddHot) {    //  点击热门标签不加入历史搜索
                historyView.addData(search);
            } else {
                isAddHot = false;
            }
            layoutHistoryContent.setVisibility(View.GONE);
            autoFullList.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof SearchStoreFragment) {
                    SearchStoreFragment searchStoreFragment = (SearchStoreFragment) fragments.get(i);
                    searchStoreFragment.clearData();
                    searchStoreFragment.keyword = search;
                } else if (fragments.get(i) instanceof SearchDiscountFragment) {
                    SearchDiscountFragment discountFragment = (SearchDiscountFragment) fragments.get(i);
                    discountFragment.clearData();
                    discountFragment.keyword = search;
                } else if (fragments.get(i) instanceof SearchTransportFragment) {
                    SearchTransportFragment transportFragment = (SearchTransportFragment) fragments.get(i);
                    transportFragment.clearData();
                    transportFragment.keyword = search;
                } else if (fragments.get(i) instanceof SearchTopicFragment) {
                    SearchTopicFragment searchTopicFragment = (SearchTopicFragment) fragments.get(i);
                    searchTopicFragment.clearData();
                    searchTopicFragment.keyword = search;
                }
            }
            refresh(currentPosition);
        } else {
            historyView.loadHistoryData();
            layoutHistoryContent.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
            autoFullList.setVisibility(View.GONE);
        }

    }

    /**
     * 刷新
     *
     * @param position Fragment位置
     */
    private void refresh(int position) {
        if (fragments == null || fragments.size() <= 0)
            return;
        if (fragments.get(position) instanceof SearchStoreFragment) {
            SearchStoreFragment searchStoreFragment = (SearchStoreFragment) fragments.get(position);
            searchStoreFragment.setKeyword(search);
        } else if (fragments.get(position) instanceof SearchDiscountFragment) {
            SearchDiscountFragment discountFragment = (SearchDiscountFragment) fragments.get(position);
            discountFragment.setKeyword(search);
        } else if (fragments.get(position) instanceof SearchTransportFragment) {
            SearchTransportFragment transportFragment = (SearchTransportFragment) fragments.get(position);
            transportFragment.setKeyword(search);
        } else if (fragments.get(position) instanceof SearchTopicFragment) {
            SearchTopicFragment searchTopicFragment = (SearchTopicFragment) fragments.get(position);
            searchTopicFragment.setKeyword(search);
        }
    }
}
