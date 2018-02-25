package com.haitao.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 图片选择设置
 */
public class PhotoPickParameterObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EXTRA_PARAMETER = "extra_parameter";
    public static final int TAKE_PICTURE_FROM_CAMERA = 100;
    public static final int TAKE_PICTURE_FROM_GALLERY = 200;
    public static final int TAKE_PICTURE_PREVIEW = 300;

    public int max_image = 9;                //最大图片选择数，int类型，默认9
    public boolean single_mode = false;    //图片选择模式，默认多选
    public boolean show_camera = true;        //是否显示相机，默认显示
    public boolean croper_image = false;        //正方形的裁剪图片必须与单张相结合
    public int position = 0;                //在预览时带的下标，也可作为标识使用
    public boolean filter_image = false;        //使用滤镜功能
    public int type = 0;//表示返回类型，0为一般。1为完成
    public ArrayList<String> image_list;        //已经选择的图片集
    public ArrayList<String> image_data;//列表数据
    public ArrayList<String> link_list;

}
