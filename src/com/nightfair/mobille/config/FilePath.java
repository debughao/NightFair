package com.nightfair.mobille.config;

import com.nightfair.mobille.util.FileUtils;

import android.content.Context;


public class FilePath {

	public static String getCameraStore(Context c){
		
		return  FileUtils.getPicturesStorageDir(c,"CameraStore").getAbsolutePath();
	}

}
