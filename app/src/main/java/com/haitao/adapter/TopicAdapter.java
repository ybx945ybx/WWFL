package com.haitao.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haitao.R;
import com.haitao.activity.PreviewActivity;
import com.haitao.activity.QuickLoginActivity;
import com.haitao.activity.TalentDetailActivity;
import com.haitao.activity.TopicDetailActivity;
import com.haitao.common.Constant.TransConstant;
import com.haitao.common.UserManager;
import com.haitao.connection.api.ForumApi;
import com.haitao.model.PhotoPickParameterObject;
import com.haitao.utils.FileUtils;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.utils.ScreenUtils;
import com.haitao.utils.ShareUtils;
import com.haitao.utils.ToastUtils;
import com.haitao.utils.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.haitao.view.CanDoBlankGridView;
import com.haitao.view.CustomImageView;
import com.haitao.view.tag.TagCloudLayout;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.TopicModel;


/**
 * 帖子 - Adapter
 * Created by tqy on 2015/11/20.
 */
public class TopicAdapter extends BaseListAdapter<TopicModel> {
    public boolean isTimeShow    = true;
    public boolean isAvatorClick = true;

    public String subTitle = "";

    public TopicAdapter(Context context, List<TopicModel> data) {
        super(context, data);
    }

    public boolean isDelete = false;

    public interface OnItemClickLitener {
        void onAgreeClick(int position, TopicModel object);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_topic, null);
            holder.viewSeparate = getView(convertView, R.id.viewSeparate);
            holder.ivAvator = getView(convertView, R.id.img_avatar);
            holder.tvAuthor = getView(convertView, R.id.tvAuthor);
            holder.tvTime = getView(convertView, R.id.tvTime);
            holder.tvTitle = getView(convertView, R.id.tvTitle);
            holder.tvComment = getView(convertView, R.id.tvComment);
            holder.tvAgree = getView(convertView, R.id.tvAgree);
            holder.gvList = getView(convertView, R.id.gv_order_pics);
            holder.btnChoose = getView(convertView, R.id.btnChoose);
            holder.tvBest = getView(convertView, R.id.tvBest);
            holder.tvHot = getView(convertView, R.id.tvHot);
            holder.tagLayout = getView(convertView, R.id.tagLayout);
            holder.tvSubTitle = getView(convertView, R.id.tvSubTitle);
            holder.layoutRecommend = getView(convertView, R.id.layoutRecommend);
            holder.ivPic = getView(convertView, R.id.ivPic);
            holder.ivShare = getView(convertView, R.id.ivShare);
            int with = (ScreenUtils.getScreenWidth((Activity) mContext) - 2 * 15);
            holder.ivPic.setLayoutParams(new RelativeLayout.LayoutParams(with, with * 9 / 16));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvTime.setVisibility(isTimeShow ? View.VISIBLE : View.GONE);
        final TopicModel obj = mList.get(position);
        if (null != obj) {
            if ("1".equals(obj.getIsRecommended()) && null != obj.getPics() && obj.getPics().size() > 0) {
                holder.layoutRecommend.setVisibility(View.VISIBLE);
                if (null != obj.getPics() && obj.getPics().size() > 0) {
                    Logger.d("recommend Pic = " + obj.getPics().get(0));
                    ImageLoaderUtils.showOnlineImage(obj.getPics().get(0), holder.ivPic);
                }
            } else {
                holder.layoutRecommend.setVisibility(View.GONE);
            }
            ImageLoaderUtils.showOnlineGifImage(obj.getAvatar(), holder.ivAvator);
            holder.tvAuthor.setText(obj.getAuthorName());
            holder.tvTitle.setText(obj.getTitle());
            holder.tvSubTitle.setVisibility(!TextUtils.isEmpty(subTitle) && 0 == position ? View.VISIBLE : View.GONE);
            holder.tvSubTitle.setText(subTitle);
            holder.tvTime.setText(obj.getPostTime());
            holder.tvComment.setText(obj.getReplyCount());
            holder.tvAgree.setText(obj.getPraiseCount());
            holder.tvAgree.setSelected("1".equals(obj.getIsPraised()));
            holder.tvBest.setVisibility(TextUtils.isEmpty(obj.getIsBest()) || "0".equals(obj.getIsBest()) ? View.GONE : View.VISIBLE);
            holder.tvHot.setVisibility("1".equals(obj.getIsHot()) ? View.VISIBLE : View.GONE);
            holder.btnChoose.setVisibility(isDelete ? View.VISIBLE : View.GONE);
            holder.ivShare.setOnClickListener(v -> ImageLoaderUtils.downloadOnlineImage(mContext, obj.getSharePic(), status -> {
                String picUrl = FileUtils.getPicPath(mContext) + FileUtils.getFileName(obj.getSharePic());
                if (!status || !new File(picUrl).exists()) {
                    if (!new File(FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share")).exists()) {//处理分享的图片
                        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                        picUrl = FileUtils.saveBitmap(mContext, bitmap, new Md5FileNameGenerator().generate("share"));
                    } else {
                        picUrl = FileUtils.getPicPath(mContext) + new Md5FileNameGenerator().generate("share");
                    }
                }
                Message msg    = new Message();
                Bundle  bundle = new Bundle();
                bundle.putString("title", obj.getShareTitle());
                bundle.putString("content", obj.getShareContent());
                bundle.putString("content_weibo", obj.getShareContentWeibo());
                bundle.putString("url", obj.getShareUrl());
                bundle.putString("picUrl", picUrl);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }));
            mList.set(position, obj);
            if (!"1".equals(obj.getIsRecommended()) && null != obj.getPics() && obj.getPics().size() > 0) {
                holder.gvList.setVisibility(View.VISIBLE);
                List             list     = obj.getPics();
                PostImageAdapter mAdapter = new PostImageAdapter(mContext, list);

                holder.gvList.setAdapter(mAdapter);
                final PhotoPickParameterObject mPhotoPickParameterInfo = new PhotoPickParameterObject();
                mPhotoPickParameterInfo.image_list = new ArrayList<>();
                mPhotoPickParameterInfo.image_list.addAll(list);
                holder.gvList.setOnItemClickListener((parent1, view, position1, id) -> {
                    //openImagePreview(mPhotoPickParameterInfo, position);
                    TopicDetailActivity.launch(mContext, obj.getTid());
                });
                holder.gvList.setOnTouchInvalidPositionListener(motionEvent -> {
            /*当返回false的时候代表交由父级控件处理，当return true的时候表示你已经处理了该事件并不

让该事件再往上传递。为了出发listview的item点击就得返回false了*/
                    return false;
                });
            } else {
                holder.gvList.setVisibility(View.GONE);
            }
            holder.tvAgree.setOnClickListener(v -> {
                if (!UserManager.getInstance().isLogin()) {
                    QuickLoginActivity.launch(mContext);
                    return;
                }
                if (!holder.tvAgree.isSelected()) {
                    addAgree(holder.tvAgree, position, obj);
                }
            });
            holder.tvComment.setOnClickListener(view -> TopicDetailActivity.launch(mContext, obj.getTid(), true));
            if (isAvatorClick) {
                holder.ivAvator.setOnClickListener(v -> TalentDetailActivity.launch(mContext, obj.getAuthorUid()));
            }
            if (null != obj.getTags() && obj.getTags().size() > 0) {
                ForumTagAdapter tagAdapter = new ForumTagAdapter(mContext, obj.getTags());
                holder.tagLayout.setAdapter(tagAdapter);
                holder.tagLayout.setVisibility(View.VISIBLE);
            } else {
                holder.tagLayout.setVisibility(View.GONE);
            }
        }
        return convertView;
    }


