package com.haitao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.common.Constant.MethodConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.framework.asynHandler.IAsynServiceHandler;
import com.haitao.framework.codec.result.PageResult;
import com.haitao.framework.service.IEntityService;
import com.haitao.framework.service.IViewContext;
import com.haitao.imp.VF;
import com.haitao.model.CommentObject;
import com.haitao.model.EditDataObject;
import com.haitao.model.PhotoImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.PostObject;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.richEdit.RichTextEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 回贴
 */
public class PostReplyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView                ivAdd;
    private PhotoPickParameterObject mPhotoPickParameterInfo;

    private boolean isReply = true;
    private RichTextEditor etContent;
    private String content = "";
    private String id      = "";
    private CommentObject commentObj;
    protected IViewContext<PostObject, IEntityService<PostObject>> postContext = VF.<PostObject>getDefault(PostObject.class);

    private ViewGroup layoutQuote;
    private TextView  tvQuoteName;
    private TextView  tvQuoteContent;
    ArrayList<String> picList = new ArrayList<String>();

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, PostReplyActivity.class);
        intent.putExtra(TransConstant.ID, id);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    public static void launch(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PostReplyActivity.class);
        intent.putExtra(TransConstant.OBJECT, bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceCalanderType) {
        super.onCreate(savedInstanceCalanderType);
        setContentView(R.layout.activity_post_reply);
        initVars();
        initView();
        initEvent();
        if (!HtApplication.isLogin()) {
            QuickLoginActivity.launch(mContext);
            return;
        }
        initData();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(TransConstant.ID)) {
            isReply = false;
            id = intent.getStringExtra(TransConstant.ID);
        } else if (null != intent && intent.hasExtra(TransConstant.OBJECT)) {
            Bundle bundle = intent.getBundleExtra(TransConstant.OBJECT);
            if (null != bundle) {
                id = bundle.getString(TransConstant.ID);
                commentObj = (CommentObject) bundle.getSerializable(TransConstant.OBJECT);
            }
        }
        TAG = "回贴";
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTop();
        tvTitle.setText(isReply ? R.string.post_comment_reply_title : R.string.post_comment_add_title);
        tvRight.setTextColor(getResources().getColor(R.color.brightOrange));
        tvRight.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        tvRight.setText(R.string.post_send_submit);
        ivAdd = getView(R.id.ivAdd);
        etContent = getView(R.id.etContent);
        layoutQuote = getView(R.id.layoutQuote);
        tvQuoteName = getView(R.id.tvQuoteName);
        tvQuoteContent = getView(R.id.tvQuoteContent);
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
        ivAdd.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.single_mode = true;
        mPhotoPickParameterInfo.max_image = 8;
        if (null != commentObj) {
            layoutQuote.setVisibility(View.VISIBLE);
            tvQuoteName.setText("“" + commentObj.author + "”");
            tvQuoteContent.setText(commentObj.content);
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
            case R.id.ivAdd:
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
        postContext.getEntity().tid = id;
        if (null != commentObj) {
            postContext.getEntity().pid = commentObj.id;
        }

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
        postContext.getEntity().message = content;
        if (picList.size() > 0) {
            new PicTask().execute();
        } else {
            postContext.getService().asynFunction(MethodConstant.THREAD_POST, postContext.getEntity(), new responseHandler());
        }


    }

    private class PicTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Pair<String, File>[] files = null;
            ArrayList<String>    pics  = compressImage();
            files = new Pair[pics.size()];
            for (int i = 0; i < pics.size(); i++) {
                files[i] = new Pair<String, File>("pic[" + i + "]", new File(pics.get(i)));
            }
            postContext.getService().asynFunctionWidthFile(MethodConstant.THREAD_POST, postContext.getEntity(), files, new responseHandler());
            return null;
        }
    }

    private ArrayList<String> compressImage() {
        ArrayList<String> mData = new ArrayList<String>();
        for (int i = 0; i < picList.size(); i++) {
            String path = ImageUtil.getNomalBitmap(picList.get(i), 408, 800);
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
     * @param imagePath
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
