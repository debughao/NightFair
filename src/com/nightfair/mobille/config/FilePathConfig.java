package com.nightfair.mobille.config;

import java.io.File;

import com.nightfair.mobille.util.FileUtils;

import android.content.Context;

/**
 * 
 * @ClassName: FilePath
 * @Description: TODO(文件存储路径配置)
 * @author debughao
 * @date 2015年10月18日
 */
public class FilePathConfig {

	public static String getCameraStore(Context c) {

		return FileUtils.getPicturesStorageDir(c, "CameraStore").getAbsolutePath();
	}

	/**
	 * 获取临时图片目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getTemp(Context c) {
		return FileUtils.getPicturesStorageDir(c, "temp").getAbsolutePath();
	}

	/**
	 * 获取外部缓存目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getExternalCachePath(Context context) {
		return FileUtils.getExternalCacheDir(context);
	}
	/**
	 * 获取头像目录
	 * 
	 * @param context
	 * 
	 */
	public static String getHeadFaceParh(Context context) {
		return getExternalCachePath(context) + File.separator + "hd";

	}
	public static String getPushMessageDbPath(Context context) {
		return getExternalCachePath(context) + File.separator + "PushMessage";
	}
}

