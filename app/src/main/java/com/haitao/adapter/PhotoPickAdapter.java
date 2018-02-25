package com.haitao.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.haitao.R;
import com.haitao.model.PhotoPickImageObject;
import com.haitao.utils.ImageLoaderUtils;
import com.haitao.view.CustomImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/18.
 */
public class PhotoPickAdapter extends BaseAdapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean                         showCamera      = true;
    private boolean                         mode_single     = false; //是否单选　默认为多选
    private ArrayList<PhotoPickImageObject> mImages         = new ArrayList<PhotoPickImageObject>();
    private ArrayList<PhotoPickImageObject> mSelectedImages = new ArrayList<PhotoPickImageObject>();
    private OnChekClickLitener onChekClickLitener;
    int screenWidth = 0;

    public void setOnChekClickLitener(OnChekClickLitener onChekClickLitener) {
        this.onChekClickLitener = onChekClickLitener;
    }

    private int                   mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    public PhotoPickAdapter(Context context, boolean showCamera, boolean mode_single) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        this.mode_single = mode_single;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
        screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param photoPickImageInfo
     */
    public void select(PhotoPickImageObject photoPickImageInfo) {
        if (mSelectedImages.contains(photoPickImageInfo)) {
            mSelectedImages.remove(photoPickImageInfo);
        } else {
            mSelectedImages.add(photoPickImageInfo);
        }

    }

    public void removeOne(String path) {
        PhotoPickImageObject photoPickImageInfo = getImageByPath(path);
        if (mSelectedImages.contains(photoPickImageInfo)) {
            mSelectedImages.remove(photoPickImageInfo);
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getImages() {
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < mImages.size(); i++) {
            data.add(mImages.get(i).path);
        }
        return data;
    }

    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList
     */
    public void setSelectedList(ArrayList<String> resultList) {
        mSelectedImages.clear();
        for (String path : resultList) {
            PhotoPickImageObject photoPickImageInfo = getImageByPath(path);
            if (photoPickImageInfo != null) {
                mSelectedImages.add(photoPickImageInfo);
            }
        }
    }

    private PhotoPickImageObject getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (PhotoPickImageObject photoPickImageInfo : mImages) {
                if (photoPickImageInfo.path.equalsIgnoreCase(path)) {
                    return photoPickImageInfo;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     *
     * @param photoPickImageInfos
     */
    public void setData(ArrayList<PhotoPickImageObject> photoPickImageInfos) {
        mSelectedImages.clear();

        if (photoPickImageInfos != null && photoPickImageInfos.size() > 0) {
            mImages = photoPickImageInfos;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public PhotoPickImageObject getItem(int position) {
        if (showCamera) {
            if (position == 0) {
                return null;
            }
            return mImages.get(position - 1);
        } else {
            return mImages.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == TYPE_CAMERA) {
            convertView = mInflater.inflate(R.layout.item_photo_pick_camera, parent, false);
            convertView.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_photo_pick, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                if (holder == null) {
                    convertView = mInflater.inflate(R.layout.item_photo_pick, parent, false);
                    holder = new ViewHolder(convertView);
                }
            }
            if (holder != null) {
                holder.checkBox.setOnClickListener(v -> {
                    PhotoPickImageObject mPhotoPickImageInfo = getItem(position);

                    if (onChekClickLitener != null) {
                        onChekClickLitener.onCheckedClick(mPhotoPickImageInfo, v);
                    }


                });
                holder.bindData(getItem(position));
            }
        }


        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) convertView.getLayoutParams();
        if (lp.height != mItemSize) {
            convertView.setLayoutParams(mItemLayoutParams);
        }

        return convertView;
    }

    class ViewHolder {
        CustomImageView image;
        ImageView       checkBox;

        ViewHolder(View view) {
            image = view.findViewById(R.id.image);
            checkBox = view.findViewById(R.id.checkmark);
            DisplayMetrics           dm        = mContext.getResources().getDisplayMetrics();
            float                    density   = dm.density;
            int                      itemWidth = (int) (screenWidth - density * 2 * 3) / 4;
            FrameLayout.LayoutParams lp        = new FrameLayout.LayoutParams(itemWidth, itemWidth);
            image.setLayoutParams(lp);
            view.setTag(this);
        }

        void bindData(final PhotoPickImageObject data) {
            if (data == null) return;
            if (!mode_single) {
                //多选状态
                checkBox.setVisibility(View.VISIBLE);
                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    checkBox.setSelected(true);
                } else {
                    // 未选择
                    checkBox.setSelected(false);
                }
            } else {
                checkBox.setVisibility(View.GONE);
            }
            if (mItemSize > 0) {
                ImageLoaderUtils.showLocationImage(mContext, "file://" + data.path, image);
                /*ImageLoaderUtils.showLocationImage(data.path, image, R.mipmap.ic_photo_loading, new ImageSize(200, 200), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        super.onLoadingStarted(imageUri, view);
                    }
                });*/

            }
        }
    }

    public interface OnChekClickLitener {
        void onCheckedClick(PhotoPickImageObject info, View view);
    }
}
