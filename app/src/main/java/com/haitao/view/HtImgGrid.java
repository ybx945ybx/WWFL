package com.haitao.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.haitao.R;
import com.haitao.utils.ImageLoaderUtils;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 图片选择条目
 *
 * @author 陶声
 * @since 2018-01-10
 */

public class HtImgGrid extends FrameLayout {

    @BindView(R.id.img_cover)      CustomImageView mImgCover;
    @BindView(R.id.ib_close_cover) ImageButton     mIbCloseCover;

    @BindDrawable(R.drawable.border_rect_orange_2dp) Drawable mBgImgSelected;

    @BindDimen(R.dimen.grid_img_size) int mImgSize;

    private CallbackListener mListener;
    private Context          mContext;
    private boolean          mIsEmpty;

    public HtImgGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context mContext
     * @param attrs   attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mIsEmpty = true;
        LayoutInflater.from(context).inflate(R.layout.layout_img_grid, this);
        ButterKnife.bind(this);
    }

    /**
     * 设置图片
     */
    public void setImg(String imgUrl) {
        mIsEmpty = false;
        mImgCover.setVisibility(VISIBLE);
        ImageLoaderUtils.showOnlineImage(imgUrl, mImgCover);
        if (imgUrl.startsWith("http")) {
            ImageLoaderUtils.showOnlineImage(imgUrl, mImgCover);
        } else {
            ImageLoaderUtils.showLocationImage(imgUrl, mImgCover, R.mipmap.ic_default_240);
        }
        setImgSelected(true);
    }

    /**
     * 清空图片
     */
    public void clearImg() {
        mIsEmpty = true;
        mImgCover.setVisibility(GONE);
        mIbCloseCover.setVisibility(GONE);
    }

    /**
     * 设置选中状态
     */
    public void setImgSelected(boolean selected) {
        if (selected) {
            mImgCover.setVisibility(VISIBLE);
        }
        mImgCover.setBackground(selected ? mBgImgSelected : null);
        mIbCloseCover.setVisibility(selected ? VISIBLE : GONE);
    }

    /**
     * 清除图片
     */
    @OnClick(R.id.ib_close_cover)
    public void onClickClose() {
        clearImg();
        if (mListener != null) {
            mListener.onClose();
        }
    }

    /**
     * 添加图片
     */
    @OnClick(R.id.ll_add_img)
    public void onClickAdd() {
        if (mListener != null) {
            if (mIsEmpty) {
                mListener.onAdd();
            } else {
                setImgSelected(true);
                mListener.onSelected();
            }
        }
    }

    public interface CallbackListener {
        void onClose();

        void onAdd();

        void onSelected();
    }

    public HtImgGrid setCallbackListener(CallbackListener listener) {
        mListener = listener;
        return this;
    }
}
