package com.haitao.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.haitao.R;
import com.haitao.common.Constant.SPConstant;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.HtApplication;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.CachePostObject;
import com.haitao.model.EditDataObject;
import com.haitao.model.PhotoImageObject;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.model.forum.BoardObject;
import com.haitao.utils.ImageUtil;
import com.haitao.utils.ProgressDialogUtils;
import com.haitao.utils.SPUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.core.ImageLoader;
import com.haitao.view.richEdit.RichTextEditor;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 发贴
 */
public class TopicSendActivity extends BaseActivity implements View.OnClickListener {
    private PhotoPickParameterObject mPhotoPickParameterInfo;
    private ViewGroup                layoutBottom;

    private ViewGroup layoutBoard, layoutCategory;
    private TextView tvBoard, tvCategory;
    private EditText       etSubject;
    private RichTextEditor etContent;


    private String subject = "", content = "";

    private CachePostObject cachePost = null;

    private BoardObject boardObject = null;

    ArrayList<String> picList = new ArrayList<String>();


    private String boardId = "", boardName = "";
    private String subBoardId = "", subBoardName = "";

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, TopicSendActivity.class);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }

    /**
     * 跳转到当前页
     *
     * @param context mContext
     */
    public static void launch(Context context, BoardObject object) {
        Intent intent = new Intent(context, TopicSendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TransConstant.OBJECT, object);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, TransConstant.REFRESH);
    }


    @Override
    protected void onCreate(Bundle savedInstanceCalanderType) {
        super.onCreate(savedInstanceCalanderType);
        setContentView(R.layout.activity_post_send);
        ImageLoader.getInstance().clearMemoryCache();
        Intent intent = getIntent();
        if (null != intent && null != intent.getExtras()) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(TransConstant.OBJECT)) {
                boardObject = (BoardObject) bundle.getSerializable(TransConstant.OBJECT);
            }
        }
        TAG = "发贴";
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
        tvTitle.setText(R.string.post_send_title);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.post_send_submit);
        layoutBoard = getView(R.id.layoutBoard);
        tvBoard = getView(R.id.tvBoard);
        layoutCategory = getView(R.id.layoutCategory);
        tvCategory = getView(R.id.tvCategory);
        etSubject = getView(R.id.etSubject);
        etContent = getView(R.id.etContent);
        layoutBottom = getView(R.id.layoutBottom);
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
            checkSubmitEnable();
        });
        layoutBottom.setOnClickListener(this);
        etSubject.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                subject = s.toString().trim();
                checkSubmitEnable();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        layoutBoard.setOnClickListener(this);
        tvBoard.setOnClickListener(this);

        layoutCategory.setOnClickListener(this);
        tvCategory.setOnClickListener(this);

        tvRight.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    List<EditDataObject> editList  = etContent.buildEditData();
                    int                  imageSize = 0;
                    picList.clear();
                    for (int i = 0; i < editList.size(); i++) {
                        EditDataObject itemData = editList.get(i);
                        if (!TextUtils.isEmpty(itemData.imagePath)) {
                            imageSize++;
                            picList.add(itemData.imagePath);
                        }
                    }
                    if (0 == imageSize) {
                        etContent.setFocusOn();
                    }
                }

                return false;
            }
        });
    }

    private void checkSubmitEnable() {
        tvRight.setEnabled(!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(content));
        tvRight.setTextColor(ContextCompat.getColor(mContext, tvRight.isEnabled() ? R.color.orangeFF804D : R.color.greyA5A5A8));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mPhotoPickParameterInfo = new PhotoPickParameterObject();
        mPhotoPickParameterInfo.max_image = 8;
        mPhotoPickParameterInfo.single_mode = true;
        final String cacheStrPost = (String) SPUtils.get(mContext, SPConstant.CACHE_POST, "");
        Logger.d(cacheStrPost);
        Logger.d(JSON.toJSONString(boardObject));
        if (null != boardObject && !TextUtils.isEmpty(boardObject.categoryName)) {
            //用户发报告
            boardId = boardObject.id;
            subBoardId = boardObject.typeid;
            boardName = boardObject.categoryName;
            subBoardName = boardObject.categoryName;
            tvBoard.setText(String.format("版块：%s", boardName));
            tvCategory.setText(String.format("版块：%s", subBoardName));
            layoutBoard.setEnabled(false);
            tvBoard.setEnabled(false);
            layoutCategory.setEnabled(false);
            tvCategory.setEnabled(false);
        } else if (!TextUtils.isEmpty(cacheStrPost)) {
            new AlertDialog.Builder(mContext)
                    .setMessage(R.string.post_open_cache_tips)
                    .setPositiveButton(R.string.open, (dialog, which) -> {
                        cachePost = JSON.parseObject(cacheStrPost, new TypeReference<CachePostObject>() {
                        });
                        if (null != cachePost) {
                            boardId = cachePost.boardId;
                            boardName = cachePost.boardName;
                            subBoardId = cachePost.subBoardId;
                            subBoardName = cachePost.subBoardName;
                            if (!TextUtils.isEmpty(boardId) && !TextUtils.isEmpty(boardName)) {
                                tvBoard.setText(boardName);
                            }
                            if (!TextUtils.isEmpty(subBoardId) && !TextUtils.isEmpty(subBoardName)) {
                                tvCategory.setText(subBoardName);
                            }
                            etSubject.setText(cachePost.subject);
                            if (null != cachePost.editList && cachePost.editList.size() > 0) {
                                etContent.setEditData(cachePost.editList);
                            }
                            if (!TextUtils.isEmpty(cachePost.content)) {
                                etContent.insertText(cachePost.content);
                            }
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        SPUtils.put(mContext, SPConstant.CACHE_POST, "");
                        if (null != boardObject && !TextUtils.isEmpty(boardObject.id) && !TextUtils.isEmpty(boardObject.name)) {
                            boardId = boardObject.id;
                            boardName = boardObject.name;
                            tvBoard.setText(String.format("版块：%s", boardName));
                            layoutBoard.setEnabled(false);
                            tvBoard.setEnabled(false);
                        }
                        dialog.dismiss();
                    }).show();
        } else {
            if (null != boardObject && !TextUtils.isEmpty(boardObject.id) && !TextUtils.isEmpty(boardObject.name)) {
                boardId = boardObject.id;
                boardName = boardObject.name;
                tvBoard.setText(String.format("版块：%s", boardName));
            }
        }
        checkSubmitEnable();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                photoPickWrapper();
                break;
            case R.id.btnLeft:
                doSave();
                break;
            case R.id.layoutBoard:
            case R.id.tvBoard:
                BoardSelectActivity.launch(mContext, boardId);
                break;
            case R.id.layoutCategory:
            case R.id.tvCategory:
                if (TextUtils.isEmpty(boardId)) {
                    layoutBoard.performClick();
                    return;
                }
                SubBoardSelectActivity.launch(mContext, boardId, subBoardId);
                break;
            case R.id.tvRight:
                subject = etSubject.getText().toString().trim();
                if (TextUtils.isEmpty(boardId)) {
                    ToastUtils.show(mContext, R.string.post_section_tips);
                    return;
                }
                if (TextUtils.isEmpty(subBoardId)) {
                    ToastUtils.show(mContext, R.string.post_category_tips);
                    return;
                }
                if (TextUtils.isEmpty(subject)) {
                    ToastUtils.show(mContext, R.string.post_subject_tips);
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(mContext, R.string.post_content_tips);
                    return;
                }
                submit();
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
        List<EditDataObject> editList  = etContent.buildEditData();
        StringBuffer         sbContent = new StringBuffer();
        picList.clear();
        String contentStr = "";
        for (int i = 0; i < editList.size(); i++) {
            EditDataObject itemData = editList.get(i);
            if (!TextUtils.isEmpty(itemData.inputStr)) {
                contentStr += itemData.inputStr;
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
        if (contentStr.length() < 15) {
            ToastUtils.show(mContext, "文字内容必须大于15个字符");
            return;
        }
        tvRight.post(() -> ProgressDialogUtils.show(mContext, R.string.operationg));
        content = sbContent.toString();

        if (picList.size() > 0) {
            new PicTask().execute();
        } else {
            ForumApi.getInstance().forumBoardBoardIdTopicPost(boardId, subject, content, subBoardId, null,
                    response -> {
                        if (layoutBottom == null)
                            return;
                        ProgressDialogUtils.dismiss();
                        if ("0".equals(response.getCode())) {
                            ToastUtils.show(mContext, response.getMsg());
                            SPUtils.put(mContext, SPConstant.CACHE_POST, "");
                            setResult(TransConstant.REFRESH);
                            finish();
                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    }, error -> {
                        if (layoutBottom == null)
                            return;
                        showErrorToast(error);
                        ProgressDialogUtils.dismiss();
                    });
        }


    }

    private class PicTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ArrayList<String> pics = compressImage();
            ForumApi.getInstance().forumBoardBoardIdTopicPost(boardId, subject, content, subBoardId, pics,
                    response -> {
                        ProgressDialogUtils.dismiss();
                        if ("0".equals(response.getCode())) {
                            ToastUtils.show(mContext, response.getMsg());
                            SPUtils.put(mContext, SPConstant.CACHE_POST, "");
                            setResult(TransConstant.REFRESH);
                            finish();
                        } else {
                            ToastUtils.show(mContext, response.getMsg());
                        }
                    }, error -> ProgressDialogUtils.dismiss());
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoPickParameterObject.TAKE_PICTURE_FROM_GALLERY://选择图片
            case PhotoPickParameterObject.TAKE_PICTURE_PREVIEW://图片展示
                if (resultCode != RESULT_OK) {
                    return;
                }
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
        if (requestCode == resultCode && requestCode == TransConstant.REFRESH) {
            if (null != data) {
                String type = data.getStringExtra(TransConstant.TYPE);
                if ("board".equals(type)) {
                    boardId = data.getStringExtra(TransConstant.ID);
                    subBoardId = "";
                    subBoardName = "";
                    tvCategory.setText("分类");
                    boardName = data.getStringExtra(TransConstant.TITLE);
                    tvBoard.setText(String.format("版块：%s", boardName));
                    layoutCategory.performClick();
                } else {
                    subBoardId = data.getStringExtra(TransConstant.ID);
                    subBoardName = data.getStringExtra(TransConstant.TITLE);
                    tvCategory.setText(String.format("分类：%s", subBoardName));
                }
            }
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doSave();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doSave() {
        if (!TextUtils.isEmpty(subject) || !TextUtils.isEmpty(content)) {
            new AlertDialog.Builder(mContext)
                    .setMessage(R.string.post_save_cache_tips)
                    .setPositiveButton(R.string.save, (dialog, which) -> {
                        List<EditDataObject> editList = etContent.buildEditData();
                        cachePost = new CachePostObject();
                        cachePost.boardId = boardId;
                        cachePost.boardName = boardName;
                        cachePost.subBoardId = subBoardId;
                        cachePost.subBoardName = subBoardName;
                        cachePost.subject = subject;

                        if (editList.size() > 0) {
                            cachePost.editList = new ArrayList<EditDataObject>();
                            cachePost.editList.addAll(editList);
                        }
                        String strCache = JSON.toJSONString(cachePost);
                        SPUtils.put(mContext, SPConstant.CACHE_POST, strCache);
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(TopicSendActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        etContent.setKeyboardDismiss();
                        dialog.dismiss();
                        finish();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        SPUtils.put(mContext, SPConstant.CACHE_POST, "");
                        dialog.dismiss();
                        finish();
                    }).show();
        } else {
            SPUtils.put(mContext, SPConstant.CACHE_POST, "");
            finish();
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

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String       data   = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
