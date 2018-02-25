package com.haitao.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.ShareAnalyticsObject;
import com.haitao.model.forum.ForumCommentObject;
import com.haitao.utils.CommonUtils;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.TraceUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CustomImageView;
import com.haitao.view.HtHeadView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cn.magicwindow.mlink.annotation.MLinkRouter;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.ForumTopicBriefModelData;
import io.swagger.client.model.TagModel;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 贴子详情
 */
@MLinkRouter(keys = {"topicKey"})
public class TopicDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvComment, tvFav, tvAgree, tvCommentCount;
    private ViewGroup layoutFav, layoutAgree, layoutCommentCount, layoutBottom;
    private WebView wvBody;

    private ViewGroup layoutProgress;

    private String id = "";
    private ForumTopicBriefModelData topicModel;

    PhotoPickParameterObject mPhotoPickParameterInfo;

    private boolean isLoadComment = false;

    private String floor = "";

    private String keyword = "";
    private CustomImageView mImgActivityFab; // 全局活动入口
    private HtHeadView      mHtHeadView;


    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id, boolean isLoadComment) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(TransConstant.ID, id);
        intent.putExtra("isLoadComment", isLoadComment);
        context.startActivity(intent);
    }


    /**
     * 跳转到当前页
     *
     * @param context mContext
     * @param topicId 帖子Id
     * @param floor   楼层
     */
    public static void launch(Context context, String topicId, String floor) {
        if (TextUtils.isEmpty(topicId))
            return;
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(TransConstant.ID, topicId);
        intent.putExtra(TransConstant.POSITION, floor);
        context.startActivity(intent);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String pid, String floor, String keyword) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(TransConstant.ID, pid);
        intent.putExtra(TransConstant.POSITION, floor);
        intent.putExtra(TransConstant.KEY, keyword);
        context.startActivity(intent);
    }


    private void logging() {
        ForumApi.getInstance().commonSearchingClickingLoggingPost(TextUtils.isEmpty(keyword) ? TransConstant.LogType.BBS_CLICK : TransConstant.LogType.BBS_SEARCH_CLICK, UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserId() : "0", topicModel.getTid(), topicModel.getTitle(), String.valueOf(System.currentTimeMillis()), keyword,
                response -> {

                }, error -> {

                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        initVars();
        initView();
        initEvent();
        initData();
    }

    private void initVars() {
        TAG = "帖子详情";

        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra(TransConstant.ID)) {
                id = intent.getStringExtra(TransConstant.ID);
            } else if (intent.hasExtra(TransConstant.VALUE)) {
                // 魔窗字段
                id = intent.getStringExtra(TransConstant.VALUE);

                // 页面埋点 魔窗事件
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Key, "topicKey");
                kv.put(TraceUtils.Event_Kv_Value, id);
                YbxTrace.getInstance().event((BaseActivity) mContext, "", "", ((BaseActivity) mContext).purl, ((BaseActivity) mContext).purlh, "", "", TraceUtils.Event_Category_Media, TraceUtils.Event_Action_Media_Mw, kv, "", TraceUtils.Fid_MW);

            }

            Logger.d("topic id = " + id);

            if (intent.hasExtra("isLoadComment")) {
                isLoadComment = intent.getBooleanExtra("isLoadComment", false);
            }
            if (isLoadComment)
                floor = "0";
            if (intent.hasExtra(TransConstant.POSITION)) {
                floor = intent.getStringExtra(TransConstant.POSITION);
            }
            if (intent.hasExtra(TransConstant.KEY)) {
                keyword = intent.getStringExtra(TransConstant.KEY);
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //        initTop();
        layoutBottom = getView(R.id.layoutBottom);
        layoutBottom.setVisibility(View.GONE);
        tvComment = getView(R.id.tvComment);
        mHtHeadView = getView(R.id.ht_headview);
        tvFav = getView(R.id.tvFav);
        mImgActivityFab = getView(R.id.img_event);
        tvAgree = getView(R.id.tvAgree);
        tvCommentCount = getView(R.id.tvCommentCount);
        layoutFav = getView(R.id.layoutFav);
        layoutAgree = getView(R.id.layoutAgree);
        layoutCommentCount = getView(R.id.layoutCommentCount);

        layoutProgress = getView(R.id.llProgress_common_progress);
        layoutProgress.setVisibility(View.VISIBLE);

        wvBody = getView(R.id.wvBody);
        wvBody.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvBody.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mHtHeadView.setCenterText("");
        // 活动入口
        if (!TextUtils.isEmpty(HtApplication.mActivityFabImg)) {
            mImgActivityFab.setVisibility(View.VISIBLE);
            mImgActivityFab.setOnClickListener(v -> goEvent(mContext));
            //            ImageLoaderUtils.showOnlineImage(HtApplication.mActivityFabImg, mImgActivityFab);
            ImageLoaderUtils.showOnlineGifImage(HtApplication.mActivityFabImg, mImgActivityFab);
        }
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        tvComment.setOnClickListener(this);
        layoutFav.setOnClickListener(this);
        layoutAgree.setOnClickListener(this);
        layoutCommentCount.setOnClickListener(this);
//        btnRight.setOnClickListener(this);
        mHtHeadView.setOnRightClickListener(view -> {
            String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(topicModel.getSharePic());
            if (TextUtils.isEmpty(topicModel.getSharePic())) {
                if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                    bitmap.recycle();
                } else {
                    picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                }
            }
            ShareUtils.showShareDialog(mContext, 1, topicModel.getShareTitle(), topicModel.getShareContent(), topicModel.getShareContentWeibo(), topicModel.getShareUrl(), picUrl, new ShareAnalyticsObject("分享_帖子", id));

        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //        btnRight.setImageResource(R.drawable.ic_share);
//        mHtHeadView.setRightImg(R.mipmap.ic_share);
        mHtHeadView.setCenterText("帖子详情");
        getData();
    }


    private void getData() {
        ForumApi.getInstance().forumTopicTopicIdBriefGet(id,
                response -> {
                    if (layoutBottom == null)
                        return;
                    if ("0".equals(response.getCode())) {
                        topicModel = response.getData();
                        renderView();
                        layoutProgress.setVisibility(View.GONE);
                        layoutBottom.setVisibility(View.VISIBLE);
                        logging();
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                        Logger.d(response.getMsg());
                        finish();
                    }
                }, error -> {
                    if (layoutBottom == null)
                        return;
                    layoutProgress.setVisibility(View.GONE);
                    showErrorToast(error);
                });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void renderView() {
        if (null != topicModel) {
            if (null == mPhotoPickParameterInfo)
                mPhotoPickParameterInfo = new PhotoPickParameterObject();
            mPhotoPickParameterInfo.image_list = new ArrayList<String>();
            ImageLoaderUtils.downloadOnlineImage(mContext, topicModel.getSharePic());
            //            btnRight.setVisibility(View.VISIBLE);
            mHtHeadView.setRightImg(R.mipmap.ic_share);
            tvFav.setSelected("1".equals(topicModel.getIsFavorite()));
            tvFav.setText("0".equals(topicModel.getCollectionCount()) ? "收藏" : topicModel.getCollectionCount());
            tvAgree.setSelected("1".equals(topicModel.getIsPraised()));
            tvAgree.setText("0".equals(topicModel.getPraiseCount()) ? "赞" : topicModel.getPraiseCount());
            tvCommentCount.setText(topicModel.getReplyCount());
            wvBody.setHorizontalScrollBarEnabled(false);
            wvBody.setVerticalScrollBarEnabled(false);
            String url = UserManager.getInstance().isLogin() ? topicModel.getTopicUrl() + "&token=" + UserManager.getInstance().getHtToken() : topicModel.getTopicUrl();
            url += "&platform=android";
            if (isLoadComment || !TextUtils.isEmpty(floor)) {
                url += "&floor=" + floor;
            }
            wvBody.loadUrl(url);
            wvBody.getSettings().setJavaScriptEnabled(true);
            wvBody.setWebChromeClient(new WebChromeClient());
            wvBody.addJavascriptInterface(new JavascriptInterface(mContext), "android");
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            wvBody.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    if (CommonUtils.isAppUrl(url)) {
                        CommonUtils.parseUrlAndGo(mContext, url);
                    } else {
                        WebActivity.launch(mContext, getResources().getString(R.string.app_name), url);
                    }
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    view.getSettings().setJavaScriptEnabled(true);
                    /*if(isLoadComment){
                        wvBody.loadUrl("javascript:appGotoFloor()");
                    }*/

                    super.onPageFinished(view, url);
                    //wvBody.loadUrl("javascript:alert(navigator.userAgent)");
                    Logger.d("====loadOver===" + url);
                }
            });

        }
    }


    // js通信接口
    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void doLogin() {
            QuickLoginActivity.launch(mContext);
            Logger.d("doLogin");
        }

        @android.webkit.JavascriptInterface
        public void goToBoard(String boardId) {
            Logger.d("goToBoard:" + boardId);
            BoardDetailActivity.launch(mContext, boardId);
        }

        @android.webkit.JavascriptInterface
        public void showUserCenter(String uid) {
            Logger.d("showUserCenter:" + uid);
            TalentDetailActivity.launch(mContext, uid);
        }

        @android.webkit.JavascriptInterface
        public void showTag(String tagInfo) {
            Logger.d("showTag:" + tagInfo);
            TagModel tagObject = JsonUtil.deserializeToObject(tagInfo, TagModel.class);
            TagDetailActivity.launch(mContext, tagObject.getTagName(), tagObject.getTagId());
        }

        @android.webkit.JavascriptInterface
        public void showImgByIdx(String idx, String[] list) {
            Logger.d("showImgByIdx:" + idx);
            int position = Integer.parseInt(idx);
            if (null == list || position >= list.length) {
                return;
            }
            openImagePreview(Integer.parseInt(idx), list);
        }

        @android.webkit.JavascriptInterface
        public void actReply(String replyInfo) {
            Logger.d("actReply:" + replyInfo);
            ForumCommentObject forumCommentObject = JSON.parseObject(replyInfo, ForumCommentObject.class);
            Bundle             bundle             = new Bundle();
            bundle.putSerializable(TransConstant.OBJECT, forumCommentObject);
            bundle.putString(TransConstant.ID, id);
            TopicReplyActivity.launch(mContext, bundle);
        }


    }

    //图片预览
    public void openImagePreview(int position, String[] list) {
        mPhotoPickParameterInfo.position = position;
        mPhotoPickParameterInfo.image_list.clear();
        mPhotoPickParameterInfo.image_list.addAll(Arrays.asList(list));
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvComment:
                TopicReplyActivity.launch(mContext, id);
                break;
            case R.id.layoutCommentCount:
                wvBody.loadUrl("javascript:appGotoFloor()");
                break;
            case R.id.layoutFav:
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                layoutFav.setEnabled(false);
                if (tvFav.isSelected()) {
                    delFav();
                } else {
                    addFav();
                }
                break;
            case R.id.layoutAgree:
                if (!HtApplication.isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                layoutAgree.setEnabled(false);
                if (!tvAgree.isSelected()) {
                    addAgree();
                }
                break;
           /* case R.id.btnRight:
                String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(topicModel.getSharePic());
                if (TextUtils.isEmpty(topicModel.getSharePic())) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                        bitmap.recycle();
                    } else {
                        picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }
                ShareUtils.showShareDialog(mContext, 1, topicModel.getShareTitle(), topicModel.getShareContent(), topicModel.getShareContentWeibo(), topicModel.getShareUrl(), picUrl, new ShareAnalyticsObject("分享_帖子", id));
                break;*/
            default:
                break;
        }
    }

    /**
     * 点赞-网络请求
     */
    private void addAgree() {
        ForumApi.getInstance().userPraisingPost(TransConstant.praiseType.POST, id,
                response -> {
                    if (layoutBottom == null)
                        return;
                    layoutFav.setEnabled(true);
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "点赞成功");
                        tvAgree.setSelected(true);
                        int praiseCount = TextUtils.isEmpty(topicModel.getPraiseCount()) ? 0 : Integer.parseInt(topicModel.getPraiseCount());
                        tvAgree.setText(String.valueOf(praiseCount + 1));
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (layoutBottom == null)
                        return;
                    showErrorToast(error);
                });
    }

    /**
     * 添加收藏-网络请求
     */
    private void addFav() {
        ForumApi.getInstance().userCollectionPost(TransConstant.favType.POST, id, "",
                response -> {
                    if (layoutFav == null)
                        return;
                    layoutFav.setEnabled(true);
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "收藏成功");
                        int favCount = TextUtils.isEmpty(topicModel.getCollectionCount()) ? 0 : Integer.parseInt(topicModel.getCollectionCount());
                        topicModel.setCollectionCount(String.valueOf(favCount + 1));
                        tvFav.setText("0".equals(topicModel.getCollectionCount()) ? "收藏" : topicModel.getCollectionCount());
                        tvFav.setSelected(true);
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_POST_FAV);
                        mContext.sendBroadcast(mIntent);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (layoutFav == null)
                        return;
                    showErrorToast(error);
                });

    }

    /**
     * 取消收藏-网络请求
     */
    private void delFav() {
        ForumApi.getInstance().userCollectionDelete(TransConstant.favType.POST, id,
                response -> {
                    if (layoutFav == null)
                        return;
                    layoutFav.setEnabled(true);
                    if ("0".equals(response.getCode())) {
                        ToastUtils.show(mContext, "取消收藏");
                        int favCount = TextUtils.isEmpty(topicModel.getCollectionCount()) ? 0 : Integer.parseInt(topicModel.getCollectionCount());
                        topicModel.setCollectionCount(favCount > 0 ? String.valueOf(favCount - 1) : "0");
                        tvFav.setText("0".equals(topicModel.getCollectionCount()) ? "收藏" : topicModel.getCollectionCount());
                        tvFav.setSelected(false);
                        Intent mIntent = new Intent(TransConstant.CHANGE_BROADCAST);
                        mIntent.putExtra(TransConstant.TYPE, TransConstant.BROAD_POST_FAV);
                        mContext.sendBroadcast(mIntent);
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {
                    if (layoutFav == null)
                        return;
                    showErrorToast(error);
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View v = getWindow().getCurrentFocus();
        if (null != v)
            v.clearFocus();
        if (requestCode == resultCode && requestCode == TransConstant.IS_LOGIN) {
            getData();
        }
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            wvBody.loadUrl((UserManager.getInstance().isLogin() ? topicModel.getTopicUrl() + "&token=" + UserManager.getInstance().getHtToken() : topicModel.getTopicUrl()) + "&platform=android" + "&floor=0");
        }
    }
}
