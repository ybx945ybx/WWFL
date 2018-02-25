package com.haitao.model;

/**
 * 图片的实体
 * Created by pxl(彭小利) on 2015/9/18.
 */
public class PhotoPickImageObject implements Comparable<PhotoPickImageObject> {
    public String path = "";
    public String name = "";
    public long time = 0;

    public PhotoPickImageObject(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            PhotoPickImageObject other = (PhotoPickImageObject) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }


    @Override
    public int compareTo(PhotoPickImageObject another) {
        if (another == null) {
            return 1;
        }
        return (int) ((another.time - time) / 1000);
    }


}
