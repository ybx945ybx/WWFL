package com.haitao.model;
import java.util.ArrayList;

/**
 * Created by pxl(彭小利) on 2015/9/22.
 */
public class PhotoFolderObject {
    public String name;
    public String path;
    public PhotoPickImageObject cover;
    public ArrayList<PhotoPickImageObject> imageInfos;

    @Override
    public boolean equals(Object o) {
        try {
            PhotoFolderObject other = (PhotoFolderObject) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
