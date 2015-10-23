package com.nightfair.mobille.task;

import java.io.File;
import com.nightfair.mobille.util.FileUtils;
import android.content.Context;
import android.os.AsyncTask;



public class FileCalculateAsyncTask extends AsyncTask<File, Void, Long> {

	public FileCalculateAsyncTask(Context context) {

	}

	private OnResponseListener onResponseListener;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Long doInBackground(File... params) {
		return FileUtils.getFileSize(params[0]);
	}

	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		if (null != onResponseListener) {
			onResponseListener.onResponse(String.valueOf(result));
		}
	}

	public OnResponseListener getResponseListener() {
		return onResponseListener;
	}

	public void setOnResponseListener(OnResponseListener onResponseListener) {
		this.onResponseListener = onResponseListener;
	}
}
