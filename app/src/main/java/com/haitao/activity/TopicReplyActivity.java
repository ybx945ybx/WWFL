package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.model.EditDataObject;
import com.haitao.model.PhotoImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.PostObject;
import com.haitao.model.forum.ForumCommentObject;
import com.haitao.utils.ColorPhrase;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.ExpandableTextView;
import com.haitao.view.richEdit.RichTextEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * 回贴
 */
public class TopicReplyActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup                layoutBottom;
    private PhotoPickParameterObject mPhotoPickParameterInfo;

    private boolean isReply = true;
    private RichTextEditor etContent;
    private String content = "";
    private String id      = "", pid = "";
    private ForumCommentObject commentObj;

    private ViewGroup layoutQuote;
    private TextView  tvQuoteName;
    ExpandableTextView layoutReplyContent;
    ArrayList<String> picList = new ArrayList<String>();

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, TopicReplyActivity.class);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    public static void launch(Context context, Bundle bundle) {
        Intent intent = new Intent(context, TopicReplyActivity.class);
        intent.putExtra(TransConstant.OBJECT, bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceCalanderType) {
        super.onCreate(savedInstanceCalanderType);
        setContentView(R.layout.activity_topic_reply);
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(TransConstant.ID)) {
            isReply = false;
            id = intent.getStringExtra(TransConstant.ID);
        } else if (null != intent && intent.hasExtra(TransConstant.OBJECT)) {
            Bundle bundle = intent.getBundleExtra(TransConstant.OBJECT);
            if (null != bundle) {
                id = bundle.getString(TransConstant.ID);
                commentObj = (ForumCommentObject) bundle.getSerializable(TransConstant.OBJECT);
            }
        }
        TAG = "回贴";
        ImageLoader.getInstance().clearMemoryCache();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(isReply ? R.string.post_comment_reply_title : R.string.post_comment_add_title);
        tvRight.setTextColor(getResources().getColor(R.color.midBlue));
        tvRight.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        tvRight.setText(R.string.post_send_submit);
        layoutBottom = getView(R.id.layoutBottom);
        etContent = getView(R.id.etContent);
        layoutQuote = getView(R.id.layoutQuote);
        tvQuoteName = getView(R.id.tvQuoteName);
        layoutReplyContent = getView(R.id.layoutReplyContent);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        etContent.setTextChangeListener(() -> {
            List<EditDataObject> editList = etContent.buildEditData();
            content = String.valueOf(editList.size());
            if (1 == editList.size()) {
                EditDataObject ed = editList.get(0);
                if (TextUtils.isEmpty(ed.inputStr) && TextUtils.isEmpty(ed.imagePath)) {
                    content = "";
                }
            }
        });
        tvRight.setOnClickListener(this);
        layoutBottom.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.single_mode = true;
        mPhotoPickParameterInfo.max_image = 8;
        if (null != commentObj) {
            pid = commentObj.pid;
            layoutQuote.setVisibility(View.VISIBLE);
            String author = String.format("回复 {%s楼  %s}   %s", commentObj.floor_num, commentObj.author_name, commentObj.post_time);
            CharSequence formatted = ColorPhrase.from(author)
                    .withSeparator("{}")
                    .innerColor(mContext.getResources().getColor(R.color.black))
                    .outerColor(mContext.getResources().getColor(R.color.grey))
                    .format();
            tvQuoteName.setText(formatted);
            layoutReplyContent.setText(commentObj.content);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
                finish();
                break;
            case R.id.tvRight:
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(mContext, R.string.post_content_tips);
                    return;
                }
                submit();
                break;
            case R.id.layoutBottom:
                List<EditDataObject> editList = etContent.buildEditData();
                int imageSize = 0;
                picList.clear();
                for (int i = 0; i < editList.size(); i++) {
                    EditDataObject itemData = editList.get(i);
                    if (!TextUtils.isEmpty(itemData.imagePath)) {
                        imageSize++;
                        picList.add(itemData.imagePath);
                    }
                }
                if (imageSize >= 9) {
                    ToastUtils.show(mContext, "最多可选择9张图片");
                    return;
                }
                //                PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
                photoPickWrapper();
                break;
            default:
                break;
        }
    }

    @Override
    protected void photoPick() {
        super.photoPick();
        PhotoPickActivity.launch(mContext, mPhotoPickParameterInfo);
    }

    private void submit() {

        ProgressDialogUtils.show(mContext, R.string.operationg);

        List<EditDataObject> editList  = etContent.buildEditData();
        StringBuffer         sbContent = new StringBuffer();
        picList.clear();
        for (int i = 0; i < editList.size(); i++) {
            EditDataObject itemData = editList.get(i);
            if (!TextUtils.isEmpty(itemData.inputStr)) {
                sbContent.append(itemData.inputStr);
                sbContent.append("\n");
            } else if (!TextUtils.isEmpty(itemData.imagePath)) {
                sbContent.append("[attach]");
                sbContent.append(picList.size());
                sbContent.append("[/attach]");
                sbContent.append("\n");
                picList.add(itemData.imagePath);
            }
        }
        content = sbContent.toString();

        if (picList.size() > 0) {
            new PicTask().execute();
        } else {
            ForumApi.getInstance().forumTopicTopicIdReplyPost(id, content, pid, null,
                    response -> {
                        if (tvTitle == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        if ("0".equals(response.getCode())) {
                            ToastUtils.show(mContext, response.getMsg());
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
                            setResult(TransConstant.REFRESH);
                            finish();
                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    }, error -> {
                        if (tvTitle == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        showErrorToast(error);
                    });
        }
    }

    private class PicTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ArrayList<String> pics = compressImage();
            ForumApi.getInstance().forumTopicTopicIdReplyPost(id, content, pid, pics,
                    response -> {
                        ProgressDialogUtils.dismiss();
                        if ("0".equals(response.getCode())) {
                            ToastUtils.show(mContext, response.getMsg());
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
                            setResult(TransConstant.REFRESH);
                            finish();
                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    }, error -> ProgressDialogUtils.dismiss());
            return null;
        }
    }

    private ArrayList<String> compressImage() {
        ArrayList<String> mData = new ArrayList<String>();
        for (int i = 0; i < picList.size(); i++) {
            String path = ImageUtil.getBitmapBase64(picList.get(i), 408, 800);
            mData.add(path);
        }
        return mData;
    }


    class responseHandler implements IAsynServiceHandler<PostObject> {

        @Override
        public void onSuccess(PostObject entity) throws Exception {
            ProgressDialogUtils.dismiss();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnLeft.getWindowToken(), 0);
            ToastUtils.show(mContext, R.string.post_success);
            setResult(TransConstant.REFRESH);
            finish();
        }

        @Override
        public void onSuccessPage(PageResult<PostObject> entity) throws Exception {
            ProgressDialogUtils.dismiss();

        }

        @Override
        public void onFailed(String error) {
            ProgressDialogUtils.dismiss();
            ToastUtils.show(mContext, error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(PhotoPickParameterObject.EXTRA_PARAMETER)) {
                    getBundle(data.getExtras());
                }
                break;
        }
        if (requestCode == TransConstant.IS_LOGIN) {
            if (!HtApplication.isLogin()) {
                finish();
            } else {
                initData();
            }
        }
    }

    private void getBundle(Bundle bundle) {
        if (bundle != null) {
            mPhotoPickParameterInfo = (PhotoPickParameterObject) bundle.getSerializable(PhotoPickParameterObject.EXTRA_PARAMETER);
            ArrayList<String> list = mPhotoPickParameterInfo.image_list;
            if (list != null) {

                for (int i = 0; i < list.size(); i++) {
                    PhotoImageObject img = new PhotoImageObject();
                    img.source_image = list.get(i);
                    insertBitmap(img.source_image);
                }

            }
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath 图片地址
     */
    private void insertBitmap(String imagePath) {
        etContent.insertImage(imagePath);
    }
    //图片预览

    public void openImagePreview(int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        intent.putExtras(b);
        startActivityForResult(intent, PhotoPickParameterObject.TAKE_PICTURE_PREVIEW);
    }

}