    private void addAgree(final TextView tvAgree, final int position, final TopicModel topicModel) {
        ForumApi.getInstance().userPraisingPost(TransConstant.praiseType.POST, topicModel.getTid(),
                response -> {
                    if ("0".equals(response.getCode())) {
                        String count      = tvAgree.getText().toString();
                        int    agreeCount = Integer.parseInt(count) + 1;
                        topicModel.setIsPraised("1");
                        topicModel.setPraiseCount(String.valueOf(agreeCount));
                        tvAgree.setSelected(true);
                        tvAgree.setText(String.valueOf(agreeCount));
                        if (null != mOnItemClickLitener) {
                            mOnItemClickLitener.onAgreeClick(position, topicModel);
                        }
                    } else {
                        ToastUtils.show(mContext, response.getMsg());
                    }
                }, error -> {

                });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            ShareUtils.showShareDialog(mContext, 1, bundle.getString("title"), bundle.getString("content"), bundle.getString("content_weibo"), bundle.getString("url"), bundle.getString("picUrl"));
        }
    };


    private class Holder {
        View               viewSeparate;
        CustomImageView    ivAvator;
        TextView           tvTitle;
        TextView           tvComment;
        TextView           tvAgree;
        TextView           tvAuthor;
        TextView           tvTime;
        ImageView          tvBest;
        ImageView          tvHot;
        ImageButton        btnChoose;
        CanDoBlankGridView gvList;
        TagCloudLayout     tagLayout;
        TextView           tvSubTitle;
        ViewGroup          layoutRecommend;
        CustomImageView    ivPic;
        ImageView          ivShare;
    }

    //图片预览

    public void openImagePreview(PhotoPickParameterObject mPhotoPickParameterInfo, int position) {
        mPhotoPickParameterInfo.position = position;
        Intent intent = new Intent();
        intent.setClass(mContext, PreviewActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(PhotoPickParameterObject.EXTRA_PARAMETER, mPhotoPickParameterInfo);
        b.putString(TransConstant.TYPE, "view");
        intent.putExtras(b);
        mContext.startActivity(intent);
    }
}
