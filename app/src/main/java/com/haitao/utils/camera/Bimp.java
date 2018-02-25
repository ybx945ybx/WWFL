package com.haitao.utils.camera;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bimp extends Application{
	public static int max = 0;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	//	public static List<Boolean> intList = new ArrayList<Boolean>();	
	public static List<String> delList = new ArrayList<String>();	
	public static Map<String,String> photodel = new HashMap<String,String>();	
	public static HashMap<String, Boolean> mHashMap = new HashMap<String, Boolean>();

	//图片Sr地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static ArrayList<String> drr = new ArrayList<String>();
	public static HashMap <String , String> desn = new HashMap<String ,String>();
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 800)
					&& (options.outHeight >> i <= 800)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				//				options.inSampleSize = 2;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return  bitmap;

	}

}
